package com.example.sneakerboss.productdetailsfetching.dto

import com.example.sneakerboss.commons.productfetching.AskToBeFirstFetcher
import com.example.sneakerboss.commons.productfetching.PriceCalculator
import com.example.sneakerboss.commons.productfetching.currencyconverting.PriceConverter
import com.example.sneakerboss.extensions.round
import com.example.sneakerboss.userproductfetching.dto.UserSettingDto
import org.json.JSONObject
import org.springframework.stereotype.Component
import java.util.*

@Component
class ProductDetailsParser(
    private val priceConverter: PriceConverter,
    private val priceCalculator: PriceCalculator,
    private val askToBeFirstFetcher: AskToBeFirstFetcher
) {

    companion object {
        private const val PARENT_ID_KEY = "parentId"
        private const val SHOE_SIZE_KEY = "shoeSize"
        private const val CHILDREN_KEY = "children"
    }

    fun parseToParentProductDetails(jsonObject: JSONObject, userSettingDto: UserSettingDto): ProductDetailsDto {
        val childrenObject = jsonObject.getJSONObject(CHILDREN_KEY)
        val children = parseChildrenToProductDetailsList(childrenObject, userSettingDto)
        return createProductDetails(
            jsonObject = jsonObject,
            children = children,
            userSettingDto = userSettingDto
        )
    }

    fun parseToChildrenProductDetails(jsonObject: JSONObject, userSettingDto: UserSettingDto): ProductDetailsDto {
        val parentId = jsonObject.optString(PARENT_ID_KEY)
        val shoeSize = jsonObject.optString(SHOE_SIZE_KEY)
        return createProductDetails(
            jsonObject = jsonObject,
            parentId = parentId,
            shoeSize = shoeSize,
            userSettingDto = userSettingDto
        )
    }

    private fun parseChildrenToProductDetailsList(
        json: JSONObject,
        userSettingDto: UserSettingDto
    ): List<ProductDetailsDto> {
        if (json.isEmpty) return emptyList()

        val childrenProductListDto = mutableListOf<ProductDetailsDto>()
        for (key in json.keys()) {
            val child = json.getJSONObject(key)
            val parentId = child.getString(PARENT_ID_KEY)
            val shoeSize = child.getString(SHOE_SIZE_KEY)
            val childrenProduct = createProductDetails(
                jsonObject = child,
                parentId = parentId,
                shoeSize = shoeSize,
                userSettingDto = userSettingDto
            )
            childrenProductListDto.add(childrenProduct)
        }
        return childrenProductListDto.toList()
    }

    private fun createProductDetails(
        jsonObject: JSONObject,
        parentId: String? = null,
        shoeSize: String? = null,
        children: List<ProductDetailsDto>? = null,
        userSettingDto: UserSettingDto
    ): ProductDetailsDto {
        val productUuid = UUID.fromString(jsonObject.optString("uuid"))
        val market = jsonObject.getJSONObject("market")
        val lowestAsk = market.getInt("lowestAsk")
        //val askToBeFirst = priceCalculator.calculateLowestAskToBeFirst(lowestAsk.toFloat())
        val askToBeFirst = askToBeFirstFetcher.getAskToBeFirst(productUuid, userSettingDto.currencyCode, userSettingDto.region)
        val totalPayout = priceCalculator.calculatePayout(askToBeFirst.toFloat(), userSettingDto.sellerLevel.transactionFeePercentage)
        return ProductDetailsDto(
            uuid = productUuid,
            title = jsonObject.optString("title"),
            lowestAsk = lowestAsk,
            numberOfAsks = market.optInt("numberOfAsks"),
            highestBid = market.optInt("highestBid"),
            numberOfBids = market.optInt("numberOfBids"),
            deadstockSold = market.optInt("deadstockSold"),
            salesLast72Hours = market.optInt("salesLast72Hours"),
            parentId = parentId,
            shoeSize = shoeSize,
            children = children,
            askToBeFirst = askToBeFirst,
            totalPayout = totalPayout.round(2),
            totalPayoutPln = priceConverter.convertToPln(totalPayout, userSettingDto.currencyCode)?.round(2)
        )
    }
}