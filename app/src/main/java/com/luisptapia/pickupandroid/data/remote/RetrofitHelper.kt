package com.luisptapia.pickupandroid.data.remote

import com.luisptapia.pickupandroid.utils.Constants
import com.luisptapia.pickupandroid.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper(
    private val sessionManager: SessionManager,
    private val onUnauthorized: () -> Unit
) {

    val interceptor = HttpLoggingInterceptor().apply {
        level =
            HttpLoggingInterceptor.Level.BODY //respuesta al nivel del body en la operación de red
    }

    val client = OkHttpClient.Builder().apply {
        addInterceptor(interceptor)

    }.build()



    fun getRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(sessionManager, onUnauthorized)) // 👈 Your custom one
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}

class AuthInterceptor(
    private val sessionManager: SessionManager,
    private val onUnauthorized: () -> Unit
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = sessionManager.getToken()

        val request = if (token != null) {
            original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            original
        }

        val response = chain.proceed(request)

        if (response.code == 401 || response.code == 403) {
            sessionManager.logout()
            onUnauthorized()
        }

        return response
    }
}
