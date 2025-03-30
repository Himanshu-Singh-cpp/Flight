// app/src/main/java/com/example/flight/repository/FlightRepository.kt
package com.example.flight.repository

import com.example.flight.api.FlightApiService
import com.example.flight.data.Flight

class FlightRepository(private val apiService: FlightApiService) {

    suspend fun getFlightInfo(flightNumber: String): Result<List<Flight>> {
        return try {
            val response = apiService.getFlightData(FlightApiService.API_KEY, flightNumber)
            if (response.isSuccessful) {
                val flights = response.body()?.data ?: emptyList()
                Result.success(flights)
            } else {
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}