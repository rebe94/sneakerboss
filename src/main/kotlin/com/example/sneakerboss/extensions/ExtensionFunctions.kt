package com.example.sneakerboss.extensions

import kotlin.math.pow
import org.json.JSONObject
import org.springframework.core.io.ClassPathResource

fun Float.round(precision: Int) = kotlin.math.round(this * (10.0F.pow(precision))) / (10.0F.pow(precision))

fun Double.round(precision: Int) = kotlin.math.round(this * (10.0.pow(precision))) / (10.0.pow(precision))

fun getTextFromResource(path: String): String = ClassPathResource(path).inputStream.bufferedReader().use { it.readText() }

fun String.substitute(substitutions: Map<String, String>) =
    substitutions.entries.fold(this) { replacedText, substitute -> replacedText.replaceWith(substitute) }

private fun String.replaceWith(substitute: Map.Entry<String, String>) =
    this.replace("```${substitute.key}```", substitute.value)

fun JSONObject.at(key: String) = this.getJSONObject(key)