package com.example.sneakerboss.commons.productfetching.productmarkerdatafetching

import java.util.UUID

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