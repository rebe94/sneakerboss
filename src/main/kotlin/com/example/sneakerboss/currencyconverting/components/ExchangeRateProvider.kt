package com.example.sneakerboss.currencyconverting.components

interface ExchangeRateProvider {

    fun getCurrentExchangeRate(currencyCode: CurrencyCode): Float?
}