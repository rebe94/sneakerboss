package com.example.sneakerboss.pricecalculating

import org.springframework.stereotype.Component

@Component
class PriceCalculator {

    companion object {
        private const val TAX_FACTOR_PERCENTAGE = 0.95F
        private const val FIXED_AMOUNT_TO_SUBSTRACT = 2F
        private const val TRANSACTION_FEE_PERCENTAGE = 0.1F
        private const val PAYMENT_PERCENTAGE = 0.03F
        private const val SHIPPING_FEE = 5F
    }

    fun calculateLowestAskToBeFirst(amount: Float): Float {
        return amount * TAX_FACTOR_PERCENTAGE - FIXED_AMOUNT_TO_SUBSTRACT
    }

    fun calculatePayout(amount: Float): Float {
        return (amount - (amount * TRANSACTION_FEE_PERCENTAGE) - (amount * PAYMENT_PERCENTAGE) - SHIPPING_FEE)
    }
}

