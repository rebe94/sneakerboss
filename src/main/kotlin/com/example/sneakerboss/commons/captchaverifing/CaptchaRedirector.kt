package com.example.sneakerboss.commons.captchaverifing

import org.springframework.stereotype.Component
import org.springframework.ui.Model

@Component
class CaptchaRedirector {

    companion object {
        private const val REDIRECT_URL = "https://stockx.com/"
    }

    fun getHtmlWithCaptchaContent(page: Model, rawHtmlContent: String?): String {
        rawHtmlContent ?: "redirect:$REDIRECT_URL"
        val captchaContent = rawHtmlContent?.substringAfter(":")?.replace("<EOL>", "")
        page.addAttribute("captchaContent", captchaContent)
        return "resolvecaptcha.html"
    }
}