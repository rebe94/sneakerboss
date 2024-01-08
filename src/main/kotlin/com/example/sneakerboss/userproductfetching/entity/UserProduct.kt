package com.example.sneakerboss.userproductfetching.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("user_products")
data class UserProduct(
    @Id
    val id: UUID,
    val userId: UUID,
    val parentProductUuid: UUID,
    val shoeVariantUuid: UUID
) {
    companion object {
        fun create(parentProductUuid: UUID, shoeVariantUuid: UUID, userId: UUID) =
            UserProduct(
                id = UUID.randomUUID(),
                userId = userId,
                parentProductUuid = parentProductUuid,
                shoeVariantUuid = shoeVariantUuid
            )
    }
}