package com.example.sneakerboss.userproductfetching.repository

import com.example.sneakerboss.userproductfetching.entity.UserProduct
import java.util.Optional
import java.util.UUID
import java.util.function.Function
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.query.FluentQuery

interface UserProductRepository: MongoRepository<UserProduct, UUID> {

    fun findAllByUserId(userId: UUID): List<UserProduct>
}

class InMemoryUserProductRepository : UserProductRepository {

    private val entities = mutableListOf<UserProduct>()

    override fun findAllByUserId(userId: UUID): List<UserProduct> = entities.filter { it.userId == userId }

    override fun <S : UserProduct> save(entity: S): S {
        val existingEntity = entities.find { it.id == entity.id }
        if (existingEntity != null) entities.remove(existingEntity)
        entities.add(entity)
        return entity
    }

    override fun deleteById(id: UUID) {
        entities.removeIf { it.id == id }
    }

    override fun <S : UserProduct> saveAll(entities: MutableIterable<S>): MutableList<S> {
        TODO("Not yet implemented")
    }

    override fun findById(id: UUID): Optional<UserProduct> {
        TODO("Not yet implemented")
    }

    override fun existsById(id: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableList<UserProduct> {
        TODO("Not yet implemented")
    }

    override fun findAll(sort: Sort): MutableList<UserProduct> {
        TODO("Not yet implemented")
    }

    override fun <S : UserProduct> findAll(example: Example<S>): MutableList<S> {
        TODO("Not yet implemented")
    }

    override fun <S : UserProduct> findAll(example: Example<S>, sort: Sort): MutableList<S> {
        TODO("Not yet implemented")
    }

    override fun findAll(pageable: Pageable): Page<UserProduct> {
        TODO("Not yet implemented")
    }

    override fun <S : UserProduct> findAll(example: Example<S>, pageable: Pageable): Page<S> {
        TODO("Not yet implemented")
    }

    override fun findAllById(ids: MutableIterable<UUID>): MutableIterable<UserProduct> {
        TODO("Not yet implemented")
    }

    override fun count(): Long {
        TODO("Not yet implemented")
    }

    override fun <S : UserProduct> count(example: Example<S>): Long {
        TODO("Not yet implemented")
    }

    override fun delete(entity: UserProduct) {
        TODO("Not yet implemented")
    }

    override fun deleteAllById(ids: MutableIterable<UUID>) {
        TODO("Not yet implemented")
    }

    override fun deleteAll(entities: MutableIterable<UserProduct>) {
        TODO("Not yet implemented")
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }

    override fun <S : UserProduct> findOne(example: Example<S>): Optional<S> {
        TODO("Not yet implemented")
    }

    override fun <S : UserProduct> exists(example: Example<S>): Boolean {
        TODO("Not yet implemented")
    }

    override fun <S : UserProduct, R : Any> findBy(
        example: Example<S>,
        queryFunction: Function<FluentQuery.FetchableFluentQuery<S>, R>
    ): R {
        TODO("Not yet implemented")
    }

    override fun <S : UserProduct> insert(entity: S): S {
        TODO("Not yet implemented")
    }

    override fun <S : UserProduct> insert(entities: MutableIterable<S>): MutableList<S> {
        TODO("Not yet implemented")
    }
}