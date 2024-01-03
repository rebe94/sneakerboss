package com.example.sneakerboss.commons.httprequestexecuting

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Component
class HttpRequestExecuter(private val restTemplate: RestTemplate) {

    fun executeGetRequest(uri: String, headers: HttpHeaders): ResponseEntity<String> = restTemplate.exchange(
        uri,
        HttpMethod.GET,
        HttpEntity<HttpHeaders>(headers),
        String()
    )

    fun executePostRequest(uri: String, headers: HttpHeaders, requestBody: String): ResponseEntity<String> =
        restTemplate.exchange(
            uri,
            HttpMethod.POST,
            HttpEntity(requestBody, headers),
            String()
        )
}