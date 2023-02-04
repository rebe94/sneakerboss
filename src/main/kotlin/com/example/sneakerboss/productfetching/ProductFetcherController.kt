package com.example.sneakerboss.productfetching

import com.example.sneakerboss.captchaverifing.CaptchaRedirector
import com.example.sneakerboss.matchingproductfetching.MatchingProductFetcherController
import com.example.sneakerboss.productfetching.components.Product
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.HttpClientErrorException
import java.util.*

@Controller
class ProductFetcherController(private val productFetcher: ProductFetcher, private val captchaRedirector: CaptchaRedirector) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ProductFetcherController::class.java)
    }

    @GetMapping("/product")
    private fun product(
        @RequestParam uuid: String,
        @RequestParam(required = false) sortBy: String,
        page: Model
    ): String {
        val product: Product?
        try {
            product = productFetcher.findProductBy(UUID.fromString(uuid))
        } catch (ex: HttpClientErrorException) {
            LOGGER.info("${ex.message}")
            LOGGER.info("User redirected to resolve captcha")
            return captchaRedirector.getCaptchaRedirectingUrl()
        }
        val childrenProducts = product?.children
        addAttributes(page, product, sortBy, childrenProducts)

        return "productfetcher.html"
    }

    private fun addAttributes(page: Model, product: Product?, sortBy: String, childrenProducts: List<Product>?) {
        val sortByParam = if (sortBy == null) "" else sortBy
        val sortedChildrenProducts = when (sortByParam) {
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
        page.addAttribute("parentProduct", product)
        page.addAttribute("childrenProducts", sortedChildrenProducts)
    }
}