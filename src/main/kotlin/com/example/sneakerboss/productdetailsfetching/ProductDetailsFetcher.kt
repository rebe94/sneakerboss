package com.example.sneakerboss.productdetailsfetching

import com.example.sneakerboss.productdetailsfetching.dto.ProductDetailsDto
import com.example.sneakerboss.productdetailsfetching.dto.ProductDetailsParser
import com.example.sneakerboss.commons.productfetching.ProductFetcher
import com.example.sneakerboss.userproductfetching.dto.UserSettingDto
import org.json.JSONObject
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProductDetailsFetcher(
    private val productFetcher: ProductFetcher,
    private val productDetailsParser: ProductDetailsParser
) {

    fun findProductBy(uuid: UUID, userSettingDto: UserSettingDto): ProductDetailsDto? {
        val foundProduct = productFetcher.findProductBy(uuid, userSettingDto.currencyCode, userSettingDto.region) ?: return null
        return parseToProductDetails(foundProduct, userSettingDto)
    }

    private fun parseToProductDetails(jsonObject: JSONObject, userSettingDto: UserSettingDto): ProductDetailsDto {
        val parentId = jsonObject.optString("parentId")
        return when (parentId) {
            "", null -> productDetailsParser.parseToParentProductDetails(jsonObject, userSettingDto)
            else -> productDetailsParser.parseToChildrenProductDetails(jsonObject, userSettingDto)
        }
    }
}