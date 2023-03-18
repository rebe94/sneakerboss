package com.example.sneakerboss.productdetailsfetching.dto

import com.example.sneakerboss.commons.productfetching.PriceCalculator
import com.example.sneakerboss.commons.productfetching.currencyconverting.PriceConverter
import com.example.sneakerboss.extensions.round
import com.example.sneakerboss.commons.productfetching.ProductFetcher
import org.json.JSONObject
import org.springframework.stereotype.Component
import java.util.*

@Component
class ProductDetailsParser(
    private val priceConverter: PriceConverter,
    private val priceCalculator: PriceCalculator
) {

    companion object {
        private const val PARENT_ID_KEY = "parentId"
        private const val SHOE_SIZE_KEY = "shoeSize"
        private const val CHILDREN_KEY = "children"
    }

    fun parseToParentProductDetails(jsonObject: JSONObject): ProductDetailsDto {
        val childrenObject = jsonObject.getJSONObject(CHILDREN_KEY)
        val children = parseChildrenToProductDetailsList(childrenObject)
        return createProductDetails(
            jsonObject = jsonObject,
            children = children)
    }

    fun parseToChildrenProductDetails(jsonObject: JSONObject): ProductDetailsDto {
        val parentId = jsonObject.optString(PARENT_ID_KEY)
        val shoeSize = jsonObject.optString(SHOE_SIZE_KEY)
        return createProductDetails(
            jsonObject = jsonObject,
            parentId = parentId,
            shoeSize = shoeSize
        )
    }

    private fun parseChildrenToProductDetailsList(json: JSONObject): List<ProductDetailsDto> {
        if (json.isEmpty) return emptyList()

        val childrenProductListDto = mutableListOf<ProductDetailsDto>()
        for (key in json.keys()) {
            val child = json.getJSONObject(key)
            val parentId = child.getString(PARENT_ID_KEY)
            val shoeSize = child.getString(SHOE_SIZE_KEY)
            val childrenProduct = createProductDetails(child, parentId, shoeSize)
            childrenProductListDto.add(childrenProduct)
        }
        return childrenProductListDto.toList()
    }

    private fun createProductDetails(
        jsonObject: JSONObject,
        parentId: String? = null,
        shoeSize: String? = null,
        children: List<ProductDetailsDto>? = null
    ): ProductDetailsDto {
        val market = jsonObject.getJSONObject("market")
        val lowestAsk = market.getInt("lowestAsk")
        val askToBeFirst = priceCalculator.calculateLowestAskToBeFirst(lowestAsk.toFloat())
        val totalPayout = priceCalculator.calculatePayout(askToBeFirst)
        return ProductDetailsDto(
            uuid = UUID.fromString(jsonObject.optString("uuid")),
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
            askToBeFirst = askToBeFirst.round(2),
            totalPayout = totalPayout.round(2),
            totalPayoutPln = priceConverter.convertToPln(totalPayout, ProductFetcher.CURRENCY_CODE)?.round(2)
        )
    }
}