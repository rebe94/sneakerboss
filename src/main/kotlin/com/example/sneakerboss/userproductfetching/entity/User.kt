package com.example.sneakerboss.userproductfetching.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("users")
data class User private constructor(
    @Id
    val id: UUID,
    val email: String
) {
    companion object {
        fun create(email: String) = User(UUID.randomUUID(), email)
    }
}