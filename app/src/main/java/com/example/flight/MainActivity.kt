// app/src/main/java/com/example/flight/MainActivity.kt
package com.example.flight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flight.data.Flight
import com.example.flight.ui.theme.FlightTheme
import com.example.flight.viewmodel.FlightViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlightTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    FlightTrackerApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FlightTrackerApp(modifier: Modifier = Modifier) {
    val viewModel: FlightViewModel = viewModel()
    val flightData by viewModel.flightData.observeAsState(emptyList())
    val errorMessage by viewModel.errorMessage.observeAsState("")
    val isLoading by viewModel.isLoading.observeAsState(false)

    var flightNumber by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Flight Tracker",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = flightNumber,
            onValueChange = { flightNumber = it },
            label = { Text("Flight Number (e.g. BA123)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    viewModel.trackFlight(flightNumber)
                }
            ),
            trailingIcon = {
                IconButton(onClick = {
                    keyboardController?.hide()
                    viewModel.trackFlight(flightNumber)
                }) {
                    Icon(Icons.Filled.Search, contentDescription = "Track Flight")
                }
            }
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (flightData.isNotEmpty()) {
            Text(
                text = "Flight Information",
                style = MaterialTheme.typography.titleLarge
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(flightData) { flight ->
                    FlightInfoCard(flight)
                }
            }
        }
    }
}

@Composable
fun FlightInfoCard(flight: Flight) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            flight.flight?.let {
                Text(
                    text = "Flight: ${flight.airline?.name ?: ""} ${it.iata ?: it.number}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Text(
                text = "Status: ${flight.flight_status ?: "Unknown"}",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Departure",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = flight.departure?.airport ?: "Unknown",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = formatTime(flight.departure?.scheduled),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Column {
                    Text(
                        text = "Arrival",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = flight.arrival?.airport ?: "Unknown",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = formatTime(flight.arrival?.scheduled),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            flight.live?.let { live ->
                Text(
                    text = "Live Information",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "Last updated: ${formatTime(live.updated)}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Position: Lat ${live.latitude}, Long ${live.longitude}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Altitude: ${live.altitude ?: "Unknown"} ft",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Speed: ${live.speed_horizontal ?: "Unknown"} km/h",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = if (live.is_ground == true) "On ground" else "In air",
                    style = MaterialTheme.typography.bodyMedium
                )
            } ?: Text("Live tracking information not available")
        }
    }
}

fun formatTime(timeString: String?): String {
    if (timeString == null) return "Unknown"
    try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        val outputFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.US)
        val date = inputFormat.parse(timeString)
        return date?.let { outputFormat.format(it) } ?: "Unknown"
    } catch (e: Exception) {
        return timeString
    }
}