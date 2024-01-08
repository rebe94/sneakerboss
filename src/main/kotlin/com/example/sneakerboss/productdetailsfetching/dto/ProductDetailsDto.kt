package com.example.sneakerboss.productdetailsfetching.dto

import com.example.sneakerboss.commons.productfetching.productmarkerdatafetching.ShoeVariant
import java.util.UUID

data class ProductDetailsDto(
    val uuid: UUID,
    val title: String,
    val numberOfAsks: Int,
    val numberOfBids: Int,
    val deadstockSold: Int,
    val shoeVariants: List<ShoeVariant>
)

