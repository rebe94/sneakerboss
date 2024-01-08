package com.example.sneakerboss.commons.productfetching.currencyconverting

import com.example.sneakerboss.commons.productfetching.currencyconverting.components.CurrencyCode
import com.example.sneakerboss.commons.productfetching.currencyconverting.components.ExchangeRateClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock

class PriceConverterUnitTest {

    private lateinit var priceConverter: PriceConverter
    private lateinit var exchangeRateClient: ExchangeRateClient

    @BeforeEach
    fun setup() {
        exchangeRateClient = mock(ExchangeRateClient::class.java)
        priceConverter = PriceConverter(exchangeRateClient)
    }

    @Test
    fun `returns correct converted price in PLN when given price value in EUR`() {
        //given
        val currencyCode = CurrencyCode.EUR
        given(exchangeRateClient.getCurrentExchangeRate(currencyCode)).willReturn(2.0F)

        //when
        val actualConvertedPrice = priceConverter.convertToPln(20F, currencyCode)

        //then
        assertThat(actualConvertedPrice).isEqualTo(40F)
    }

    @Test
    fun `returns null when exchange rate client could not provide currency rate`() {
        //given
        val currencyCode = CurrencyCode.EUR
        given(exchangeRateClient.getCurrentExchangeRate(currencyCode)).willReturn(null)

        //when
        val actualConvertedPrice = priceConverter.convertToPln(20F, currencyCode)

        //then
        assertThat(actualConvertedPrice).isNull()
    }
}