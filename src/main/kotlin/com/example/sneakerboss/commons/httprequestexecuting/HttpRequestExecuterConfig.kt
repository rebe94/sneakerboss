package com.example.sneakerboss.commons.httprequestexecuting

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class HttpRequestExecuterConfig {

    @Bean
    fun restTemplate(): RestTemplate = RestTemplate()
}