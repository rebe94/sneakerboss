package com.example.sneakerboss.usersettings

import com.example.sneakerboss.commons.productfetching.currencyconverting.components.CurrencyCode
import com.example.sneakerboss.commons.productfetching.currencyconverting.components.Region
import com.example.sneakerboss.userproductfetching.dto.UserSettingDto
import com.example.sneakerboss.userproductfetching.dto.UserSettingParser
import com.example.sneakerboss.usersettings.entity.UserSetting
import com.example.sneakerboss.usersettings.repository.UserSettingRepository
import com.example.sneakerboss.uservalidating.UserValidator
import org.springframework.stereotype.Service
import java.lang.Exception

@Service
class UserSettingService(
    private val userSettingRepository: UserSettingRepository,
    private val userSettingParser: UserSettingParser,
    private val userValidator: UserValidator
) {

    fun getUserSettings(userEmail: String): UserSettingDto? {
        val user = userValidator.validUser(userEmail)
        val userSetting = userSettingRepository.findByUserId(user.id)
        userSetting ?: return null
        return userSettingParser.parseToUserSettingDto(userSetting)
    }

    fun changeUserSettings(userEmail: String, newUserSettings: UserSettingModel): UserSetting? {
        val user = userValidator.validUser(userEmail)
        val userSetting = userSettingRepository.findByUserId(user.id)
        userSetting ?: throw Exception("User settings does not exist for user email ${user.email}")

        val newSellerLevel = SellerLevel.valueOf(newUserSettings.sellerLevel)
        val newRegion = Region.valueOf(newUserSettings.region)
        val newCurrencyCode = CurrencyCode.valueOf(newUserSettings.currencyCode)
        return userSettingRepository.save(UserSetting(userSetting.id, userSetting.userId, newSellerLevel, newCurrencyCode, newRegion))
    }
}