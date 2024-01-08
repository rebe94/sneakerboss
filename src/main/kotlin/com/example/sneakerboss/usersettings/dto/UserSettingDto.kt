package com.example.sneakerboss.userproductfetching.dto

import com.example.sneakerboss.commons.productfetching.currencyconverting.components.CurrencyCode
import com.example.sneakerboss.commons.productfetching.currencyconverting.components.Region
import com.example.sneakerboss.usersettings.SellerLevel
import java.util.*

data class UserSettingDto(
    val userSettingId: UUID,
    val userId: UUID,
    val sellerLevel: SellerLevel,
    val currencyCode: CurrencyCode,
    val region: Region
) {
    companion object {
        val DEFAULT = UserSettingDto(
            userSettingId = UUID.randomUUID(),
            userId = UUID.randomUUID(),
            sellerLevel = SellerLevel.LEVEL_1,
            currencyCode = CurrencyCode.EUR,
            region = Region.PL
        )
    }
}

