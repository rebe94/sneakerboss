package com.example.sneakerboss.matchingproductfetching

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class MatchingProductFetcherConfig {

    @Bean
    fun restTemplate(): RestTemplate {
        return RestTemplate()
    }
}