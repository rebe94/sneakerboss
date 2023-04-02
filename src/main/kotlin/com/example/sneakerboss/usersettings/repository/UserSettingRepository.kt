package com.example.sneakerboss.usersettings.repository

import com.example.sneakerboss.commons.productfetching.currencyconverting.components.CurrencyCode
import com.example.sneakerboss.commons.productfetching.currencyconverting.components.Region
import com.example.sneakerboss.usersettings.SellerLevel
import com.example.sneakerboss.usersettings.entity.UserSetting
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserSettingRepository : MongoRepository<UserSetting, UUID> {

    fun findByUserId(userId: UUID): UserSetting?

    /*fun updateUserSettingFieldsById(
        id: UUID,
        sellerLevel: SellerLevel,
        currencyCode: CurrencyCode,
        region: Region
    ): UserSetting?*/
}