package com.example.sneakerboss.userproductfetching

import com.example.sneakerboss.commons.productfetching.ProductFetcher
import com.example.sneakerboss.userproductfetching.dto.UserProductDto
import com.example.sneakerboss.userproductfetching.dto.UserProductParser
import com.example.sneakerboss.userproductfetching.entity.User
import com.example.sneakerboss.userproductfetching.entity.UserProduct
import com.example.sneakerboss.userproductfetching.repository.UserProductRepository
import com.example.sneakerboss.userproductfetching.repository.UserRepository
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserProductService(
    private val userRepository: UserRepository,
    private val userProductRepository: UserProductRepository,
    private val productFetcher: ProductFetcher,
    private val userProductParser: UserProductParser
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(UserProductService::class.java)
    }

    fun getUserProducts(userEmail: String): List<UserProductDto> {
        val user = validUser(userEmail)
        val userProductUuids = userProductRepository.findAllByUserId(user.id)
        val userProductDtos = mutableListOf<UserProductDto>()
        userProductUuids.forEach {
            val jsonProduct = productFetcher.findProductBy(it.productUuid) ?: JSONObject()
            val userProductDto = userProductParser.parseToUserProductDto(jsonProduct, it.id)
            userProductDtos.add(userProductDto)
        }
        return userProductDtos.toList()
    }

    fun addUserProduct(productUuid: UUID, userEmail: String) {
        val user = validUser(userEmail)
        userProductRepository.save(UserProduct.create(productUuid, user.id))
    }

    fun deleteUserProduct(userProductId: UUID) {
        userProductRepository.deleteById(userProductId)
    }

    private fun validUser(userEmail: String): User {
        val user = userRepository.findByEmail(userEmail)
        return user ?: userRepository.save(User.create(userEmail))
    }
}