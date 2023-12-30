package com.example.sneakerboss.matchingproductfetching

import com.example.sneakerboss.commons.httprequestexecuting.HttpRequestExecuter
import com.example.sneakerboss.extensions.at
import com.example.sneakerboss.extensions.getTextFromResource
import com.example.sneakerboss.extensions.substitute
import com.example.sneakerboss.matchingproductfetching.dto.MatchingProductDto
import com.example.sneakerboss.matchingproductfetching.dto.MatchingProductParser
import com.example.sneakerboss.matchingproductfetching.dto.MatchingProductParser.Companion.LIMIT_FOUND_PRODUCTS
import org.json.JSONObject
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service


@Service
class MatchingProductFetcher(
    private val httpRequestExecuter: HttpRequestExecuter,
    private val matchingProductParser: MatchingProductParser
) {

    companion object {
        private const val SEARCH_PRODUCT_BASE_URL = "https://stockx.com/api/p/e"
    }

    fun searchProductBy(key: String): List<MatchingProductDto> {
        val requestBody = getTextFromResource("getSearchResultsRequestBody.json").substitute(
            mapOf(
                "KEY" to key.replace(" ", "+"),
                "RESULT_LIMIT" to LIMIT_FOUND_PRODUCTS.toString()
            )
        )
        val response =
            httpRequestExecuter.executePostRequest(SEARCH_PRODUCT_BASE_URL, getHeaders(), requestBody)
        val foundMatchingProductJSONArray = JSONObject(response.body).at("data").at("browse").at("results").getJSONArray("edges")
        if (foundMatchingProductJSONArray.isEmpty) return emptyList()
        return matchingProductParser.parseToMatchingProducts(foundMatchingProductJSONArray)
    }

    private fun getHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        headers.add("authority", "stockx.com")
        headers.add("accept", "application/json")
        headers.add("accept-language", "en-US")
        headers.add("apollographql-client-name", "Iron")
        headers.add("apollographql-client-version", "2023.12.10.05")
        headers.add("app-platform", "Iron")
        headers.add("app-version", "2023.12.10.05")
        headers.add("content-type", "application/json")
        headers.add("origin", "https://stockx.com")
        //headers.add("referer", "https://stockx.com/air-jordan-1-retro-low-og-sp-travis-scott-olive-w")
        headers.add("sec-ch-ua", "\" Not_A Brand\";v=\"8\", \" Chromium \";v=\"120\", \" Google Chrome\";v=\"120\"")
        headers.add("sec-ch-ua-mobile", "?0")
        headers.add("sec-ch-ua-platform", "Windows")
        headers.add("sec-fetch-dest", "empty")
        headers.add("sec-fetch-mode", "cors")
        headers.add("sec-fetch-site", "same-origin")
        headers.add("selected-country", "PL")
        headers.add(
            "user-agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
        )
        headers.add("x-operation-name", "GetSearchResults")
        headers.add("x-stockx-device-id", "1f1fe444-feca-432e-8629-25af27bc0722")
        headers.add("x-stockx-session-id", "668161b4-03c8-4fb0-9c77-ad29a9670949")
        return headers
    }
}