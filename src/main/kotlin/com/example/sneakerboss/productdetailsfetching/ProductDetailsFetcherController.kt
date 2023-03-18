package com.example.sneakerboss.productdetailsfetching

import com.example.sneakerboss.commons.captchaverifing.CaptchaRedirector
import com.example.sneakerboss.productdetailsfetching.dto.ProductDetailsDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.HttpClientErrorException
import java.util.*

@Controller
class ProductDetailsFetcherController(
    private val productDetailsFetcher: ProductDetailsFetcher,
    private val captchaRedirector: CaptchaRedirector
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ProductDetailsFetcherController::class.java)
    }

    @GetMapping("/products")
    private fun products(
        @RequestParam uuid: String,
        @RequestParam(required = false) sortBy: String,
        page: Model
    ): String {
        val productDetailsDto: ProductDetailsDto?
        try {
            productDetailsDto = productDetailsFetcher.findProductBy(UUID.fromString(uuid))
        } catch (ex: HttpClientErrorException) {
            LOGGER.info("User redirected to resolve captcha.")
            return captchaRedirector.getHtmlWithCaptchaContent(page, ex.message)
        }
        addAttributes(page, productDetailsDto, sortBy)

        return "productfetcher.html"
    }

    private fun addAttributes(page: Model, productDetailsDto: ProductDetailsDto?, sortBy: String) {
        val childrenProducts = productDetailsDto?.children
        val sortByParam = if (sortBy == null) "" else sortBy
        val sortedChildrenProducts = sortChildrenProductsBy(sortByParam, childrenProducts)
        page.addAttribute("parentProduct", productDetailsDto)
        page.addAttribute("childrenProducts", sortedChildrenProducts)
    }

    private fun sortChildrenProductsBy(sortByParam: String, childrenProducts: List<ProductDetailsDto>?) = when (sortByParam) {
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