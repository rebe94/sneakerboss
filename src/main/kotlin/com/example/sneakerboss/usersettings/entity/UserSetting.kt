package com.example.sneakerboss.usersettings.entity

import com.example.sneakerboss.commons.productfetching.currencyconverting.components.CurrencyCode
import com.example.sneakerboss.commons.productfetching.currencyconverting.components.Region
import com.example.sneakerboss.usersettings.SellerLevel
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("user_settings")
data class UserSetting(
    @Id
    val id: UUID,
    val userId: UUID,
    val sellerLevel: SellerLevel,
    val currencyCode: CurrencyCode,
    val region: Region
) {
    companion object {
        fun create(userId: UUID) =
            UserSetting(UUID.randomUUID(), userId, SellerLevel.LEVEL_1, CurrencyCode.EUR, Region.PL)
    }
}