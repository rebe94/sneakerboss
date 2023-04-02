package com.example.sneakerboss.userproductfetching.dto

import com.example.sneakerboss.commons.productfetching.AskToBeFirstFetcher
import com.example.sneakerboss.commons.productfetching.PriceCalculator
import com.example.sneakerboss.commons.productfetching.currencyconverting.PriceConverter
import com.example.sneakerboss.commons.productfetching.currencyconverting.components.CurrencyCode
import com.example.sneakerboss.commons.productfetching.currencyconverting.components.Region
import com.example.sneakerboss.extensions.round
import org.json.JSONObject
import org.springframework.stereotype.Component
import java.net.URL
import java.util.*

@Component
class UserProductParser(
    private val priceConverter: PriceConverter,
    private val priceCalculator: PriceCalculator,
    private val askToBeFirstFetcher: AskToBeFirstFetcher
) {
    fun parseToUserProductDto(
        jsonObject: JSONObject,
        userProductId: UUID,
        currencyCode: CurrencyCode,
        region: Region,
        transactionFeePercentage: Float
    ): UserProductDto {
        val productUuid = UUID.fromString(jsonObject.optString("uuid"))
        val media = jsonObject.getJSONObject("media")
        val market = jsonObject.getJSONObject("market")
        val lowestAsk = market.getInt("lowestAsk")
        val askToBeFirst = askToBeFirstFetcher.getAskToBeFirst(productUuid, currencyCode, region)
        val totalPayout = priceCalculator.calculatePayout(askToBeFirst.toFloat(), transactionFeePercentage)
        return UserProductDto(
            userProductId = userProductId,
            uuid = productUuid,
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
            askToBeFirst = askToBeFirst,
            totalPayout = totalPayout.round(2),
            totalPayoutPln = priceConverter.convertToPln(totalPayout, currencyCode)?.round(2)
        )
    }
}