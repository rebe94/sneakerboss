package com.example.sneakerboss.usersettings

import com.example.sneakerboss.commons.productfetching.currencyconverting.components.CurrencyCode
import com.example.sneakerboss.commons.productfetching.currencyconverting.components.Region
import com.example.sneakerboss.userproductfetching.dto.UserSettingParser
import com.example.sneakerboss.userproductfetching.entity.User
import com.example.sneakerboss.userproductfetching.repository.InMemoryUserRepository
import com.example.sneakerboss.userproductfetching.repository.UserRepository
import com.example.sneakerboss.usersettings.entity.UserSetting
import com.example.sneakerboss.usersettings.repository.InMemoryUserSettingRepository
import com.example.sneakerboss.usersettings.repository.UserSettingRepository
import com.example.sneakerboss.uservalidating.EmailFormatValidator
import com.example.sneakerboss.uservalidating.UserValidator
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserSettingServiceCompTest {

    private lateinit var userSettingService: UserSettingService
    private lateinit var userRepository: UserRepository
    private lateinit var userSettingRepository: UserSettingRepository
    private lateinit var userValidator: UserValidator
    private lateinit var emailFormatValidator: EmailFormatValidator

    private val EXPECTED_NEW_USER_SETTING =
        UserSettingModel(
            sellerLevel = SellerLevel.LEVEL_5.toString(),
            region = Region.NL.toString(),
            currencyCode = CurrencyCode.GBP.toString()
        )

    @BeforeEach
    fun setup() {
        userRepository = InMemoryUserRepository()
        userSettingRepository = InMemoryUserSettingRepository()
        emailFormatValidator = EmailFormatValidator()
        userValidator = UserValidator(userRepository, userSettingRepository, emailFormatValidator)
        userSettingService = UserSettingService(userSettingRepository, UserSettingParser(), userValidator)
    }

    @Test
    fun `changes existing user settings to new ones`() {
        //given
        val existingUser = givenExistingUser()

        //when
        userSettingService.changeUserSettings(existingUser.email, EXPECTED_NEW_USER_SETTING)
        val actualUserSetting = userSettingRepository.findByUserId(existingUser.id)

        //then
        assertThatUserSettingsEqual(actualUserSetting, EXPECTED_NEW_USER_SETTING)
    }

    @Test
    fun `creates new user setting and change default with new values when given non existing email`() {
        //given
        val nonExistingUserEmail = "nonExistingUserEmail@email.com"

        //when
        userSettingService.changeUserSettings(nonExistingUserEmail, EXPECTED_NEW_USER_SETTING)
        val newlyCreatedUser = userRepository.findByEmail(nonExistingUserEmail)!!
        val actualUserSetting = userSettingRepository.findByUserId(newlyCreatedUser.id)

        //then
        assertThatUserSettingsEqual(actualUserSetting, EXPECTED_NEW_USER_SETTING)
    }

    private fun givenExistingUser(): User {
        val existingUserEmail = "existingUserEmail@email.com"
        val existingUser = User.create(existingUserEmail)
        userRepository.save(existingUser)
        val existingUserSetting =
            UserSetting(
                id = UUID.randomUUID(),
                userId = existingUser.id,
                sellerLevel = SellerLevel.LEVEL_1,
                currencyCode = CurrencyCode.EUR,
                region = Region.PL
            )
        userSettingRepository.save(existingUserSetting)

        return existingUser
    }

    private fun assertThatUserSettingsEqual(actualUserSetting: UserSetting?, expectedUserSetting: UserSettingModel) {
        assertThat(actualUserSetting?.sellerLevel.toString()).isEqualTo(expectedUserSetting.sellerLevel)
        assertThat(actualUserSetting?.region.toString()).isEqualTo(expectedUserSetting.region)
        assertThat(actualUserSetting?.currencyCode.toString()).isEqualTo(expectedUserSetting.currencyCode)
    }
}