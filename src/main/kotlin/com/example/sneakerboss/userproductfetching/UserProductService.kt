package com.example.sneakerboss.userproductfetching

import com.example.sneakerboss.commons.productfetching.productmarkerdatafetching.ProductMarketDataFetcher
import com.example.sneakerboss.userproductfetching.dto.UserProductDto
import com.example.sneakerboss.usersettings.UserSettingService
import com.example.sneakerboss.userproductfetching.dto.UserProductParser
import com.example.sneakerboss.userproductfetching.entity.UserProduct
import com.example.sneakerboss.userproductfetching.repository.UserProductRepository
import com.example.sneakerboss.uservalidating.UserValidator
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserProductService(
    private val userProductRepository: UserProductRepository,
    private val productMarketDataFetcher: ProductMarketDataFetcher,
    private val userProductParser: UserProductParser,
    private val userSettingService: UserSettingService,
    private val userValidator: UserValidator
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(this::class.java)
    }

    fun getUserProducts(userEmail: String): List<UserProductDto> {
        val user = userValidator.validUser(userEmail)
        val userSettingDto = userSettingService.getUserSettings(user.email)!!

        val userProductDtos = mutableListOf<UserProductDto>()

        userProductRepository.findAllByUserId(user.id).forEach {
            val productMarketDataJson = productMarketDataFetcher.findProductMarketDataBy(
                it.parentProductUuid,
                userSettingDto.currencyCode,
                userSettingDto.region
            ) ?: JSONObject()
            val userProductDto =
                userProductParser.parseToUserProductDto(
                    productMarketDataJson,
                    it.id,
                    it.shoeVariantUuid,
                    userSettingDto
                )
            userProductDtos.add(userProductDto)
        }
        return userProductDtos.toList()
    }

    fun addUserProduct(parentProductUuid: UUID, shoeVariantUuid: UUID, userEmail: String) {
        val user = userValidator.validUser(userEmail)
        userProductRepository.save(UserProduct.create(parentProductUuid, shoeVariantUuid, user.id))
    }

    fun deleteUserProduct(userProductId: UUID) {
        userProductRepository.deleteById(userProductId)
    }
}