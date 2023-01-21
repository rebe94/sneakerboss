package com.example.sneakerboss.productfetching.components

import java.util.*

interface ProductFetchable {

    fun findProductBy(uuid: UUID): Product?
}