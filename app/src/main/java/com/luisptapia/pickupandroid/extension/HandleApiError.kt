package com.luisptapia.pickupandroid.extension

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit

fun <T> ResponseBody?.parseError(retrofit: Retrofit, clazz: Class<T>): T? {
    return this?.let {
        val converter: Converter<ResponseBody, T> =
            retrofit.responseBodyConverter(clazz, arrayOf())
        try {
            converter.convert(it)
        } catch (e: Exception) {
            null
        }
    }
}