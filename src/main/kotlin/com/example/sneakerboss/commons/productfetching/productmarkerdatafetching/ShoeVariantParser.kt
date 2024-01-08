package com.example.sneakerboss.commons.productfetching.productmarkerdatafetching

import com.example.sneakerboss.commons.productfetching.PriceCalculator
import com.example.sneakerboss.commons.productfetching.currencyconverting.PriceConverter
import com.example.sneakerboss.extensions.at
import com.example.sneakerboss.extensions.round
import com.example.sneakerboss.userproductfetching.dto.UserSettingDto
import java.util.UUID
import org.json.JSONObject
import org.springframework.stereotype.Component

@Component
class ShoeVariantParser(
    private val priceConverter: PriceConverter,
    private val priceCalculator: PriceCalculator,
) {

    fun getShoeVariants(jsonObject: JSONObject, userSettingDto: UserSettingDto): List<ShoeVariant> {
        val shoeVariants = mutableListOf<ShoeVariant>()

        val jsonArray = jsonObject.getJSONArray("variants")
        (0 until jsonArray.length()).forEach {
            val variant = jsonArray.getJSONObject(it)
            val market = variant.at("market")
            val bidAskData = market.at("bidAskData")
            val size = bidAskData.optString("highestBidSize")
            if (size.isBlank()) return@forEach
            val state = market.at("state")
            val uuid = UUID.fromString(variant.getString("id"))
            val askToBeFirst = variant.at("pricingGuidance").at("sellingGuidance").optInt("earnMore")
            val totalPayout = priceCalculator.calculatePayout(
                askToBeFirst.toFloat(),
                userSettingDto.sellerLevel.transactionFeePercentage
            )

            shoeVariants.add(
                ShoeVariant(
                    uuid = uuid,
                    size = if (size.isBlank()) "-1" else size,
                    highestBid = bidAskData.optInt("highestBid"),
                    lowestAsk = bidAskData.optInt("lowestAsk"),
                    numberOfAsks = state.optInt("numberOfAsks"),
                    numberOfBids = state.optInt("numberOfBids"),
                    askToBeFirst = askToBeFirst,
                    totalPayout = totalPayout.round(2),
                    totalPayoutPln = priceConverter.convertToPln(totalPayout, userSettingDto.currencyCode)?.round(2),
                    salesLast72Hours = market.at("salesInformation").optInt("salesLast72Hours"),
                    deadstockSold = market.at("deadStock").optInt("sold"),
                )
            )
        }

        return shoeVariants.toList()
    }
}