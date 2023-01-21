package com.example.sneakerboss

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import com.example.sneakerboss.matchingproductfetching.MatchingProductFetcher
import com.example.sneakerboss.productfetching.ProductFetcher
import java.util.*

@SpringBootApplication
class SneakerbossApplication

fun main(args: Array<String>) {
	val context = runApplication<SneakerbossApplication>(*args)
	val matchingProductFetcher = context.getBean("matchingProductFetcher") as MatchingProductFetcher
	val productFetcher = context.getBean("productFetcher") as ProductFetcher
	//val matchingProducts = matchingProductFetcher.searchProductBy("nike air jordan low")
	//val product = productFetcher.findProductBy(UUID.fromString("8d8fa98b-7777-441b-b927-917ac5d3b255"))//parent
	//val product = productFetcher.findProductBy(UUID.fromString("9ff5f937-c0d1-49f8-9af6-ebd7b4c92296"))//child
	//println(product)
	//println(matchingProducts.toString())
}