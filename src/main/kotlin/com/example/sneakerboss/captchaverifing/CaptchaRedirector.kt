package com.example.sneakerboss.captchaverifing

import org.springframework.stereotype.Component

@Component
class CaptchaRedirector {

    companion object {
        private const val REDIRECT_URL = "https://stockx.com"
    }

    fun getCaptchaRedirectingUrl(): String {
        return "redirect:$REDIRECT_URL"
    }
}