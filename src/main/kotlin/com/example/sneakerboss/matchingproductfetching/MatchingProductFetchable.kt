package com.example.sneakerboss.matchingproductfetching

interface MatchingProductFetchable {
    fun searchProductBy(key: String): List<MatchingProduct>
}