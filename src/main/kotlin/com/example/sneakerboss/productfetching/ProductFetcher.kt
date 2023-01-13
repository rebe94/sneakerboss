package com.example.sneakerboss.productfetching

import com.example.sneakerboss.httpexecuter.HttpRequestExecuter
import org.json.JSONException
import org.json.JSONObject
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import java.net.URL
import java.util.*

@Service
class ProductFetcher(private val httpRequestExecuter: HttpRequestExecuter) : ProductFetchable {

    companion object {
        private const val FIND_PRODUCT_BASE_URL = "https://stockx.com/api/products"
        private const val CURRENCY_CODE = "EUR"
        private const val COUNTRY = "PL"
    }

    override fun findProductBy(uuid: UUID): Product? {
        val uri = "$FIND_PRODUCT_BASE_URL/$uuid?includes=market&currency=$CURRENCY_CODE&country=$COUNTRY"
        val headers = getHeaders()
        val response = httpRequestExecuter.executeHttpGetRequest(uri, headers)
        val json = JSONObject(response.body)

        val jsonProduct = try {
            json.getJSONObject("Product")
        } catch (ex: JSONException) {
            return null
        }
        return parseToProductEntity(jsonProduct)
    }

    private fun getHeaders(): HttpHeaders {
        val headers = HttpHeaders()
        headers.add("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:108.0) Gecko/20100101 Firefox/108.0")
        headers.add("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8")
        headers.add("Accept-Language", "en-US,en;q=0.5")
        //headers.add("Accept-Encoding", "gzip, deflate, br")
        headers.add("Connection", "keep-alive")
        headers.add(
            "Cookie",
            "stockx_device_id=733d67bd-1e3a-425e-b597-ba0ae285a464; forterToken=e6c1eae5d1e842178d51f439a1783281_1673250168387_483_UDF9_13ck; _ga=undefined; OptanonConsent=isGpcEnabled=0&datestamp=Mon+Jan+09+2023+08%3A42%3A49+GMT%2B0100+(Central+European+Standard+Time)&version=202211.2.0&isIABGlobal=false&hosts=&consentId=f290dc45-54df-46e4-be59-3b23c72d5d28&interactionCount=1&landingPath=NotLandingPage&groups=C0001%3A1%2CC0002%3A1%2CC0005%3A1%2CC0004%3A1%2CC0003%3A1&geolocation=PL%3B02&AwaitingReconsent=false; stockx_homepage=sneakers; OptanonAlertBoxClosed=2022-10-04T18:38:36.715Z; ajs_anonymous_id=c05d2af4-814f-4cbb-9b0e-bc1a8cd111eb; ajs_user_id=ac3ff493-9ea3-11eb-9825-124738b50e12; stockx_seen_ask_new_info=true; _pxvid=df9a6d72-4fe8-11ed-9fc1-67537045744f; __pxvid=e011e731-4fe8-11ed-9e4c-0242ac120002; rbuid=rbos-b6be326b-338a-4ad1-bc1b-265ae8c09a0a; stockx_dismiss_modal=true; stockx_dismiss_modal_set=2022-12-19T17%3A38%3A33.245Z; stockx_dismiss_modal_expiration=2023-12-19T17%3A38%3A33.245Z; language_code=en; stockx_preferred_market_activity=sales; stockx_product_visits=36; stockx_default_size=%7B%22sneakers%22%3A%228%22%2C%22shoes%22%3A%228%22%7D; pxcts=197cbb2c-8e8b-11ed-98b3-566c6459676c; stockx_selected_locale=en; stockx_selected_region=PL; stockx_selected_currency=EUR; __cf_bm=iEqTPbuk20oaDhCAOItmspDd3YAP75NPdfHqZw7BoEI-1673366051-0-Abh+Sg6p+V3fGgfA/aHBQnnJ08O6cjSj9o0UyR2DvuCazT1znSzEbrGTZWEhU5+AnXfVBue+ftUQV93JNDwY2xo="
        )
        headers.add("Upgrade-Insecure-Requests", "1")
        headers.add("Sec-Fetch-Dest", "document")
        headers.add("Sec-Fetch-Mode", "navigate")
        headers.add("Sec-Fetch-Site", "none")
        headers.add("Sec-Fetch-User", "?1")
        headers.add("If-None-Match", "W/yr7orbq44n56bw")
        headers.add("TE", "trailers")
        return headers
    }

    private fun parseToProductEntity(jsonObject: JSONObject): Product {
        val parentId = try {
            jsonObject.getString("parentId")
        } catch (ex: JSONException) {
            null
        }
        return when (parentId) {
            null -> parseToParentProduct(jsonObject)
            else -> parseToChildrenProduct(jsonObject)
        }
    }

    private fun parseToParentProduct(jsonObject: JSONObject): Product {
        val childrenObject = jsonObject.getJSONObject("children")
        val children = parseChildrenToProductList(childrenObject)
        return createProduct(
            jsonObject = jsonObject,
            children = children)
    }

    private fun parseToChildrenProduct(jsonObject: JSONObject): Product {
        val parentId = try {
            jsonObject.getString("parentId")
        } catch (ex: JSONException) {
            null
        }
        val shoeSize = try {
            jsonObject.getString("shoeSize")
        } catch (ex: JSONException) {
            null
        }
        return createProduct(
            jsonObject = jsonObject,
            parentId = parentId,
            shoeSize = shoeSize
        )
    }

    private fun parseChildrenToProductList(json: JSONObject): List<Product> {
        if (json.isEmpty) return emptyList()

        val childrenProductList = mutableListOf<Product>()
        for (key in json.keys()) {
            val child = json.getJSONObject(key)
            val parentId = child.getString("parentId")
            val shoeSize = child.getString("shoeSize")
            val childrenProduct = createProduct(child, parentId, shoeSize)
            childrenProductList.add(childrenProduct)
        }
        return childrenProductList.toList()
    }

    private fun createProduct(
        jsonObject: JSONObject,
        parentId: String? = null,
        shoeSize: String? = null,
        children: List<Product>? = null
    ): Product {
        val media = jsonObject.getJSONObject("media")
        val market = jsonObject.getJSONObject("market")
        return Product(
            uuid = UUID.fromString(jsonObject.getString("uuid")),
            title = jsonObject.getString("title"),
            brand = jsonObject.getString("brand"),
            colorway = jsonObject.getString("colorway"),
            styleId = jsonObject.getString("styleId"),
            gender = jsonObject.getString("gender"),
            releaseDate = jsonObject.getString("releaseDate"),
            retailPrice = jsonObject.getInt("retailPrice"),
            imageUrl = URL(media.getString("smallImageUrl")),
            lowestAsk = market.getInt("lowestAsk"),
            numberOfAsks = market.getInt("numberOfAsks"),
            highestBid = market.getInt("highestBid"),
            numberOfBids = market.getInt("numberOfBids"),
            deadstockSold = market.getInt("deadstockSold"),
            averageDeadstockPrice = market.getInt("averageDeadstockPrice"),
            totalDollars = market.getInt("totalDollars"),
            parentId = parentId,
            shoeSize = shoeSize,
            children = children
        )
    }
}