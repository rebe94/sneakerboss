package com.example.sneakerboss.matchingproductfetching.components

interface MatchingProductFetchable {
    fun searchProductBy(key: String): List<MatchingProduct>
}