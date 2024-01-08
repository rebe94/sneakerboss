package com.example.sneakerboss.productdetailsfetching

import com.example.sneakerboss.commons.captchaverifing.CaptchaRedirector
import com.example.sneakerboss.productdetailsfetching.dto.ProductDetailsDto
import com.example.sneakerboss.commons.productfetching.productmarkerdatafetching.ShoeVariant
import com.example.sneakerboss.userproductfetching.dto.UserSettingDto
import com.example.sneakerboss.usersettings.UserSettingService
import com.example.sneakerboss.uservalidating.UserValidator
import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.HttpClientErrorException
import java.util.*

@Controller
class ProductDetailsFetcherController(
    private val productDetailsFetcher: ProductDetailsFetcher,
    private val captchaRedirector: CaptchaRedirector,
    private val userValidator: UserValidator,
    private val userSettingService: UserSettingService
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(this::class.java)
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
        var userSetting = UserSettingDto.DEFAULT
        try {
            if (isUserLogged) {
                val validUser = userValidator.validUser(oauth2User?.attributes?.getValue("email").toString())
                userSetting = userSettingService.getUserSettings(validUser.email)!!
            }
            productDetailsDto = productDetailsFetcher.findProductBy(UUID.fromString(uuid), userSetting)
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
        val shoeVariants = productDetailsDto?.shoeVariants
        val sortByParam = if (sortBy == null) "" else sortBy
        val sortedShoeVariants = sortShoeVariantsBy(sortByParam, shoeVariants)
        page.addAttribute("productDetails", productDetailsDto)
        page.addAttribute("sortedShoeVariants", sortedShoeVariants)
    }

    private fun sortShoeVariantsBy(sortByParam: String, shoeVariants: List<ShoeVariant>?) =
        when (sortByParam) {
            "lowestAsk" -> shoeVariants?.sortedByDescending { it.lowestAsk }
            "askToBeFirst" -> shoeVariants?.sortedByDescending { it.askToBeFirst }
            "totalPayout" -> shoeVariants?.sortedByDescending { it.totalPayout }
            "totalPayoutPln" -> shoeVariants?.sortedByDescending { it.totalPayoutPln }
            "numberOfAsks" -> shoeVariants?.sortedByDescending { it.numberOfAsks }
            "highestBid" -> shoeVariants?.sortedByDescending { it.highestBid }
            "numberOfBids" -> shoeVariants?.sortedByDescending { it.numberOfBids }
            "deadstockSold" -> shoeVariants?.sortedByDescending { it.deadstockSold }
            "salesLast72Hours" -> shoeVariants?.sortedByDescending { it.salesLast72Hours }
            else -> shoeVariants?.sortedBy { it.size.replace(Regex("[a-zA-Z]"), "").toDouble() }
        }
}