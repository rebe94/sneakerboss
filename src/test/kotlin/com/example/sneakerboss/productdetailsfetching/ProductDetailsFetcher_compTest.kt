package com.example.sneakerboss.productdetailsfetching

import com.example.sneakerboss.commons.httprequestexecuting.HttpRequestExecuter
import com.example.sneakerboss.commons.productfetching.PriceCalculator
import com.example.sneakerboss.commons.productfetching.currencyconverting.PriceConverter
import com.example.sneakerboss.commons.productfetching.currencyconverting.components.CurrencyCode
import com.example.sneakerboss.commons.productfetching.productmarkerdatafetching.ProductMarketDataFetcher
import com.example.sneakerboss.commons.productfetching.productmarkerdatafetching.ShoeVariantParser
import com.example.sneakerboss.commonsfortesting.ProductMarketDataProvider.Companion.FIXED_TOTAL_PAYOUT_VALUE
import com.example.sneakerboss.commonsfortesting.ProductMarketDataProvider.Companion.getProductDetails
import com.example.sneakerboss.commonsfortesting.ProductMarketDataProvider.Companion.getSubstitutionMapForExpectedProductMarketData
import com.example.sneakerboss.commonsfortesting.any
import com.example.sneakerboss.extensions.getTextFromResource
import com.example.sneakerboss.extensions.substitute
import com.example.sneakerboss.productdetailsfetching.dto.ProductDetailsDto
import com.example.sneakerboss.productdetailsfetching.dto.ProductDetailsParser
import com.example.sneakerboss.userproductfetching.dto.UserSettingDto
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.mockito.ArgumentMatchers.anyFloat
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ProductDetailsFetcherCompTest {

    private lateinit var productDetailsFetcher: ProductDetailsFetcher
    private lateinit var priceConverter: PriceConverter
    private lateinit var priceCalculator: PriceCalculator
    private lateinit var productMarketDataFetcher: ProductMarketDataFetcher
    private lateinit var httpRequestExecuter: HttpRequestExecuter

    private val EXPECTED_PRODUCT_DETAILS = getProductDetails()

    @BeforeEach
    fun setup() {
        priceConverter = mock(PriceConverter::class.java)
        priceCalculator = mock(PriceCalculator::class.java)
        httpRequestExecuter = mock(HttpRequestExecuter::class.java)
        productMarketDataFetcher = ProductMarketDataFetcher(httpRequestExecuter)
        productDetailsFetcher = ProductDetailsFetcher(
            productMarketDataFetcher = productMarketDataFetcher,
            productDetailsParser = ProductDetailsParser(
                ShoeVariantParser(
                    priceConverter,
                    priceCalculator
                )
            )
        )
    }

    @Test
    fun `returns product details with shoe variants when given existing StockX ID and user settings`() {
        //given
        givenStockxResponse("stockxApiResponses/getMarketDataResponses/productMarketDataFound.json")
        givenFixedTotalPayoutValue()

        //when
        val actualProductDetailsDto =
            productDetailsFetcher.findProductBy(EXPECTED_PRODUCT_DETAILS.uuid, UserSettingDto.DEFAULT)

        //then
        assertThatObjectsEqual(actualProductDetailsDto, EXPECTED_PRODUCT_DETAILS)
    }

    @Test
    fun `returns null when given non existing StockX ID`() {
        //given
        val nonExistingProductUuid = UUID.randomUUID()
        givenStockxResponse("stockxApiResponses/getMarketDataResponses/noProductMarketDataFound.json")

        //when
        val actualProductDetailsDto =
            productDetailsFetcher.findProductBy(nonExistingProductUuid, UserSettingDto.DEFAULT)

        //then
        assertThat(actualProductDetailsDto).isNull()
    }

    private fun givenStockxResponse(path: String) {
        given(
            httpRequestExecuter.executePostRequest(
                anyString(),
                any(HttpHeaders::class.java),
                anyString()
            )
        ).willReturn(
            ResponseEntity<String>(
                getTextFromResource(path).substitute(
                    getSubstitutionMapForExpectedProductMarketData()
                ), HttpStatus.OK
            )
        )
    }

    private fun assertThatObjectsEqual(
        actualProductDetailsDto: ProductDetailsDto?,
        expectedProductDetails: ProductDetailsDto
    ) {
        assertAll(
            {
                assertThat(actualProductDetailsDto?.uuid).isEqualTo(expectedProductDetails.uuid)
                assertThat(actualProductDetailsDto?.title).isEqualTo(expectedProductDetails.title)
                assertThat(actualProductDetailsDto?.numberOfBids).isEqualTo(expectedProductDetails.numberOfBids)
                assertThat(actualProductDetailsDto?.numberOfAsks).isEqualTo(expectedProductDetails.numberOfAsks)
                assertThat(actualProductDetailsDto?.deadstockSold).isEqualTo(expectedProductDetails.deadstockSold)
                assertThat(actualProductDetailsDto?.shoeVariants).isEqualTo(expectedProductDetails.shoeVariants)
            }
        )
    }

    private fun givenFixedTotalPayoutValue() {
        given(priceConverter.convertToPln(anyFloat(), any(CurrencyCode::class.java))).willReturn(
            FIXED_TOTAL_PAYOUT_VALUE
        )
        given(priceCalculator.calculatePayout(anyFloat(), anyFloat())).willReturn(FIXED_TOTAL_PAYOUT_VALUE)
    }
}
