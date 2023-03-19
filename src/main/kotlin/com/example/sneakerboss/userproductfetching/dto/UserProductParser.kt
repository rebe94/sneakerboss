package com.example.sneakerboss.userproductfetching.dto

import com.example.sneakerboss.commons.productfetching.PriceCalculator
import com.example.sneakerboss.commons.productfetching.currencyconverting.PriceConverter
import com.example.sneakerboss.extensions.round
import com.example.sneakerboss.commons.productfetching.ProductFetcher
import org.json.JSONObject
import org.springframework.stereotype.Component
import java.net.URL
import java.util.*

@Component
class UserProductParser(
    private val priceConverter: PriceConverter,
    private val priceCalculator: PriceCalculator
) {
    fun parseToUserProductDto(jsonObject: JSONObject, userProductId: UUID): UserProductDto = createUserProductDto(jsonObject, userProductId)

    private fun createUserProductDto(
        jsonObject: JSONObject,
        userProductId: UUID
    ): UserProductDto {
        val media = jsonObject.getJSONObject("media")
        val market = jsonObject.getJSONObject("market")
        val lowestAsk = market.getInt("lowestAsk")
        val askToBeFirst = priceCalculator.calculateLowestAskToBeFirst(lowestAsk.toFloat())
        val totalPayout = priceCalculator.calculatePayout(askToBeFirst)
        return UserProductDto(
            userProductId = userProductId,
            uuid = UUID.fromString(jsonObject.optString("uuid")),
            title = jsonObject.optString("title"),
            brand = jsonObject.optString("brand"),
            colorway = jsonObject.optString("colorway"),
            styleId = jsonObject.optString("styleId"),
            gender = jsonObject.optString("gender"),
            releaseDate = jsonObject.optString("releaseDate"),
            retailPrice = jsonObject.optInt("retailPrice"),
            imageUrl = URL(media.optString("smallImageUrl")),
            lowestAsk = lowestAsk,
            numberOfAsks = market.optInt("numberOfAsks"),
            highestBid = market.optInt("highestBid"),
            numberOfBids = market.optInt("numberOfBids"),
            deadstockSold = market.optInt("deadstockSold"),
            salesLast72Hours = market.optInt("salesLast72Hours"),
            parentId = jsonObject.optString("parentId"),
            shoeSize = jsonObject.optString("shoeSize"),
            askToBeFirst = askToBeFirst.round(2),
            totalPayout = totalPayout.round(2),
            totalPayoutPln = priceConverter.convertToPln(totalPayout, ProductFetcher.CURRENCY_CODE)?.round(2)
        )
    }
}