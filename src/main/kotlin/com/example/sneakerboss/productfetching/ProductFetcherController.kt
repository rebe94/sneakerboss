package com.example.sneakerboss.productfetching

import com.example.sneakerboss.productfetching.components.Product
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@Controller
class ProductFetcherController(private val productFetcher: ProductFetcher) {

    @GetMapping("/product")
    private fun product(
        @RequestParam uuid: String,
        @RequestParam(required = false) sortBy: String,
        page: Model
    ): String {
        val product = productFetcher.findProductBy(UUID.fromString(uuid))
        val childrenProducts = product?.children
        addAttributes(page, product, sortBy, childrenProducts)

        return "productfetcher.html"
    }

    private fun addAttributes(page: Model, product: Product?, sortBy: String, childrenProducts: List<Product>?) {
        val sortByParam = if (sortBy == null) "" else sortBy
        val sortedChildrenProducts = when (sortByParam) {
            "lowestAsk" -> childrenProducts?.sortedByDescending { it.lowestAsk }
            "numberOfAsks" -> childrenProducts?.sortedByDescending { it.numberOfAsks }
            "highestBid" -> childrenProducts?.sortedByDescending { it.highestBid }
            "numberOfBids" -> childrenProducts?.sortedByDescending { it.numberOfBids }
            "deadstockSold" -> childrenProducts?.sortedByDescending { it.deadstockSold }
            else -> childrenProducts?.sortedBy { it.shoeSize?.replace(Regex("[a-zA-Z]"), "")?.toDouble() }
        }
        page.addAttribute("parentProduct", product)
        page.addAttribute("childrenProducts", sortedChildrenProducts)
    }
}