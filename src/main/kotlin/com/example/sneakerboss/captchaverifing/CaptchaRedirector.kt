package com.example.sneakerboss.captchaverifing

import org.springframework.stereotype.Component

@Component
class CaptchaRedirector {

    companion object {
        private const val REDIRECT_URL = "https://stockx.com/api/products/19ee5f88-55e0-4bdd-b855-1e9b3d2ba0b3?includes=market&currency=EUR&country=PL"
    }

    fun getCaptchaRedirectingUrl(): String {
        return "redirect:$REDIRECT_URL"
    }
}