package com.example.sneakerboss.commons.productfetching.productmarkerdatafetching

import com.example.sneakerboss.commons.httprequestexecuting.HttpRequestExecuter
import com.example.sneakerboss.commons.productfetching.currencyconverting.components.CurrencyCode
import com.example.sneakerboss.commons.productfetching.currencyconverting.components.Region
import com.example.sneakerboss.extensions.at
import com.example.sneakerboss.extensions.getTextFromResource
import com.example.sneakerboss.extensions.substitute
import java.util.UUID
import org.json.JSONObject
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service

@Service
class ProductMarketDataFetcher(
    private val httpRequestExecuter: HttpRequestExecuter
) {

    companion object {
        private const val FIND_PRODUCT_BASE_URL = "https://stockx.com/api/p/e"
    }

    fun findProductMarketDataBy(uuid: UUID, currencyCode: CurrencyCode, region: Region): JSONObject? {
        val requestBody = getTextFromResource("getMarketDataRequestBody.json").substitute(
            mapOf(
                "PRODUCT_UUID" to uuid.toString(),
                "CURRENCY_CODE" to currencyCode.toString(),
                "COUNTRY_CODE" to region.abbreviation.uppercase()
            )
        )
        val response = httpRequestExecuter.executePostRequest(FIND_PRODUCT_BASE_URL, getHeaders(), requestBody)
        return JSONObject(response.body).at("data").at("product")
    }

    private fun getHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        //headers.add("cookie", "language_code=en; stockx_device_id=1f1fe444-feca-432e-8629-25af27bc0722; __ssid=b2324686eaa247232daf2ccc46c9c04; rskxRunCookie=0; rCookie=1z58sstg40dj9yta0ryndlfwzvdj6; __pxvid=39bf7dd7-d002-11ed-8cc1-0242ac120002; ajs_anonymous_id=f0e8a1e8-ed90-4512-8517-da2cbc4550e0; QuantumMetricUserID=2a5d9a5a5f8bc1abf60bed1c700a7a89; stockx_homepage=sneakers; OptanonAlertBoxClosed=2023-03-31T20:26:41.981Z; ajs_user_id=ac3ff493-9ea3-11eb-9825-124738b50e12; _pin_unauth=dWlkPVl6VmpZVFk0WVRJdFkyWXlOQzAwWWpjMUxXSXpabU10WmpsaU5HRm1OekpqT0RkaQ; stockx_seen_ask_new_info=true; __pdst=f6c4bbf9ac8e46ef95d4fecf7cc983cc; _ga=GA1.2.765721649.1680294402; ftr_ncd=6; _derived_epik=dj0yJnU9Wm9mSGJTQlE5U1BmNlMxNXJzN2lKM3pxYnJmSDNRVHYmbj1qYThnaGg1Ni1SaUVnT085UURnUWF3Jm09NCZ0PUFBQUFBR1FuWnp3JnJtPTQmcnQ9QUFBQUFHUW5aencmc3A9Mg; RoktRecogniser=910a0066-d8c7-4047-8949-96a647b1ae86; _pxvid=389bb70e-d002-11ed-9550-e7180b2d9bd2; _gcl_au=1.1.942135275.1702140640; rbuid=rbos-bff60988-cd9c-4e0c-927e-ef65fb1dc825; stockx_session_id=d3a56fad-2810-4913-b2b8-1ea72c94cd3a; stockx_session=3d0bdeec-3ce2-4fea-8248-9d07a54b5226; stockx_selected_region=PL; __cf_bm=YlIIUGF.fZicrvL_oOHXXXP7CmwejHHyleKG4fl.j1k-1702740273-1-Af1wAH799vQHjJOcDuP/X6u48iZsW7QxPNB91Vbfa4Bq93en4wynC2gCiKMB2GFckt7VEa7Efz19idTcT1gW/dQ=; stockx_preferred_market_activity=sales; display_location_selector=false; is_gdpr=true; stockx_ip_region=PL; pxcts=37fb2104-9c27-11ee-909e-3b543de37dc5; cf_clearance=ijNVB.m0fcZAMDFGu7Bx0pdMlpToh.iXdMEhtWwKork-1702740275-0-1-df7f8abf.400f1e9c.5a7fac1b-0.2.1702740275; _px3=ea8935504ab85d604be61ba26b5de638c78e40505e5f1ea46d99512c1fc24db8:pzLKWo10EmpTHzeREYqHNnphe/AcFdPf8wqaoLPAooV/8D1HUbyUJvqery6ZRTVlbwEiUYRAljlrWUJUjG5DEQ==:1000:05No/rACWQ1rT4C3VP5Y1wJor/16dvXxFSGcQmN1KQxLXXdWjrjmEBf9U91zIbk5YAMRVD30UXuu7JSbCcquCVByiTFvBbZowJYXOeLKmd94ZA+umChY27267r35YeRED5W1hUWpFpcSjS7YMd5IB3e5C3lVuHCvln65ZBCLuPrhlGYz4Og8P2EwHSpZUCB/LEvkWVGAdHEnRWMa1UIECHiUZofxniYHN8vRmD+Jhm8=; _gid=GA1.2.892621357.1702740275; QuantumMetricSessionID=d7f6205c9d1966b7bf048ea2d1bef5ee; _ga=GA1.1.765721649.1680294402; ftr_blst_1h=1702740283536; lastRskxRun=1702740283572; forterToken=16b39fd043c6490d88b3de61540fb859_1702740275933_1882_UDF43-m4_13ck; _tq_id.TV-6345811836-1.1a3e=352e80643701eef8.1702140647.0.1702740286..; IR_gbd=stockx.com; IR_9060=1702740287083%7C4294847%7C1702740287083%7C%7C; IR_PI=3a6cdcf2-8756-33ad-83ea-a86360c94a89%7C1702826687083; _gat=1; _uetsid=3cfb8c109c2711eea05e43dbdf6b4e2f; _uetvid=44de8520d00211ed9185cd880f0ef666; _dd_s=rum=0&expire=1702741236571&logs=1&id=cbf2aa06-a5e3-40bb-95de-4bf354e012b4&created=1702740274185; _ga_TYYSNQDG4W=GS1.1.1702740277.4.1.1702740344.0.0.0; _pxde=a387071b17e89d1be9c7cfda5933af65f77edae7f4c53825ee0cb03eba8e4ca3:eyJ0aW1lc3RhbXAiOjE3MDI3NDAzNDU1NTYsImZfa2IiOjB9; OptanonConsent=isGpcEnabled=0&datestamp=Sat+Dec+16+2023+16%3A25%3A45+GMT%2B0100+(Central+European+Standard+Time)&version=202309.1.0&isIABGlobal=false&hosts=&consentId=21d5a1e9-59e6-4dd7-8aea-50cd84e4fdbb&interactionCount=2&landingPath=NotLandingPage&groups=C0001%3A1%2CC0002%3A1%2CC0005%3A1%2CC0004%3A1%2CC0003%3A1&AwaitingReconsent=false&geolocation=PL%3B02&browserGpcFlag=0; stockx_product_visits=4")
        headers.add("authority", "stockx.com")
        headers.add("accept", "application/json")
        headers.add("accept-language", "en-US")
        headers.add("apollographql-client-name", "Iron")
        headers.add("apollographql-client-version", "2023.12.10.03")
        headers.add("app-platform", "Iron")
        headers.add("app-version", "2023.12.10.03")
        headers.add("content-type", "application/json")
        headers.add("origin", "https://stockx.com")
        //headers.add("referer", "https://stockx.com/air-jordan-1-retro-low-og-sp-travis-scott-black-phantom")
        headers.add("sec-ch-ua", "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"")
        headers.add("sec-ch-ua-mobile", "?0")
        headers.add("sec-ch-ua-platform", "\"Windows\"")
        headers.add("sec-fetch-dest", "empty")
        headers.add("sec-fetch-mode", "cors")
        headers.add("sec-fetch-site", "same-origin")
        headers.add("selected-country", "PL")
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
        headers.add("x-operation-name", "GetMarketData")
        headers.add("x-stockx-device-id", "1f1fe444-feca-432e-8629-25af27bc0722")
        //headers.add("x-stockx-session-id", "d3a56fad-2810-4913-b2b8-1ea72c94cd3a")

        return headers
    }
}