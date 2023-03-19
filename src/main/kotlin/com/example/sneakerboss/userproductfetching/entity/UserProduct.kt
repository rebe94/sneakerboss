package com.example.sneakerboss.userproductfetching.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("user_products")
data class UserProduct(
    @Id
    val id: UUID,
    val userId: UUID,
    val productUuid: UUID
) {
    companion object {
        fun create(productUuid: UUID, userId: UUID) = UserProduct(UUID.randomUUID(), userId, productUuid)
    }
}