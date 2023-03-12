package com.example.sneakerboss.extensions

import kotlin.math.pow

fun Float.round(precision: Int) = kotlin.math.round(this * (10.0F.pow(precision))) / (10.0F.pow(precision))

fun Double.round(precision: Int) = kotlin.math.round(this * (10.0.pow(precision))) / (10.0.pow(precision))