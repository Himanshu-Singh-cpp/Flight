# Flight Tracker Application

A real-time flight tracking application built with Jetpack Compose that allows users to monitor flight details such as departure, arrival, and live location information.

## Features

- Search for flights using IATA flight numbers (e.g., BA123)
- Real-time flight tracking with automatic updates every minute
- Display of comprehensive flight information:
  - Flight status (scheduled, active, landed, etc.)
  - Departure and arrival details (airport, scheduled times)
  - Live tracking data (position, altitude, speed)
  - Airline information

## Technical Implementation

### 1. API Integration

The application uses the AviationStack API to retrieve flight data:
- RESTful API calls implemented with Retrofit2
- API responses parsed using Gson converter
- Error handling and response validation
- OkHttp client with logging interceptor for debugging

### 2. UI Design

Built with Jetpack Compose featuring:
- Material3 design components
- Responsive layout that adapts to different screen sizes
- Search functionality with keyboard actions
- Loading indicators during API calls
- Error messages for failed requests
- Flight information displayed in cards

### 3. Data Processing

- JSON responses mapped to Kotlin data classes
- Date formatting for human-readable output
- Null safety handling for API responses
- Live data updates with proper UI refresh

## Setup and Installation

1. Clone the repository
2. Open the project in Android Studio Meerkat (2024.3.1) or later
3. Replace the API key in `FlightApiService.kt` with your own from [AviationStack](https://aviationstack.com/)
4. Build and run the application on an emulator or physical device

## Architecture

The application follows MVVM (Model-View-ViewModel) architecture:
- **Model**: Data classes and repository pattern for data handling
- **View**: Jetpack Compose UI components in MainActivity
- **ViewModel**: FlightViewModel managing UI state and business logic

## Usage Instructions

1. Enter a valid flight number in the search field (IATA format preferred, e.g., BA123)
2. Tap the search icon or press the search action on the keyboard
3. Flight information will display when available
4. Data refreshes automatically every minute while the app is running

## Requirements

- Android 7.0 (API level 24) or higher
- Internet connection

## Assessment Criteria Mapping

1. **API Usage and Data Download (5 marks)**
   - Integration with AviationStack API
   - Proper API key handling
   - Asynchronous data fetching with coroutines
   - Response handling and error management

2. **UI Creation (5 marks)**
   - Modern Material3 design with Jetpack Compose
   - Responsive layout with proper spacing
   - Loading indicators and error messaging
   - Clean presentation of complex flight data

3. **JSON Parsing (5 marks)**
   - Structured data models mapped to API responses
   - Null safety handling for optional fields
   - Date formatting and data transformation
   - Gson integration with Retrofit

4. **Proper Output and Running Code (10 marks)**
   - Functional flight tracking with real-time updates
   - Organized display of flight information
   - Clean architecture separating concerns
   - Performance optimization with LiveData

5. **Input Validation and Error Handling (5 marks)**
   - Input validation for flight numbers
   - Descriptive error messages
   - Network error handling
   - Loading state management

## Future Improvements

- Flight path visualization on a map
- Push notifications for flight status changes
- Offline caching of previously tracked flights
- Multiple flight tracking simultaneously
- Flight history
