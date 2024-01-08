package com.example.sneakerboss.usersettings.repository

import com.example.sneakerboss.usersettings.entity.UserSetting
import java.util.Optional
import java.util.UUID
import java.util.function.Function
import org.springframework.data.domain.Example
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.query.FluentQuery

interface UserSettingRepository : MongoRepository<UserSetting, UUID> {

    fun findByUserId(userId: UUID): UserSetting?
}

class InMemoryUserSettingRepository : UserSettingRepository {

    private val entities = mutableListOf<UserSetting>()

    override fun findByUserId(userId: UUID): UserSetting? = entities.find { it.userId == userId }

    override fun <S : UserSetting> save(entity: S): S {
        val existingEntity = entities.find { it.id == entity.id }
        if (existingEntity != null) entities.remove(existingEntity)
        entities.add(entity)
        return entity
    }

    override fun <S : UserSetting> saveAll(entities: MutableIterable<S>): MutableList<S> {
        TODO("Not yet implemented")
    }

    override fun findById(id: UUID): Optional<UserSetting> {
        TODO("Not yet implemented")
    }

    override fun existsById(id: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override fun findAll(): MutableList<UserSetting> {
        TODO("Not yet implemented")
    }

    override fun findAll(sort: Sort): MutableList<UserSetting> {
        TODO("Not yet implemented")
    }

    override fun <S : UserSetting> findAll(example: Example<S>): MutableList<S> {
        TODO("Not yet implemented")
    }

    override fun <S : UserSetting> findAll(example: Example<S>, sort: Sort): MutableList<S> {
        TODO("Not yet implemented")
    }

    override fun findAll(pageable: Pageable): Page<UserSetting> {
        TODO("Not yet implemented")
    }

    override fun <S : UserSetting> findAll(example: Example<S>, pageable: Pageable): Page<S> {
        TODO("Not yet implemented")
    }

    override fun findAllById(ids: MutableIterable<UUID>): MutableIterable<UserSetting> {
        TODO("Not yet implemented")
    }

    override fun count(): Long {
        TODO("Not yet implemented")
    }

    override fun <S : UserSetting> count(example: Example<S>): Long {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: UUID) {
        TODO("Not yet implemented")
    }

    override fun delete(entity: UserSetting) {
        TODO("Not yet implemented")
    }

    override fun deleteAllById(ids: MutableIterable<UUID>) {
        TODO("Not yet implemented")
    }

    override fun deleteAll(entities: MutableIterable<UserSetting>) {
        TODO("Not yet implemented")
    }

    override fun deleteAll() {
        TODO("Not yet implemented")
    }

    override fun <S : UserSetting> findOne(example: Example<S>): Optional<S> {
        TODO("Not yet implemented")
    }

    override fun <S : UserSetting> exists(example: Example<S>): Boolean {
        TODO("Not yet implemented")
    }

    override fun <S : UserSetting, R : Any> findBy(
        example: Example<S>,
        queryFunction: Function<FluentQuery.FetchableFluentQuery<S>, R>
    ): R {
        TODO("Not yet implemented")
    }

    override fun <S : UserSetting> insert(entity: S): S {
        TODO("Not yet implemented")
    }

    override fun <S : UserSetting> insert(entities: MutableIterable<S>): MutableList<S> {
        TODO("Not yet implemented")
    }
}