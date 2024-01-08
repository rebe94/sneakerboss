package com.example.sneakerboss.uservalidating

import com.example.sneakerboss.userproductfetching.entity.User
import com.example.sneakerboss.userproductfetching.repository.UserRepository
import com.example.sneakerboss.usersettings.entity.UserSetting
import com.example.sneakerboss.usersettings.repository.UserSettingRepository
import org.springframework.stereotype.Service

@Service
class UserValidator(
    private val userRepository: UserRepository,
    private val userSettingRepository: UserSettingRepository,
    private val emailFormatValidator: EmailFormatValidator
) {

    fun validUser(userEmail: String): User {
        emailFormatValidator.validEmailFormat(userEmail)
        val user = userRepository.findByEmail(userEmail)
        val validUser = user ?: userRepository.save(User.create(userEmail))
        val userSetting = userSettingRepository.findByUserId(validUser.id)
        userSetting ?: userSettingRepository.save(UserSetting.create(validUser.id))
        return validUser
    }
}