package com.example.sneakerboss.usersettings.repository

import com.example.sneakerboss.usersettings.entity.UserSetting
import java.util.UUID
import org.springframework.data.mongodb.repository.MongoRepository

interface UserSettingRepository : MongoRepository<UserSetting, UUID> {

    fun findByUserId(userId: UUID): UserSetting?
}