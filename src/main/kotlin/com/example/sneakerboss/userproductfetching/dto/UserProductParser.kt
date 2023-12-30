package com.example.sneakerboss.userproductfetching.dto

import com.example.sneakerboss.commons.productfetching.AskToBeFirstFetcher
import com.example.sneakerboss.commons.productfetching.PriceCalculator
import com.example.sneakerboss.commons.productfetching.currencyconverting.PriceConverter
import com.example.sneakerboss.extensions.at
import com.example.sneakerboss.extensions.round
import com.example.sneakerboss.matchingproductfetching.MatchingProductFetcher
import com.example.sneakerboss.productdetailsfetching.dto.ProductDetailsParser
import org.json.JSONObject
import org.springframework.stereotype.Component
import java.net.URL
import java.util.*

@Component
class UserProductParser(
    private val priceConverter: PriceConverter,
    private val priceCalculator: PriceCalculator,
    private val askToBeFirstFetcher: AskToBeFirstFetcher,
    private val productDetailsParser: ProductDetailsParser
) {
    fun parseToUserProductDto(
        productMarketDataJson: JSONObject,
        userProductId: UUID,
        shoeVariantUuid: UUID,
        userSettingDto: UserSettingDto
    ): UserProductDto {
        val shoeVariant = productDetailsParser.getSizeVariants(productMarketDataJson, userSettingDto)
            .find { it.uuid == shoeVariantUuid }
        val lowestAsk = shoeVariant?.lowestAsk
        val askToBeFirst =
            askToBeFirstFetcher.getAskToBeFirst(shoeVariantUuid, userSettingDto.currencyCode, userSettingDto.region)
        val totalPayout =
            priceCalculator.calculatePayout(askToBeFirst.toFloat(), userSettingDto.sellerLevel.transactionFeePercentage)

        return UserProductDto(
            userProductId = userProductId,
            parentUuid = UUID.fromString(productMarketDataJson.optString("id")),
            shoeVariantUuid = shoeVariantUuid,
            shoeSize = shoeVariant?.size ?: "",
            title = productMarketDataJson.optString("title"),
            brand = productMarketDataJson.optString("brand"),
            numberOfAsks = shoeVariant?.numberOfAsks ?: -1,
            numberOfBids = shoeVariant?.numberOfBids ?: -1,
            styleId = productMarketDataJson.optString("styleId"),
            gender = productMarketDataJson.optString("gender"),
            highestBid = shoeVariant?.highestBid ?: -1,
            imageUrl = URL(productMarketDataJson.at("media").optString("imageUrl")),
            lowestAsk = lowestAsk ?: -1,
            deadstockSold = shoeVariant?.deadstockSold ?: -1,
            salesLast72Hours = shoeVariant?.salesLast72Hours ?: -1,
            askToBeFirst = askToBeFirst,
            totalPayout = totalPayout.round(2),
            totalPayoutPln = priceConverter.convertToPln(totalPayout, userSettingDto.currencyCode)?.round(2)
        )
    }
}