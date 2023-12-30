package com.example.sneakerboss.userproductfetching.dto

import java.net.URL
import java.util.*

data class UserProductDto(
    val userProductId: UUID,
    val parentUuid: UUID,
    val shoeVariantUuid: UUID,
    val title: String,
    val brand: String,
    val styleId: String,
    val gender: String,
    val imageUrl: URL,
    val lowestAsk: Int,
    val numberOfAsks: Int,
    val highestBid: Int,
    val numberOfBids: Int,
    val deadstockSold: Int,
    val salesLast72Hours: Int,
    val shoeSize: String,
    val askToBeFirst: Int?,
    val totalPayout: Float,
    val totalPayoutPln: Float?
)