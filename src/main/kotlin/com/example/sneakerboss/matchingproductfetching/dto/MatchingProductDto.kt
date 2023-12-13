package com.example.sneakerboss.matchingproductfetching.dto

import java.net.URL
import java.util.*

data class MatchingProductDto(
    val uuid: UUID,
    val urlKey: String,
    val title: String,
    val brand: String,
    val category: String,
    val colorway: String,
    val styleId: String,
    val gender: String,
    val releaseDate: String,
    val retailPrice: Int,
    val imageUrl: URL
)