package com.example.sneakerboss.matchingproductfetching.dto

import org.json.JSONArray
import org.json.JSONObject
import org.springframework.stereotype.Component
import java.net.URL
import java.util.*

@Component
class MatchingProductParser() {

    companion object {
        private const val LIMIT_FOUND_PRODUCTS = 10
    }

    fun parseToMatchingProducts(matchingProducts: JSONArray): List<MatchingProductDto> {
        val matchingProductsListDto = mutableListOf<MatchingProductDto>()
        val limit =
            if (matchingProducts.length() < LIMIT_FOUND_PRODUCTS) matchingProducts.length() else LIMIT_FOUND_PRODUCTS
        for (i in 0 until limit) {
            val it: JSONObject = matchingProducts.optJSONObject(i)
            val media = it.optJSONObject("media")
            matchingProductsListDto.add(
                MatchingProductDto(
                    uuid = UUID.fromString(it.optString("uuid")),
                    title = it.optString("title"),
                    brand = it.optString("brand"),
                    category = it.optString("category"),
                    colorway = it.optString("colorway"),
                    styleId = it.optString("styleId"),
                    gender = it.optString("gender"),
                    releaseDate = it.optString("releaseDate"),
                    retailPrice = it.optInt("retailPrice"),
                    imageUrl = URL(media.optString("smallImageUrl"))
                )
            )
        }
        return matchingProductsListDto.toList()
    }
}