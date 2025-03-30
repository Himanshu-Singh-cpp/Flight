// app/src/main/java/com/example/flight/api/FlightApiService.kt
package com.example.flight.api

import com.example.flight.data.FlightResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface FlightApiService {
    @GET("flights")
    suspend fun getFlightData(
        @Query("access_key") accessKey: String,
        @Query("flight_iata") flightNumber: String
    ): Response<FlightResponse>

    companion object {
        private const val BASE_URL = "http://api.aviationstack.com/v1/"
        const val API_KEY = "e369a74d2a1b0cc6948a5fbbe7aa1f17" // Replace with your actual API key

        fun create(): FlightApiService {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FlightApiService::class.java)
        }
    }
}