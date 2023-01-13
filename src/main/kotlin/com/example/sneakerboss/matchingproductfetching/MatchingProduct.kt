package com.example.sneakerboss.matchingproductfetching

import java.net.URL
import java.util.*

data class MatchingProduct(
    val uuid: UUID,
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