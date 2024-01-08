package com.example.sneakerboss.commonsfortesting

import com.example.sneakerboss.commons.productfetching.productmarkerdatafetching.ShoeVariant
import com.example.sneakerboss.productdetailsfetching.dto.ProductDetailsDto
import java.util.UUID

class ProductMarketDataProvider {

    companion object {

        const val FIXED_TOTAL_PAYOUT_VALUE = 1F

        fun getSubstitutionMapForExpectedProductMarketData() =
            getProductDataSubstitutions(getProductDetails()).plus(
                getShoeVariantSubstitutions(getFirstShoeVariant(), "FIRST_")
            ).plus(getShoeVariantSubstitutions(getSecondShoeVariant(), "SECOND_"))

        fun getProductDetails() =
            ProductDetailsDto(
                uuid = UUID.fromString("8d8fa98b-7777-441b-b927-917ac5d3b255"),
                title = "Nike Dunk Low Off-White Lot 31",
                numberOfAsks = 10,
                numberOfBids = 19,
                deadstockSold = 30,
                shoeVariants = listOf(getFirstShoeVariant(), getSecondShoeVariant())
            )

        fun getFirstShoeVariant() = ShoeVariant(
            uuid = UUID.fromString("22212eb0-3d53-4bdb-9077-679f0d013dfd"),
            size = "7.5",
            lowestAsk = 863,
            highestBid = 315,
            numberOfAsks = 1,
            numberOfBids = 3,
            askToBeFirst = 200,
            totalPayout = FIXED_TOTAL_PAYOUT_VALUE,
            totalPayoutPln = FIXED_TOTAL_PAYOUT_VALUE,
            salesLast72Hours = 2,
            deadstockSold = 1
        )

        private fun getSecondShoeVariant() = ShoeVariant(
            uuid = UUID.fromString("19ee5f88-55e0-4bdd-b855-1e9b3d2ba0b3"),
            size = "8",
            lowestAsk = 478,
            highestBid = 333,
            numberOfAsks = 6,
            numberOfBids = 4,
            askToBeFirst = 455,
            totalPayout = FIXED_TOTAL_PAYOUT_VALUE,
            totalPayoutPln = FIXED_TOTAL_PAYOUT_VALUE,
            salesLast72Hours = 5,
            deadstockSold = 16,
        )

        private fun getProductDataSubstitutions(productDetailsDto: ProductDetailsDto): Map<String, String> =
            mapOf(
                "UUID" to productDetailsDto.uuid.toString(),
                "TITLE" to productDetailsDto.title,
                "NUMBER_OF_ASKS" to productDetailsDto.numberOfAsks.toString(),
                "NUMBER_OF_BIDS" to productDetailsDto.numberOfBids.toString(),
                "DEADSTOCK_SOLD" to productDetailsDto.deadstockSold.toString()
            )

        private fun getShoeVariantSubstitutions(shoeVariant: ShoeVariant, keyPrefix: String = ""): Map<String, String> =
            mapOf(
                "${keyPrefix}SHOE_VARIANT_UUID" to shoeVariant.uuid.toString(),
                "${keyPrefix}SHOE_VARIANT_SIZE" to shoeVariant.size,
                "${keyPrefix}SHOE_VARIANT_LOWEST_ASK" to shoeVariant.lowestAsk.toString(),
                "${keyPrefix}SHOE_VARIANT_HIGHEST_BID" to shoeVariant.highestBid.toString(),
                "${keyPrefix}SHOE_VARIANT_NUMBER_OF_ASKS" to shoeVariant.numberOfAsks.toString(),
                "${keyPrefix}SHOE_VARIANT_NUMBER_OF_BIDS" to shoeVariant.numberOfBids.toString(),
                "${keyPrefix}SHOE_VARIANT_ASK_TO_BE_FIRST" to shoeVariant.askToBeFirst.toString(),
                "${keyPrefix}SHOE_VARIANT_TOTAL_PAYOUT" to shoeVariant.totalPayout.toString(),
                "${keyPrefix}SHOE_VARIANT_TOTAL_PAYOUT_PLN" to shoeVariant.totalPayoutPln.toString(),
                "${keyPrefix}SHOE_VARIANT_SALES_LAST_72_HOURS" to shoeVariant.salesLast72Hours.toString(),
                "${keyPrefix}SHOE_VARIANT_DEADSTOCK_SOLD" to shoeVariant.deadstockSold.toString()
            )
    }
}