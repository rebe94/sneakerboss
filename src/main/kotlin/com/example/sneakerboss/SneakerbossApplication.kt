package com.example.sneakerboss

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
class SneakerbossApplication

fun main(args: Array<String>) { runApplication<SneakerbossApplication>(*args) }