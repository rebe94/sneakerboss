package com.example.sneakerboss.productdetailsfetching.dto

import java.util.*

data class ProductDetailsDto(
    val uuid: UUID,
    val title: String,
    val numberOfAsks: Int,
    val numberOfBids: Int,
    val deadstockSold: Int,
    val shoeVariants: List<ShoeVariant>
)

data class ShoeVariant(
    val uuid: UUID,
    val size: String,
    val lowestAsk: Int,
    val highestBid: Int,
    val numberOfAsks: Int,
    val numberOfBids: Int,
    val askToBeFirst: Int?,
    val totalPayout: Float,
    val totalPayoutPln: Float?,
    val salesLast72Hours: Int,
    val deadstockSold: Int
)