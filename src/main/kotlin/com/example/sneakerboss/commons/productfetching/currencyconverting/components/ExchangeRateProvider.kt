package com.example.sneakerboss.commons.productfetching.currencyconverting.components

interface ExchangeRateProvider {

    fun getCurrentExchangeRate(currencyCode: CurrencyCode): Float?
}