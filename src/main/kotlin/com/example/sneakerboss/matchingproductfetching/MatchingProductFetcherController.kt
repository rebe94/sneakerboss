package com.example.sneakerboss.matchingproductfetching

import com.example.sneakerboss.commons.captchaverifing.CaptchaRedirector
import com.example.sneakerboss.matchingproductfetching.dto.MatchingProductDto
import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.client.HttpClientErrorException
import java.security.Principal

@Controller
class MatchingProductFetcherController(
    private val matchingProductFetcher: MatchingProductFetcher,
    private val captchaRedirector: CaptchaRedirector
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MatchingProductFetcherController::class.java)
    }

    @GetMapping("/", "/home")
    private fun home(@AuthenticationPrincipal oauth2User: OAuth2User?, page: Model): String {
        val userName = oauth2User?.attributes?.getValue("name") ?: "Guest"
        page.addAttribute("isUserLogged", oauth2User != null)
        page.addAttribute("userName", userName)
        return "searcher.html"
    }

    @GetMapping("/search")
    private fun search(
        @RequestParam key: String,
        page: Model
    ): String {
        val matchingProductDtos: List<MatchingProductDto>
        try {
            matchingProductDtos = matchingProductFetcher.searchProductBy(key)
        } catch (ex: HttpClientErrorException) {
            LOGGER.info("User redirected to resolve captcha.")
            return captchaRedirector.getHtmlWithCaptchaContent(page, ex.message)
        }

        page.addAttribute("matchingProducts", matchingProductDtos)
        return "matchingproductfetcher.html"
    }
}