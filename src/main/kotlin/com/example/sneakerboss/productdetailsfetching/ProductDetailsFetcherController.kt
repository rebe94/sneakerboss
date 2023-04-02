package com.example.sneakerboss.productdetailsfetching

import com.example.sneakerboss.commons.captchaverifing.CaptchaRedirector
import com.example.sneakerboss.productdetailsfetching.dto.ProductDetailsDto
import com.example.sneakerboss.userproductfetching.dto.UserSettingDto
import com.example.sneakerboss.usersettings.UserSettingService
import com.example.sneakerboss.usersettings.entity.UserSetting
import com.example.sneakerboss.uservalidating.UserValidator
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Controller
class ProductDetailsFetcherController(
    private val productDetailsFetcher: ProductDetailsFetcher,
    private val captchaRedirector: CaptchaRedirector,
    private val userValidator: UserValidator,
    private val userSettingService: UserSettingService
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ProductDetailsFetcherController::class.java)
    }

    @GetMapping("/products")
    private fun products(
        @AuthenticationPrincipal oauth2User: OAuth2User?,
        @RequestParam uuid: String,
        @RequestParam(required = false) sortBy: String,
        page: Model
    ): String {
        val isUserLogged = oauth2User != null
        val productDetailsDto: ProductDetailsDto?
        val userSetting: UserSettingDto
        try {
            if (isUserLogged) {
                val validUser = userValidator.validUser(oauth2User?.attributes?.getValue("email").toString())
                userSetting = userSettingService.getUserSettings(validUser.email)!!
                productDetailsDto = productDetailsFetcher.findProductBy(UUID.fromString(uuid), userSetting)
            } else {
                userSetting = UserSettingDto.DEFAULT
                productDetailsDto = productDetailsFetcher.findProductBy(UUID.fromString(uuid), userSetting)
            }
        } catch (ex: HttpClientErrorException) {
            LOGGER.info("User redirected to resolve captcha. \n ${ex.message}")
            return captchaRedirector.getHtmlWithCaptchaContent(page, ex.message)
        }
        page.addAllAttributes(
            mapOf(
                "isUserLogged" to isUserLogged,
                "currencyCode" to userSetting.currencyCode
            )
        )
        addAttributes(page, productDetailsDto, sortBy)

        return "product.html"
    }

    private fun addAttributes(page: Model, productDetailsDto: ProductDetailsDto?, sortBy: String) {
        val childrenProducts = productDetailsDto?.children
        val sortByParam = if (sortBy == null) "" else sortBy
        val sortedChildrenProducts = sortChildrenProductsBy(sortByParam, childrenProducts)
        page.addAttribute("parentProduct", productDetailsDto)
        page.addAttribute("childrenProducts", sortedChildrenProducts)
    }

    private fun sortChildrenProductsBy(sortByParam: String, childrenProducts: List<ProductDetailsDto>?) =
        when (sortByParam) {
            "lowestAsk" -> childrenProducts?.sortedByDescending { it.lowestAsk }
            "askToBeFirst" -> childrenProducts?.sortedByDescending { it.askToBeFirst }
            "totalPayout" -> childrenProducts?.sortedByDescending { it.totalPayout }
            "totalPayoutPln" -> childrenProducts?.sortedByDescending { it.totalPayoutPln }
            "numberOfAsks" -> childrenProducts?.sortedByDescending { it.numberOfAsks }
            "highestBid" -> childrenProducts?.sortedByDescending { it.highestBid }
            "numberOfBids" -> childrenProducts?.sortedByDescending { it.numberOfBids }
            "deadstockSold" -> childrenProducts?.sortedByDescending { it.deadstockSold }
            "salesLast72Hours" -> childrenProducts?.sortedByDescending { it.salesLast72Hours }
            else -> childrenProducts?.sortedBy { it.shoeSize?.replace(Regex("[a-zA-Z]"), "")?.toDouble() }
        }
}