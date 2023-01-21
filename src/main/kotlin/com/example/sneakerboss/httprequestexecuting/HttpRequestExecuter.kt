package com.example.sneakerboss.httprequestexecuting

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Component
class HttpRequestExecuter(private val restTemplate: RestTemplate) {

    fun executeHttpGetRequest(uri: String, headers: HttpHeaders): ResponseEntity<String> {
        val response: ResponseEntity<String> =
            restTemplate.exchange(
                uri,
                HttpMethod.GET,
                HttpEntity<HttpHeaders>(headers),
                String()
            )
        return response
    }
}