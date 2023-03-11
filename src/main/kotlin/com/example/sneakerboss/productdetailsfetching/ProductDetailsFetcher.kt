package com.example.sneakerboss.productdetailsfetching

import com.example.sneakerboss.productdetailsfetching.dto.ProductDetailsDto
import com.example.sneakerboss.productdetailsfetching.dto.ProductDetailsParser
import com.example.sneakerboss.commons.productfetching.ProductFetcher
import org.json.JSONObject
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProductDetailsFetcher(
    private val productFetcher: ProductFetcher,
    private val productDetailsParser: ProductDetailsParser
) {

    fun findProductBy(uuid: UUID): ProductDetailsDto? {
        val foundProduct = productFetcher.findProductBy(uuid) ?: return null
        return parseToProductDetails(foundProduct)
    }

    private fun parseToProductDetails(jsonObject: JSONObject): ProductDetailsDto {
        val parentId = jsonObject.optString("parentId")
        return when (parentId) {
            "", null -> productDetailsParser.parseToParentProductDetails(jsonObject)
            else -> productDetailsParser.parseToChildrenProductDetails(jsonObject)
        }
    }
}