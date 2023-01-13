package com.example.sneakerboss.matchingproductfetching

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class MatchingProductFetcherController(private val matchingProductFetcher: MatchingProductFetcher) {

    @GetMapping("/")
    private fun home(): String {
        return "searcher.html"
    }

    @GetMapping("/search")
    private fun search(
        @RequestParam key: String,
        page: Model
    ): String {
        val matchingProducts = matchingProductFetcher.searchProductBy(key)

        page.addAttribute("matchingProducts", matchingProducts)

        return "matchingproductfetcher.html"
    }
}