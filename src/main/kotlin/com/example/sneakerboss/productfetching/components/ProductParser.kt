package com.example.sneakerboss.productfetching.components

import com.example.sneakerboss.currencyconverting.PriceConverter
import com.example.sneakerboss.currencyconverting.components.CurrencyCode
import com.example.sneakerboss.extensions.round
import com.example.sneakerboss.pricecalculating.PriceCalculator
import com.example.sneakerboss.productfetching.ProductFetcher
import org.json.JSONException
import org.json.JSONObject
import org.springframework.stereotype.Component
import java.net.URL
import java.util.*

@Component
class ProductParser(
    private val priceConverter: PriceConverter,
    private val priceCalculator: PriceCalculator
) {

    companion object {
        private const val PARENT_ID_KEY = "parentId"
        private const val SHOE_SIZE_KEY = "shoeSize"
        private const val CHILDREN_KEY = "children"
    }

    fun parseToParentProduct(jsonObject: JSONObject): Product {
        val childrenObject = jsonObject.getJSONObject(CHILDREN_KEY)
        val children = parseChildrenToProductList(childrenObject)
        return createProduct(
            jsonObject = jsonObject,
            children = children)
    }

    fun parseToChildrenProduct(jsonObject: JSONObject): Product {
        val parentId = try {
            jsonObject.getString(PARENT_ID_KEY)
        } catch (ex: JSONException) {
            null
        }
        val shoeSize = try {
            jsonObject.getString(SHOE_SIZE_KEY)
        } catch (ex: JSONException) {
            null
        }
        return createProduct(
            jsonObject = jsonObject,
            parentId = parentId,
            shoeSize = shoeSize
        )
    }

    private fun parseChildrenToProductList(json: JSONObject): List<Product> {
        if (json.isEmpty) return emptyList()

        val childrenProductList = mutableListOf<Product>()
        for (key in json.keys()) {
            val child = json.getJSONObject(key)
            val parentId = child.getString(PARENT_ID_KEY)
            val shoeSize = child.getString(SHOE_SIZE_KEY)
            val childrenProduct = createProduct(child, parentId, shoeSize)
            childrenProductList.add(childrenProduct)
        }
        return childrenProductList.toList()
    }

    private fun createProduct(
        jsonObject: JSONObject,
        parentId: String? = null,
        shoeSize: String? = null,
        children: List<Product>? = null
    ): Product {
        val media = jsonObject.getJSONObject("media")
        val market = jsonObject.getJSONObject("market")
        val lowestAsk = market.getInt("lowestAsk")
        val askToBeFirst = priceCalculator.calculateLowestAskToBeFirst(lowestAsk.toFloat())
        val totalPayout = priceCalculator.calculatePayout(askToBeFirst)
        return Product(
            uuid = UUID.fromString(jsonObject.getString("uuid")),
            title = jsonObject.getString("title"),
            brand = jsonObject.getString("brand"),
            colorway = jsonObject.getString("colorway"),
            styleId = jsonObject.getString("styleId"),
            gender = jsonObject.getString("gender"),
            releaseDate = jsonObject.getString("releaseDate"),
            retailPrice = jsonObject.getInt("retailPrice"),
            imageUrl = URL(media.getString("smallImageUrl")),
            lowestAsk = lowestAsk,
            numberOfAsks = market.getInt("numberOfAsks"),
            highestBid = market.getInt("highestBid"),
            numberOfBids = market.getInt("numberOfBids"),
            deadstockSold = market.getInt("deadstockSold"),
            salesLast72Hours = market.getInt("salesLast72Hours"),
            averageDeadstockPrice = market.getInt("averageDeadstockPrice"),
            totalDollars = market.getInt("totalDollars"),
            parentId = parentId,
            shoeSize = shoeSize,
            children = children,
            askToBeFirst = askToBeFirst.round(2),
            totalPayout = totalPayout.round(2),
            totalPayoutPln = priceConverter.convertToPln(totalPayout, ProductFetcher.CURRENCY_CODE)?.round(2)
        )
    }
}