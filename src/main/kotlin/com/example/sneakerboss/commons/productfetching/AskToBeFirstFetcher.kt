package com.example.sneakerboss.commons.productfetching

import com.example.sneakerboss.commons.httprequestexecuting.HttpRequestExecuter
import com.example.sneakerboss.commons.productfetching.currencyconverting.components.CurrencyCode
import com.example.sneakerboss.commons.productfetching.currencyconverting.components.Region
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class AskToBeFirstFetcher(
    private val httpRequestExecuter: HttpRequestExecuter
) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(AskToBeFirstFetcher::class.java)
        private const val STOCKX_API_BASE_URL = "https://stockx.com/api/p/e"
    }

    fun getAskToBeFirst(productUuid: UUID, currencyCode: CurrencyCode, region: Region): Int {
        val uri = STOCKX_API_BASE_URL
        val headers = getHeaders()
        val requestBody = getRequestBody(productUuid, currencyCode, region)
        val response = httpRequestExecuter.executePostRequest(uri, headers, requestBody.toString())
        return extractAskToBeFirstValue(response)
    }

    private fun getHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        headers.add("authority", "stockx.com")
        headers.add("accept", "*/*")
        headers.add("accept-language", "en-US")
        headers.add("apollographql-client-name", "Iron")
        headers.add("apollographql-client-version", "2023.03.26.02")
        headers.add("app-platform", "Iron")
        headers.add("app-version", "2023.03.26.02")
        /*headers.add(
            "authorization",
            "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik5USkNNVVEyUmpBd1JUQXdORFk0TURRelF6SkZRelV4TWpneU5qSTNNRFJGTkRZME0wSTNSQSJ9.eyJodHRwczovL3N0b2NreC5jb20vY3VzdG9tZXJfdXVpZCI6ImFjM2ZmNDkzLTllYTMtMTFlYi05ODI1LTEyNDczOGI1MGUxMiIsImh0dHBzOi8vc3RvY2t4LmNvbS9nYV9ldmVudCI6IkxvZ2dlZCBJbiIsImh0dHBzOi8vc3RvY2t4LmNvbS9lbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6Ly9hY2NvdW50cy5zdG9ja3guY29tLyIsInN1YiI6Imdvb2dsZS1vYXV0aDJ8MTA5NTA5OTk3NjIxNDUzMTg2NzU0IiwiYXVkIjpbImdhdGV3YXkuc3RvY2t4LmNvbSIsImh0dHBzOi8vc3RvY2t4LXByb2QuYXV0aDAuY29tL3VzZXJpbmZvIl0sImlhdCI6MTY4MDI5NDM3NCwiZXhwIjoxNjgwMzM3NTc0LCJhenAiOiJPVnhydDRWSnFUeDdMSVVLZDY2MVcwRHVWTXBjRkJ5RCIsInNjb3BlIjoib3BlbmlkIHByb2ZpbGUifQ.fEr-wpN4VtvOLHxL2BD5uipn3a3sU7v4SNOLrUyaGFRit97-lU-JdF-PGsFK4HWWW_AJ2ixZRGLv2SP6CmHNZN6CldBM9Pco-wAnEYhnyirli0Q2WwFr8xiWO1rFzP516i7pqgA3vodVxhjMjnssh8eZhvFgtfqPupN_AadH3vLeEBMY30x976H9taXJ_XUkZyXtojXkGc-zKa2ECwL8BoI7jAuI2LKVafUwI4RRzs95CHiS-NTgoxgEU89HB5a6pHla4sYx1aei1SRqbquM8T0k4LqIqdWjktO_Z5JsLAfAwhtQa1_g4MGMi62GreKwqWTstVmf23fMf7qSxBMjxg"
        )*/
        headers.add("content-type", "application/json")
        headers.add(
            "cookie",
            "language_code=en; stockx_selected_region=PL; stockx_device_id=1f1fe444-feca-432e-8629-25af27bc0722; stockx_session_id=191677aa-1473-41f7-89d4-f5d9e8fde9b2; stockx_session=473b6526-b96f-4175-95a4-75c2214ce788; __cf_bm=VFSgRpRvlhKhb43Vm8T939UIwY7fZPl4fCbpuJUd.Pk-1680294346-0-ARJfsFtZJ3EK15YLGoG/mFeEr3qLthKVoQl+j2hXl5oQYH2WVwXhc8BwY9ccFrqJWTls/YPv+FtD1zWHwWa9//I=; pxcts=389bc79e-d002-11ed-9550-78477678476f; _pxvid=389bb70e-d002-11ed-9550-78477678476f; _com.auth0.auth.%7B%22state%22%3A%7B%22forceLogin%22%3Atrue%7D%7D_compat={%22nonce%22:null%2C%22state%22:%22{%5C%22state%5C%22:{%5C%22forceLogin%5C%22:true}}%22%2C%22lastUsedConnection%22:%22production%22}; com.auth0.auth.%7B%22state%22%3A%7B%22forceLogin%22%3Atrue%7D%7D={%22nonce%22:null%2C%22state%22:%22{%5C%22state%5C%22:{%5C%22forceLogin%5C%22:true}}%22%2C%22lastUsedConnection%22:%22production%22}; __ssid=b2324686eaa247232daf2ccc46c9c04; lastRskxRun=1680294349120; rskxRunCookie=0; rCookie=1z58sstg40dj9yta0ryndlfwzvdj6; __pxvid=39bf7dd7-d002-11ed-8cc1-0242ac120002; ftr_blst_1h=1680294350267; _gcl_au=1.1.814091855.1680294359; ajs_anonymous_id=f0e8a1e8-ed90-4512-8517-da2cbc4550e0; rbuid=rbos-bff60988-cd9c-4e0c-927e-ef65fb1dc825; QuantumMetricSessionID=c4ee55ef69dbc19103cc31b00783604f; QuantumMetricUserID=2a5d9a5a5f8bc1abf60bed1c700a7a89; token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ik5USkNNVVEyUmpBd1JUQXdORFk0TURRelF6SkZRelV4TWpneU5qSTNNRFJGTkRZME0wSTNSQSJ9.eyJodHRwczovL3N0b2NreC5jb20vY3VzdG9tZXJfdXVpZCI6ImFjM2ZmNDkzLTllYTMtMTFlYi05ODI1LTEyNDczOGI1MGUxMiIsImh0dHBzOi8vc3RvY2t4LmNvbS9nYV9ldmVudCI6IkxvZ2dlZCBJbiIsImh0dHBzOi8vc3RvY2t4LmNvbS9lbWFpbF92ZXJpZmllZCI6dHJ1ZSwiaXNzIjoiaHR0cHM6Ly9hY2NvdW50cy5zdG9ja3guY29tLyIsInN1YiI6Imdvb2dsZS1vYXV0aDJ8MTA5NTA5OTk3NjIxNDUzMTg2NzU0IiwiYXVkIjpbImdhdGV3YXkuc3RvY2t4LmNvbSIsImh0dHBzOi8vc3RvY2t4LXByb2QuYXV0aDAuY29tL3VzZXJpbmZvIl0sImlhdCI6MTY4MDI5NDM3NCwiZXhwIjoxNjgwMzM3NTc0LCJhenAiOiJPVnhydDRWSnFUeDdMSVVLZDY2MVcwRHVWTXBjRkJ5RCIsInNjb3BlIjoib3BlbmlkIHByb2ZpbGUifQ.fEr-wpN4VtvOLHxL2BD5uipn3a3sU7v4SNOLrUyaGFRit97-lU-JdF-PGsFK4HWWW_AJ2ixZRGLv2SP6CmHNZN6CldBM9Pco-wAnEYhnyirli0Q2WwFr8xiWO1rFzP516i7pqgA3vodVxhjMjnssh8eZhvFgtfqPupN_AadH3vLeEBMY30x976H9taXJ_XUkZyXtojXkGc-zKa2ECwL8BoI7jAuI2LKVafUwI4RRzs95CHiS-NTgoxgEU89HB5a6pHla4sYx1aei1SRqbquM8T0k4LqIqdWjktO_Z5JsLAfAwhtQa1_g4MGMi62GreKwqWTstVmf23fMf7qSxBMjxg; loggedIn=ac3ff493-9ea3-11eb-9825-124738b50e12; stockx_selected_currency=EUR; _px3=8f0bee0eb42adc82d5c351124848474759b1597fb5de335a6dbe3b05d6383664:+bo2PXVKQNJx+Z1bIWnuzyVRLNY6TYZ6kvtYCzR7pMkSmMunqwrS5MJszMrEbnYVl7+eDJ6d5XFeV6djAQpiFg==:1000:nM0Q2Y4fzZRsHRZS/UOSDHbLqoQhY+ioZUD7cNxJnnKfcpcLfOao7fg+mRY48mJU9Ouepr9r09rZbCvZrtbEob7X5pIe0B4/uZzusKVPd/HUEvCPG/lrM5iRgp7kaKVJrYDYhIFpkoPFuvzEDMcJmxq0yOm+zPbUFWNsUaKwl5TlM7SxxHUeTUjL+mcZ/MM4qixbK7plUIpBdlIOXTSLZg==; forterToken=16b39fd043c6490d88b3de61540fb859_1680294376888__UDF43-m4_13ck; stockx_preferred_market_activity=sales; stockx_product_visits=1; stockx_homepage=sneakers; stockx_default_size=%7B%22sneakers%22%3A%2210.5%22%2C%22shoes%22%3A%2210.5%22%7D; _pxde=fd4723a28cef1cb6496f1b1c96b680a8545693d52df78f915cba76517fcd464b:eyJ0aW1lc3RhbXAiOjE2ODAyOTQzOTYxODcsImZfa2IiOjB9; OptanonAlertBoxClosed=2023-03-31T20:26:41.981Z; OptanonConsent=isGpcEnabled=0&datestamp=Fri+Mar+31+2023+22%3A26%3A41+GMT%2B0200+(Central+European+Summer+Time)&version=202211.2.0&isIABGlobal=false&hosts=&consentId=21d5a1e9-59e6-4dd7-8aea-50cd84e4fdbb&interactionCount=2&landingPath=NotLandingPage&groups=C0001%3A1%2CC0002%3A1%2CC0005%3A1%2CC0004%3A1%2CC0003%3A1&AwaitingReconsent=false; ajs_user_id=ac3ff493-9ea3-11eb-9825-124738b50e12; _ga=GA1.2.765721649.1680294402; _gid=GA1.2.494602226.1680294402; _gat=1; _uetsid=44de3d70d00211ed9d75417f24bb5930; _uetvid=44de8520d00211ed9185cd880f0ef666; _pin_unauth=dWlkPVl6VmpZVFk0WVRJdFkyWXlOQzAwWWpjMUxXSXpabU10WmpsaU5HRm1OekpqT0RkaQ; _dd_s=rum=0&expire=1680295303264; stockx_seen_ask_new_info=true"
        )
        headers.add("origin", "https://stockx.com")
        headers.add("sec-ch-ua", "\"Google Chrome\";v=\"111\", \"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"111\"")
        headers.add("sec-ch-ua-mobile", "?0")
        headers.add("sec-ch-ua-platform", "\"Windows\"")
        headers.add("sec-fetch-dest", "empty")
        headers.add("sec-fetch-mode", "cors")
        headers.add("sec-fetch-site", "same-origin")
        headers.add("selected-country", "PL")
        headers.add(
            "user-agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36"
        )
        headers.add("x-operation-name", "GetPricingGuidanceByProduct")
        headers.add("x-stockx-device-id", "1f1fe444-feca-432e-8629-25af27bc0722")
        headers.add("x-stockx-session-id", "191677aa-1473-41f7-89d4-f5d9e8fde9b2")
        return headers
    }

    private fun getRequestBody(productUuid: UUID, currencyCode: CurrencyCode, region: Region): JSONObject {
        val variables = JSONObject()
        variables.put("id", "$productUuid")
        variables.put("currencyCode", "$currencyCode")
        variables.put("country", "$region")
        variables.put("market", "$region")
        val body = JSONObject()
        body.put("variables", variables)
        body.put(
            "query",
            "query GetPricingGuidanceByProduct(\$id: String!, \$currencyCode: CurrencyCode, \$country: String, \$market: String) {  variant(id: \$id) {    pricingGuidance(currencyCode: \$currencyCode, country: \$country, market: \$market) {      sellingGuidance {        earnMore        }    }  }}"
        )
        body.put(
            "operationName",
            "GetPricingGuidanceByProduct"
        )
        return body
    }

    private fun extractAskToBeFirstValue(response: ResponseEntity<String>): Int {
        val json = JSONObject(response.body)
        val earnMore = json.optJSONObject("data")?.optJSONObject("variant")?.optJSONObject("pricingGuidance")
            ?.optJSONObject("sellingGuidance")?.optInt("earnMore") ?: 0
        return earnMore
    }
}