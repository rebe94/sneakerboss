package com.example.sneakerboss.userproductfetching.repository

import com.example.sneakerboss.userproductfetching.entity.User
import com.example.sneakerboss.usersettings.entity.UserSetting
import com.example.sneakerboss.usersettings.repository.UserSettingRepository
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*
import java.util.function.Function
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.query.FluentQuery

interface UserRepository: MongoRepository<User, UUID> {
    fun findByEmail(email: String): User?
}

class InMemoryUserRepository : UserRepository {

    private val entities = mutableListOf<User>()

    override fun findByEmail(email: String): User? = entities.find { it.email == email }

    override fun <S : User> save(entity: S): S {
        val existingEntity = entities.find { it.id == entity.id }
        if (existingEntity != null) entities.remove(existingEntity)
        entities.add(entity)
        return entity
    }

    override fun <S : User> saveAll(entities: MutableIterable<S>): MutableList<S> {
        TODO("Not yet implemented")
    }

    override fun findById(id: UUID): Optional<User> {
        TODO("Not yet implemented")
    }

    override fun existsById(id: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableList<User> {
        TODO("Not yet implemented")
    }

    override fun findAll(sort: Sort): MutableList<User> {
        TODO("Not yet implemented")
    }

    override fun <S : User> findAll(example: Example<S>): MutableList<S> {
        TODO("Not yet implemented")
    }

    override fun <S : User> findAll(example: Example<S>, sort: Sort): MutableList<S> {
        TODO("Not yet implemented")
    }

    override fun findAll(pageable: Pageable): Page<User> {
        TODO("Not yet implemented")
    }

    override fun <S : User> findAll(example: Example<S>, pageable: Pageable): Page<S> {
        TODO("Not yet implemented")
    }

    override fun findAllById(ids: MutableIterable<UUID>): MutableIterable<User> {
        TODO("Not yet implemented")
    }

    override fun count(): Long {
        TODO("Not yet implemented")
    }

    override fun <S : User> count(example: Example<S>): Long {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: UUID) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: User) {
        TODO("Not yet implemented")
    }

    override fun deleteAllById(ids: MutableIterable<UUID>) {
        TODO("Not yet implemented")
    }

    override fun deleteAll(entities: MutableIterable<User>) {
        TODO("Not yet implemented")
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }

    override fun <S : User> findOne(example: Example<S>): Optional<S> {
        TODO("Not yet implemented")
    }

    override fun <S : User> exists(example: Example<S>): Boolean {
        TODO("Not yet implemented")
    }

    override fun <S : User, R : Any> findBy(
        example: Example<S>,
        queryFunction: Function<FluentQuery.FetchableFluentQuery<S>, R>
    ): R {
        TODO("Not yet implemented")
    }

    override fun <S : User> insert(entity: S): S {
        TODO("Not yet implemented")
    }

    override fun <S : User> insert(entities: MutableIterable<S>): MutableList<S> {
        TODO("Not yet implemented")
    }
}