package com.example.sneakerboss.productfetching

import java.util.*

interface ProductFetchable {

    fun findProductBy(uuid: UUID): Product?
}