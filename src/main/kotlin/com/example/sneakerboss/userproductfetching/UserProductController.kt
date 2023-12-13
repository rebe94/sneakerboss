package com.example.sneakerboss.userproductfetching

import com.example.sneakerboss.commons.captchaverifing.CaptchaRedirector
import com.example.sneakerboss.extensions.round
import com.example.sneakerboss.userproductfetching.dto.UserProductDto
import com.example.sneakerboss.usersettings.UserSettingService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.ModelAndView
import java.util.*
import javax.servlet.http.HttpServletRequest

@Controller
class UserProductController(
    private val userProductService: UserProductService,
    private val captchaRedirector: CaptchaRedirector,
    private val userSettingService: UserSettingService
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(this::class.java)
    }

    @GetMapping("/user/products")
    private fun userProducts(
        @AuthenticationPrincipal oauth2User: OAuth2User?,
        @RequestParam(required = false) sortBy: String,
        page: Model
    ): String {
        val userProducts: List<UserProductDto>?
        try {
            userProducts = userProductService.getUserProducts(oauth2User?.attributes?.getValue("email").toString())
        } catch (ex: HttpClientErrorException) {
            LOGGER.info("User redirected to resolve captcha.")
            return captchaRedirector.getHtmlWithCaptchaContent(page, ex.message)
        }
        val userSettings = userSettingService.getUserSettings(oauth2User?.attributes?.getValue("email").toString())
        userSettings ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User settings not found")
        addAttributes(page, userProducts, sortBy)
        page.addAttribute("currencyCode", userSettings.currencyCode)

        return "userproduct.html"
    }

    @PostMapping("/user/products/add")
    private fun addUserProduct(
        @AuthenticationPrincipal oauth2User: OAuth2User?,
        @RequestParam parentProductUuid: String,
        @RequestParam shoeVariantUuid: String,
        request: HttpServletRequest
    ): ModelAndView {
        val userEmail = oauth2User?.attributes?.getValue("email").toString()
        userProductService.addUserProduct(UUID.fromString(parentProductUuid), UUID.fromString(shoeVariantUuid), userEmail)

        return ModelAndView("redirect:${request.getHeader("referer")}")
    }

    @PostMapping("/user/products/delete")
    private fun deleteUserProduct(
        @AuthenticationPrincipal oauth2User: OAuth2User?,
        @RequestParam userProductId: String,
        request: HttpServletRequest
    ): ModelAndView {
        userProductService.deleteUserProduct(UUID.fromString(userProductId))

        return ModelAndView("redirect:${request.getHeader("referer")}")
    }

    private fun addAttributes(page: Model, userProductDtos: List<UserProductDto>, sortBy: String) {
        val sortByParam = if (sortBy == null) "" else sortBy
        val sortedUserProducts = sortUserProductsBy(sortByParam, userProductDtos)
        val aggregation = calculateTotalNumbers(userProductDtos)
        page.addAttribute("userProducts", sortedUserProducts)
        page.addAttribute("aggregation", aggregation)
    }

    private fun calculateTotalNumbers(userProductDtos: List<UserProductDto>): Aggregation {
        val retailPrice = userProductDtos.sumOf { it.retailPrice }
        val lowestAsk = userProductDtos.sumOf { it.lowestAsk }
        val totalPayout = userProductDtos.sumOf { it.totalPayout.toDouble() }.round(2)
        val totalPayoutPln = userProductDtos.sumOf { it.totalPayoutPln?.toDouble() ?: 0.0 }.round(2)
        return Aggregation(retailPrice, lowestAsk, totalPayout, totalPayoutPln)
    }

    private fun sortUserProductsBy(sortByParam: String, userProducts: List<UserProductDto>) = when (sortByParam) {
        "styleId" -> userProducts?.sortedByDescending { it.styleId }
        "shoeSize" -> userProducts?.sortedByDescending { it.shoeSize?.replace(Regex("[a-zA-Z]"), "")?.toDouble() }
        "releaseDate" -> userProducts?.sortedByDescending { it.releaseDate }
        "retailPrice" -> userProducts?.sortedByDescending { it.retailPrice }
        "lowestAsk" -> userProducts?.sortedByDescending { it.lowestAsk }
        "askToBeFirst" -> userProducts?.sortedByDescending { it.askToBeFirst }
        "totalPayout" -> userProducts?.sortedByDescending { it.totalPayout }
        "totalPayoutPln" -> userProducts?.sortedByDescending { it.totalPayoutPln }
        "numberOfAsks" -> userProducts?.sortedByDescending { it.numberOfAsks }
        "highestBid" -> userProducts?.sortedByDescending { it.highestBid }
        "numberOfBids" -> userProducts?.sortedByDescending { it.numberOfBids }
        "deadstockSold" -> userProducts?.sortedByDescending { it.deadstockSold }
        "salesLast72Hours" -> userProducts?.sortedByDescending { it.salesLast72Hours }
        else -> userProducts?.sortedBy { it.title }
    }

    data class Aggregation(
        val retailPrice: Int,
        val lowestAsk: Int,
        val totalPayout: Double,
        val totalPayoutPln: Double
    )
}