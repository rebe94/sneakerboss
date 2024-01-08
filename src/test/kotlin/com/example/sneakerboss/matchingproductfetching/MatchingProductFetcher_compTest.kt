package com.example.sneakerboss.matchingproductfetching

import com.example.sneakerboss.commons.httprequestexecuting.HttpRequestExecuter
import com.example.sneakerboss.commonsfortesting.any
import com.example.sneakerboss.extensions.getTextFromResource
import com.example.sneakerboss.extensions.substitute
import com.example.sneakerboss.matchingproductfetching.dto.MatchingProductDto
import com.example.sneakerboss.matchingproductfetching.dto.MatchingProductParser
import java.net.URL
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.anyString
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class MatchingProductFetcherCompTest {

    private lateinit var matchingProductFetcher: MatchingProductFetcher
    private lateinit var httpRequestExecuter: HttpRequestExecuter

    private val FIRST_TOP_EXPECTED_MATCHING_PRODUCT = MatchingProductDto(
        uuid = UUID.fromString("8d8fa98b-7777-441b-b927-917ac5d3b255"),
        urlKey = "nike-dunk-low-off-white-lot-31",
        title = "Nike Dunk Low Off-White Lot 31",
        brand = "Nike",
        category = "sneakers",
        styleId = "DJ0950-116",
        gender = "men",
        imageUrl = URL("https://images.stockx.com/images/Nike-Dunk-Low-Off-White-Lot-31-Product.jpg?fit=fill&bg=FFFFFF&w=300&h=214&fm=webp&auto=compress&q=90&dpr=2&trim=color&updated_at=1636047231")
    )

    @BeforeEach
    fun setup() {
        httpRequestExecuter = Mockito.mock(HttpRequestExecuter::class.java)
        matchingProductFetcher = MatchingProductFetcher(httpRequestExecuter, MatchingProductParser())
    }

    @Test
    fun `returns correct top first matching product among two when given very detailed key`() {
        //given
        val key = "dunk lot 31"
        givenTwoMatchingProductStockxResponse()

        //when
        val matchingProducts = matchingProductFetcher.searchProductBy(key)

        //then
        assertThat(matchingProducts.first()).isEqualTo(FIRST_TOP_EXPECTED_MATCHING_PRODUCT)
        assertThat(matchingProducts.size).isEqualTo(2)
    }

    @Test
    fun `returns empty list when given key is hard to match anything`() {
        //given
        val randomKey = "asdfsadfsadfs"
        givenStockxResponse("stockxApiResponses/getSearchResultsResponses/noMatchingProductFound.json")

        //when
        val matchingProducts = matchingProductFetcher.searchProductBy(randomKey)

        //then
        assertThat(matchingProducts).isEmpty()
    }

    private fun givenTwoMatchingProductStockxResponse() = givenStockxResponse(
        "stockxApiResponses/getSearchResultsResponses/twoMatchingProductFound.json",
        mapOf(
            "UUID" to FIRST_TOP_EXPECTED_MATCHING_PRODUCT.uuid.toString(),
            "URL_KEY" to FIRST_TOP_EXPECTED_MATCHING_PRODUCT.urlKey,
            "TITLE" to FIRST_TOP_EXPECTED_MATCHING_PRODUCT.title,
            "BRAND" to FIRST_TOP_EXPECTED_MATCHING_PRODUCT.brand,
            "CATEGORY" to FIRST_TOP_EXPECTED_MATCHING_PRODUCT.category,
            "STYLE_ID" to FIRST_TOP_EXPECTED_MATCHING_PRODUCT.styleId,
            "GENDER" to FIRST_TOP_EXPECTED_MATCHING_PRODUCT.gender,
            "IMAGE_URL" to FIRST_TOP_EXPECTED_MATCHING_PRODUCT.imageUrl.toString()
        )
    )

    private fun givenStockxResponse(path: String, substitutions: Map<String, String> = emptyMap()) {
        given(
            httpRequestExecuter.executePostRequest(
                anyString(),
                any(HttpHeaders::class.java),
                anyString()
            )
        ).willReturn(
            ResponseEntity(
                getTextFromResource(path).substitute(substitutions), HttpStatus.OK
            )
        )
    }
}