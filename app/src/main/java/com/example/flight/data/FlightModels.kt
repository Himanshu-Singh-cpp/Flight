// app/src/main/java/com/example/flight/data/FlightModels.kt
package com.example.flight.data

data class FlightResponse(
    val pagination: Pagination?,
    val data: List<Flight>?
)

data class Pagination(
    val limit: Int,
    val offset: Int,
    val count: Int,
    val total: Int
)

data class Flight(
    val flight_date: String?,
    val flight_status: String?,
    val departure: FlightLocation?,
    val arrival: FlightLocation?,
    val airline: Airline?,
    val flight: FlightDetails?,
    val live: LiveData?
)

data class FlightLocation(
    val airport: String?,
    val timezone: String?,
    val iata: String?,
    val icao: String?,
    val terminal: String?,
    val gate: String?,
    val delay: Int?,
    val scheduled: String?,
    val estimated: String?,
    val actual: String?,
    val estimated_runway: String?,
    val actual_runway: String?
)

data class Airline(
    val name: String?,
    val iata: String?,
    val icao: String?
)

data class FlightDetails(
    val number: String?,
    val iata: String?,
    val icao: String?
)

data class LiveData(
    val updated: String?,
    val latitude: Double?,
    val longitude: Double?,
    val altitude: Double?,
    val direction: Double?,
    val speed_horizontal: Double?,
    val speed_vertical: Double?,
    val is_ground: Boolean?
)