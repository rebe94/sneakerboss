package com.example.sneakerboss.usersettings

import com.example.sneakerboss.commons.productfetching.currencyconverting.components.CurrencyCode
import com.example.sneakerboss.commons.productfetching.currencyconverting.components.Region
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.server.ResponseStatusException
import java.util.*
import javax.servlet.http.HttpServletRequest

@Controller
class UserSettingController(private val userSettingService: UserSettingService) {

    @GetMapping("/user/settings")
    private fun getUserSetting(
        @AuthenticationPrincipal oauth2User: OAuth2User?,
        page: Model
    ): String {
        val userSetting = userSettingService.getUserSettings(oauth2User?.attributes?.getValue("email").toString())!!
        page.addAllAttributes(
            mapOf(
                "userSetting" to userSetting,
                "sellerLevels" to SellerLevel.values(),
                "currencyCodes" to CurrencyCode.values(),
                "regions" to Region.values()
            )
        )
        return "usersetting.html"
    }

    @PostMapping("/user/settings/save")
    private fun changeUserSetting(
        @AuthenticationPrincipal oauth2User: OAuth2User?,
        @Validated @ModelAttribute("userSetting") userSetting: UserSettingModel,
        request: HttpServletRequest
    ): String {
        val userEmail = oauth2User?.attributes?.getValue("email")!!.toString()
        val changedUserSettings = userSettingService.changeUserSettings(userEmail, userSetting)
        changedUserSettings ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User settings not found")

        val referer = request.getHeader("referer")
        return "redirect:$referer"
    }
}