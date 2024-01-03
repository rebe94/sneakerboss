package com.example.sneakerboss.commonsfortesting

import org.mockito.Mockito

inline fun <reified T> any(type: Class<T>): T = Mockito.any(type)