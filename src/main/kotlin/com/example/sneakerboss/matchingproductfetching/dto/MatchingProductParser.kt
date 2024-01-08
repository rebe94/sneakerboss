package com.example.sneakerboss.matchingproductfetching.dto

import org.json.JSONArray
import org.json.JSONObject
import org.springframework.stereotype.Component
import java.net.URL
import java.util.*

@Component
class MatchingProductParser {

    companion object {
        const val LIMIT_FOUND_PRODUCTS = 10
    }

    fun parseToMatchingProducts(matchingProducts: JSONArray): List<MatchingProductDto> {
        val matchingProductsListDto = mutableListOf<MatchingProductDto>()
        val limit =
            if (matchingProducts.length() < LIMIT_FOUND_PRODUCTS) matchingProducts.length() else LIMIT_FOUND_PRODUCTS
        for (i in 0 until limit) {
            val it: JSONObject = matchingProducts.optJSONObject(i)
            val node = it.optJSONObject("node")
            val media = node.optJSONObject("media")
            matchingProductsListDto.add(
                MatchingProductDto(
                    uuid = UUID.fromString(it.optString("objectId")),
                    urlKey = node.optString("urlKey"),
                    title = node.optString("title"),
                    brand = node.optString("brand"),
                    category = node.optString("productCategory"),
                    styleId = node.optString("styleId"),
                    gender = node.optString("gender"),
                    imageUrl = URL(media.optString("smallImageUrl"))
                )
            )
        }
        return matchingProductsListDto.toList()
    }
}