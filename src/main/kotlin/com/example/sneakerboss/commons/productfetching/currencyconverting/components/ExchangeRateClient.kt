package com.example.sneakerboss.commons.productfetching.currencyconverting.components

import com.example.sneakerboss.commons.httprequestexecuting.HttpRequestExecuter
import org.json.JSONObject
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class ExchangeRateClient(private val httpRequestExecuter: HttpRequestExecuter) : ExchangeRateProvider {

    companion object {
        private const val EXCHANGE_RATE_SERVICE_BASE_URI = "http://api.nbp.pl/api/exchangerates/rates/a/"
        private const val RATES_KEY = "rates"
        private const val EXCHANGE_RATE_KEY = "mid"
    }

    override fun getCurrentExchangeRate(currencyCode: CurrencyCode): Float? {
        val headers = getHeaders()
        try {
            val response =
                httpRequestExecuter.executeHttpGetRequest(EXCHANGE_RATE_SERVICE_BASE_URI + currencyCode, headers)
            return getRate(response)
        } catch (ex: Exception) {
            return null
        }
    }

    private fun getRate(response: ResponseEntity<String>): Float {
        val jsonResponse = JSONObject(response.body)
        val ratesJSONArray = jsonResponse.getJSONArray(RATES_KEY)
        val jsonObject = ratesJSONArray.getJSONObject(0)
        return jsonObject.getFloat(EXCHANGE_RATE_KEY)
    }

    private fun getHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/109.0")
        headers.add("Accept", "text/html,application/json;q=0.9,image/avif,image/webp,*/*;q=0.8")
        headers.add("Accept-Language", "en-US,en;q=0.5")
        //headers.add("Accept-Encoding", "gzip, deflate")
        headers.add("Referer", "http://api.nbp.pl/")
        headers.add("Connection", "keep-alive")
        headers.add(
            "Cookie",
            "visid_incap_2861732=cj7AWVioTNS7olUM+oUOpSRQxGMAAAAAQUIPAAAAAAAVA6ct+Ih+N5LAVyMWFscN; incap_ses_1515_2861732=8aX2a/nl8R4Wi6yQ1FwGFSRQxGMAAAAA7k7KDAbxQL1dboeVr+bu5w==; nlbi_2861732=s4vbXitpjgzmU2YP40lgewAAAADZlR1P4/vAhsfaN6y1VBiv; ee3la5eizeiY4Eix=ud5ahSho"
        )
        headers.add("Upgrade-Insecure-Requests", "1")
        headers.add("If-None-Match", "9oGTXuPGQ2qSWuXx8eL6KbtVaoBlrZhekGshJOhi31I=")
        return headers
    }
}