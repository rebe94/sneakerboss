package com.example.sneakerboss.commons.productfetching.currencyconverting

import com.example.sneakerboss.commons.productfetching.currencyconverting.components.CurrencyCode
import com.example.sneakerboss.commons.productfetching.currencyconverting.components.ExchangeRateClient
import org.springframework.stereotype.Service

@Service
class PriceConverter(private val exchangeRateClient: ExchangeRateClient) {

    fun convertToPln(amount: Float, currencyCode: CurrencyCode): Float? {
        val exchangeRate = exchangeRateClient.getCurrentExchangeRate(currencyCode)
        exchangeRate ?: return null
        return amount * exchangeRate
    }
}