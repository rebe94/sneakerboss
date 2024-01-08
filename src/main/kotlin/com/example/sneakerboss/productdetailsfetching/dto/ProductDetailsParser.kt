package com.example.sneakerboss.productdetailsfetching.dto

import com.example.sneakerboss.commons.productfetching.productmarkerdatafetching.ShoeVariantParser
import com.example.sneakerboss.extensions.at
import com.example.sneakerboss.userproductfetching.dto.UserSettingDto
import java.util.UUID
import org.json.JSONObject
import org.springframework.stereotype.Component

@Component
class ProductDetailsParser(
    private val shoeVariantParser: ShoeVariantParser
) {

    fun parse(json: JSONObject, userSettingDto: UserSettingDto): ProductDetailsDto {
        val productUuid = UUID.fromString(json.optString("id"))
        val market = json.at("market")
        val state = market.at("state")
        return ProductDetailsDto(
            uuid = productUuid,
            title = json.getString("title"),
            numberOfAsks = state.optInt("numberOfAsks"),
            numberOfBids = state.optInt("numberOfBids"),
            deadstockSold = market.at("deadStock").optInt("sold"),
            shoeVariants = shoeVariantParser.getShoeVariants(json, userSettingDto)
        )
    }
}