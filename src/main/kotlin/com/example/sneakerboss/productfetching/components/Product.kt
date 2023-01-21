package com.example.sneakerboss.productfetching.components

import java.net.URL
import java.util.*

data class Product(
    val uuid: UUID,
    val title: String,
    val brand: String,
    val colorway: String,
    val styleId: String,
    val gender: String,
    val releaseDate: String,
    val retailPrice: Int,
    val imageUrl: URL,
    val lowestAsk: Int,
    val numberOfAsks: Int,
    val highestBid: Int,
    val numberOfBids: Int,
    val deadstockSold: Int,
    val averageDeadstockPrice: Int,
    val totalDollars: Int,
    val parentId: String?,
    val shoeSize: String?,
    val children: List<Product>?,
    val askToBeFirst: Float,
    val totalPayout: Float,
    val totalPayoutPln: Float?
)