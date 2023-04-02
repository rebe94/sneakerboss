package com.example.sneakerboss.productdetailsfetching.dto

import java.util.*

data class ProductDetailsDto(
    val uuid: UUID,
    val title: String,
    val lowestAsk: Int,
    val numberOfAsks: Int,
    val highestBid: Int,
    val numberOfBids: Int,
    val deadstockSold: Int,
    val salesLast72Hours: Int,
    val parentId: String?,
    val shoeSize: String?,
    val children: List<ProductDetailsDto>?,
    val askToBeFirst: Int?,
    val totalPayout: Float,
    val totalPayoutPln: Float?
)