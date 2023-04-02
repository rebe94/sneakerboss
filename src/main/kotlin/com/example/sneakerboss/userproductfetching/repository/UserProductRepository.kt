package com.example.sneakerboss.userproductfetching.repository

import com.example.sneakerboss.userproductfetching.entity.UserProduct
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserProductRepository: MongoRepository<UserProduct, UUID> {

    fun findAllByUserId(userId: UUID): List<UserProduct>
}