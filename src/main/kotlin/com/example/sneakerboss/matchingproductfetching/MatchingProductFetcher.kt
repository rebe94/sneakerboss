package com.example.sneakerboss.matchingproductfetching

import com.example.sneakerboss.httpexecuter.HttpRequestExecuter
import com.example.sneakerboss.matchingproductfetching.components.MatchingProduct
import com.example.sneakerboss.matchingproductfetching.components.MatchingProductFetchable
import org.json.JSONArray
import org.json.JSONObject
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import java.net.URL
import java.util.*


@Service
class MatchingProductFetcher(private val httpRequestExecuter: HttpRequestExecuter) : MatchingProductFetchable {

    companion object {
        private const val SEARCH_PRODUCT_BASE_URL = "https://stockx.com/api/browse?_search="
        private const val LIMIT_FOUND_PRODUCTS = 10
    }

    override fun searchProductBy(key: String): List<MatchingProduct> {
        val keyWithoutWhitespaces = key.replace(" ", "+")
        val uri = "$SEARCH_PRODUCT_BASE_URL$keyWithoutWhitespaces}"
        val headers = getHeaders()
        val response = httpRequestExecuter.executeHttpGetRequest(uri, headers)
        val jsonResponse = JSONObject(response.body)
        val matchingProductJSONArray = jsonResponse.getJSONArray("Products")
        if (matchingProductJSONArray.isEmpty) return emptyList()
        val matchingProducts = parseToMatchingProducts(matchingProductJSONArray)
        return matchingProducts
    }

    private fun getHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:108.0) Gecko/20100101 Firefox/108.0")
        headers.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
        headers.add("Accept-Language", "en-US,en;q=0.5")
        //headers.add("Accept-Encoding", "gzip, deflate, br")
        headers.add("Alt-Used", "stockx.com")
        headers.add("Connection", "keep-alive")
        headers.add(
            "Cookie",
            "stockx_device_id=733d67bd-1e3a-425e-b597-ba0ae285a464; forterToken=e6c1eae5d1e842178d51f439a1783281_1673134446217_242_UDF9_13ck; _ga=undefined; OptanonConsent=isGpcEnabled=0&datestamp=Sun+Jan+08+2023+00%3A34%3A08+GMT%2B0100+(Central+European+Standard+Time)&version=202211.2.0&isIABGlobal=false&hosts=&consentId=f290dc45-54df-46e4-be59-3b23c72d5d28&interactionCount=1&landingPath=NotLandingPage&groups=C0001%3A1%2CC0002%3A1%2CC0005%3A1%2CC0004%3A1%2CC0003%3A1&geolocation=PL%3B02&AwaitingReconsent=false; stockx_homepage=sneakers; OptanonAlertBoxClosed=2022-10-04T18:38:36.715Z; ajs_anonymous_id=c05d2af4-814f-4cbb-9b0e-bc1a8cd111eb; ajs_user_id=ac3ff493-9ea3-11eb-9825-124738b50e12; stockx_seen_ask_new_info=true; _pxvid=df9a6d72-4fe8-11ed-9fc1-67537045744f; __pxvid=e011e731-4fe8-11ed-9e4c-0242ac120002; rbuid=rbos-b6be326b-338a-4ad1-bc1b-265ae8c09a0a; stockx_dismiss_modal=true; stockx_dismiss_modal_set=2022-12-19T17%3A38%3A33.245Z; stockx_dismiss_modal_expiration=2023-12-19T17%3A38%3A33.245Z; token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik5USkNNVVEyUmpBd1JUQXdORFk0TURRelF6SkZRelV4TWpneU5qSTNNRFJGTkRZME0wSTNSQSJ9.eyJodHRwczovL3N0b2NreC5jb20vY3VzdG9tZXJfdXVpZCI6ImFjM2ZmNDkzLTllYTMtMTFlYi05ODI1LTEyNDczOGI1MGUxMiIsImh0dHBzOi8vc3RvY2t4LmNvbS9nYV9ldmVudCI6IkxvZ2dlZCBJbiIsImlzcyI6Imh0dHBzOi8vYWNjb3VudHMuc3RvY2t4LmNvbS8iLCJzdWIiOiJnb29nbGUtb2F1dGgyfDEwOTUwOTk5NzYyMTQ1MzE4Njc1NCIsImF1ZCI6WyJnYXRld2F5LnN0b2NreC5jb20iLCJodHRwczovL3N0b2NreC1wcm9kLmF1dGgwLmNvbS91c2VyaW5mbyJdLCJpYXQiOjE2NzMwOTYzMzAsImV4cCI6MTY3MzEzOTUzMCwiYXpwIjoiT1Z4cnQ0VkpxVHg3TElVS2Q2NjFXMER1Vk1wY0ZCeUQiLCJzY29wZSI6Im9wZW5pZCBwcm9maWxlIn0.OyeZ09wn3ES1kI7tEb8GhqUIDngPmh8oIPn5a6B_kEeul0tcGe1j6kfeMJRmOgQm56ThWNxkLDKkb6ay6P55bYxlyf7LY172Pi7Wen8-7riZhpd86yBnKC_ZwNbRuWqWF2JoQtaPqulql0Q72lqx8X58CZp_kTi5vNkHKS9kxNXtjpFv7qQDdbZ_B96iduQ1r27KcTu_oRe_nuOUgXJYFBFuOO_034lhMIsrIhkp9BLYqULNBKDDjXvY8aclVxeTnBqYRXh5JQUoXXL9-hKr8dU5LzmiUfFH_kHpzbaYT0UgdcEwMJLTtca9wwL6ZmwguoamXecG4XxoOpF7dAP3Lg; language_code=en; stockx_preferred_market_activity=sales; stockx_product_visits=36; stockx_default_size=%7B%22sneakers%22%3A%228%22%2C%22shoes%22%3A%228%22%7D; pxcts=197cbb2c-8e8b-11ed-98b3-566c6459676c; stockx_selected_locale=en; stockx_selected_region=PL; __cf_bm=cV5wLRPcKtkDpOUu.GLFMA9Bq3x3SfMhOya5rKNnhqI-1673134402-0-AdYuiKIt2NlhHtUvZmUh6Ac1DSn7MGVCuh2fOZhvC4u6nDYloO3F05JVWsiS2SejilVpopwH3LR0NRKwyfshJYM=; stockx_session=b7432b0a-b267-4777-8dfd-9dd865a670ee; _dd_s=rum=0&expire=1673135345446; loggedIn=ac3ff493-9ea3-11eb-9825-124738b50e12; stockx_selected_currency=EUR"
        )
        headers.add("Upgrade-Insecure-Requests", "1")
        headers.add("Sec-Fetch-Dest", "document")
        headers.add("Sec-Fetch-Mode", "navigate")
        headers.add("Sec-Fetch-Site", "none")
        headers.add("Sec-Fetch-User", "?1")
        headers.add("If-None-Match", "W/esrx8z3zi34t97")
        headers.add("TE", "trailers")
        return headers
    }

    private fun parseToMatchingProducts(matchingProducts: JSONArray): List<MatchingProduct> {
        val matchingProductsList = mutableListOf<MatchingProduct>()
        val limit =
            if (matchingProducts.length() < LIMIT_FOUND_PRODUCTS) matchingProducts.length() else LIMIT_FOUND_PRODUCTS
        for (i in 0 until limit) {
            val it: JSONObject = matchingProducts.getJSONObject(i)
            val media = it.getJSONObject("media")
            matchingProductsList.add(
                MatchingProduct(
                    uuid = UUID.fromString(it.getString("uuid")),
                    title = it.getString("title"),
                    brand = it.getString("brand"),
                    category = it.getString("category"),
                    colorway = it.getString("colorway"),
                    styleId = it.getString("styleId"),
                    gender = it.getString("gender"),
                    releaseDate = it.getString("releaseDate"),
                    retailPrice = it.getInt("retailPrice"),
                    imageUrl = URL(media.getString("smallImageUrl"))
                )
            )
        }
        return matchingProductsList.toList()
    }
}