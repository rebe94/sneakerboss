package com.example.sneakerboss.productdetailsfetching

import com.example.sneakerboss.productdetailsfetching.dto.ProductDetailsDto
import com.example.sneakerboss.productdetailsfetching.dto.ProductDetailsParser
import com.example.sneakerboss.commons.productfetching.productmarkerdatafetching.ProductMarketDataFetcher
import com.example.sneakerboss.userproductfetching.dto.UserSettingDto
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProductDetailsFetcher(
    private val productMarketDataFetcher: ProductMarketDataFetcher,
    private val productDetailsParser: ProductDetailsParser
) {

    fun findProductBy(uuid: UUID, userSettingDto: UserSettingDto): ProductDetailsDto? {
        val foundProductJson = productMarketDataFetcher.findProductMarketDataBy(uuid, userSettingDto.currencyCode, userSettingDto.region) ?: return null
        return productDetailsParser.parse(foundProductJson, userSettingDto)
    }
}