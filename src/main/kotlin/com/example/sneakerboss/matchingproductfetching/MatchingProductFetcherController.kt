package com.example.sneakerboss.matchingproductfetching

import com.example.sneakerboss.captchaverifing.CaptchaRedirector
import com.example.sneakerboss.matchingproductfetching.components.MatchingProduct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.HttpClientErrorException

@Controller
class MatchingProductFetcherController(private val matchingProductFetcher: MatchingProductFetcher, private val captchaRedirector: CaptchaRedirector) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MatchingProductFetcherController::class.java)
    }

    @GetMapping("/")
    private fun home(): String {
        return "searcher.html"
    }

    @GetMapping("/search")
    private fun search(
        @RequestParam key: String,
        page: Model
    ): String {
        val matchingProducts:List<MatchingProduct>
        try {
            matchingProducts = matchingProductFetcher.searchProductBy(key)
        } catch (ex: HttpClientErrorException) {
            LOGGER.info("${ex.message}")
            LOGGER.info("User redirected to resolve captcha.")
            val captchaContent = ex.message?.replace("<EOL>", "")
            page.addAttribute("captchaContent", captchaContent)
            return "resolvecaptcha.html"
        }
        page.addAttribute("matchingProducts", matchingProducts)

        return "matchingproductfetcher.html"
    }
}