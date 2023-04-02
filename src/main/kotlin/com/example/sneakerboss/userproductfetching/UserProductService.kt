package com.example.sneakerboss.userproductfetching

import com.example.sneakerboss.commons.productfetching.ProductFetcher
import com.example.sneakerboss.userproductfetching.dto.UserProductDto
import com.example.sneakerboss.usersettings.UserSettingService
import com.example.sneakerboss.userproductfetching.dto.UserProductParser
import com.example.sneakerboss.userproductfetching.entity.UserProduct
import com.example.sneakerboss.userproductfetching.repository.UserProductRepository
import com.example.sneakerboss.userproductfetching.repository.UserRepository
import com.example.sneakerboss.uservalidating.UserValidator
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserProductService(
    private val userProductRepository: UserProductRepository,
    private val productFetcher: ProductFetcher,
    private val userProductParser: UserProductParser,
    private val userSettingService: UserSettingService,
    private val userValidator: UserValidator
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(UserProductService::class.java)
    }

    fun getUserProducts(userEmail: String): List<UserProductDto> {
        val user = userValidator.validUser(userEmail)
        val userProductUuids = userProductRepository.findAllByUserId(user.id)
        val userSettingDto = userSettingService.getUserSettings(user.email)!!

        val userProductDtos = mutableListOf<UserProductDto>()
        userProductUuids.forEach {
            val jsonProduct =
                productFetcher.findProductBy(it.productUuid, userSettingDto.currencyCode, userSettingDto.region)
                    ?: JSONObject()
            val userProductDto =
                userProductParser.parseToUserProductDto(
                    jsonProduct,
                    it.id,
                    userSettingDto.currencyCode,
                    userSettingDto.region,
                    userSettingDto.sellerLevel.transactionFeePercentage
                )
            userProductDtos.add(userProductDto)
        }
        return userProductDtos.toList()
    }

    fun addUserProduct(productUuid: UUID, userEmail: String) {
        val user = userValidator.validUser(userEmail)
        userProductRepository.save(UserProduct.create(productUuid, user.id))
    }

    fun deleteUserProduct(userProductId: UUID) {
        userProductRepository.deleteById(userProductId)
    }
}