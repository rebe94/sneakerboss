package com.example.sneakerboss

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import com.example.sneakerboss.matchingproductfetching.MatchingProductFetcher
import com.example.sneakerboss.productfetching.ProductFetcher
import java.util.*

@SpringBootApplication
class SneakerbossApplication

fun main(args: Array<String>) { runApplication<SneakerbossApplication>(*args) }