// app/src/main/java/com/example/flight/viewmodel/FlightViewModel.kt
package com.example.flight.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flight.api.FlightApiService
import com.example.flight.data.Flight
import com.example.flight.repository.FlightRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class FlightViewModel : ViewModel() {
    private val repository = FlightRepository(FlightApiService.create())

    private val _flightData = MutableLiveData<List<Flight>>()
    val flightData: LiveData<List<Flight>> = _flightData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var trackingJob: Job? = null

    fun trackFlight(flightNumber: String) {
        if (flightNumber.isBlank()) {
            _errorMessage.value = "Please enter a valid flight number"
            return
        }

        // Stop previous tracking if any
        stopTracking()

        // Start new tracking
        trackingJob = viewModelScope.launch {
            while (isActive) {
                _isLoading.value = true
                try {
                    repository.getFlightInfo(flightNumber).fold(
                        onSuccess = { flights ->
                            _flightData.value = flights
                            if (flights.isEmpty()) {
                                _errorMessage.value = "No flight data found for $flightNumber"
                            }
                        },
                        onFailure = { error ->
                            _errorMessage.value = "Error: ${error.message}"
                        }
                    )
                } catch (e: Exception) {
                    _errorMessage.value = "Error: ${e.message}"
                } finally {
                    _isLoading.value = false
                }

                // Update every minute
                delay(60000)
            }
        }
    }

    fun stopTracking() {
        trackingJob?.cancel()
        trackingJob = null
    }

    override fun onCleared() {
        super.onCleared()
        stopTracking()
    }
}