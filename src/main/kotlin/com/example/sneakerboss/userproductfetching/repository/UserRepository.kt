package com.example.sneakerboss.userproductfetching.repository

import com.example.sneakerboss.userproductfetching.entity.User
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserRepository: MongoRepository<User, UUID> {
    fun findByEmail(email: String): User?
}