package com.example.sneakerboss.userproductfetching.dto

import com.example.sneakerboss.usersettings.entity.UserSetting
import org.springframework.stereotype.Component

@Component
class UserSettingParser {

    fun parseToUserSettingDto(userSettingDto: UserSetting) = UserSettingDto(
        userSettingId = userSettingDto.userId,
        userId = userSettingDto.userId,
        sellerLevel = userSettingDto.sellerLevel,
        currencyCode = userSettingDto.currencyCode,
        region = userSettingDto.region
    )
}