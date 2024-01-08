package com.example.sneakerboss.uservalidating

import com.example.sneakerboss.exceptions.InvalidEmailFormat
import com.example.sneakerboss.userproductfetching.entity.User
import com.example.sneakerboss.userproductfetching.repository.InMemoryUserRepository
import com.example.sneakerboss.userproductfetching.repository.UserRepository
import com.example.sneakerboss.usersettings.entity.UserSetting
import com.example.sneakerboss.usersettings.repository.InMemoryUserSettingRepository
import com.example.sneakerboss.usersettings.repository.UserSettingRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserValidatorUnitTest {

    private lateinit var userValidator: UserValidator
    private lateinit var userRepository: UserRepository
    private lateinit var userSettingRepository: UserSettingRepository


    @BeforeEach
    fun setup() {
        userRepository = InMemoryUserRepository()
        userSettingRepository = InMemoryUserSettingRepository()
        userValidator = UserValidator(userRepository, userSettingRepository, EmailFormatValidator())
    }

    @Test
    fun `valid user and add it to user and user settings repositories because of first time`() {
        //given
        val newUserEmail = "newUserEmail@email.com"

        //when
        userValidator.validUser(newUserEmail)
        val actualUser = userRepository.findByEmail(newUserEmail)
        val actualUserSetting = actualUser?.let { userSettingRepository.findByUserId(it.id) }

        //then
        assertThat(actualUser).isNotNull()
        assertThat(actualUser?.email).isEqualTo(newUserEmail)
        assertThat(actualUserSetting).isNotNull()
        assertThat(actualUserSetting?.userId).isEqualTo(actualUser?.id)
    }

    @Test
    fun `valid existing user and user settings`() {
        //given
        val existingUserEmail = "exisitngUserEmail@email.com"
        val existingUser = User.create(existingUserEmail)
        userRepository.save(existingUser)
        userSettingRepository.save(UserSetting.create(existingUser.id))

        //when
        userValidator.validUser(existingUserEmail)
        val actualUser = userRepository.findByEmail(existingUserEmail)
        val actualUserSetting = actualUser?.let { userSettingRepository.findByUserId(it.id) }

        //then
        assertThat(actualUser).isNotNull()
        assertThat(actualUser?.email).isEqualTo(existingUserEmail)
        assertThat(actualUserSetting).isNotNull()
        assertThat(actualUserSetting?.userId).isEqualTo(actualUser?.id)
    }

    @Test
    fun `throws exception when given wrong email format`() {
        //given
        val wrongEmailFormat = "wrong email format"

        //when
        //then
        assertThrows<InvalidEmailFormat> { userValidator.validUser(wrongEmailFormat) }
    }
}