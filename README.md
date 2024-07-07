# WeatherAppProjekt

Welcome to WeatherAppProjekt! This is a simple weather application built using Jetpack Compose for Android. The app fetches weather data from the OpenWeather API and displays the current weather for a specified city.

## Features

- Enter the name of a city to get the current weather.
- Displays the city name, temperature (in Celsius), and a brief weather description.
- Shows a loading indicator while fetching data.

## Screenshots

![App Screenshot](screenshots/weather_app_screenshot.png) <!-- Placeholder for actual screenshot -->

## How to Run

1. **Clone the repository:**
   ```sh
   git clone https://github.com/yourusername/WeatherAppProjekt.git
   cd WeatherAppProjekt

   
Open the project in Android Studio:

Open Android Studio.
Select "Open an existing Android Studio project".
Navigate to the cloned repository and open it.
Build the project:

Make sure you have the latest version of Android Studio and the necessary SDKs installed.
Click on the "Build" menu and select "Make Project" or use the shortcut Ctrl+F9.
Run the app:

Connect an Android device or start an emulator.
Click the "Run" button or use the shortcut Shift+F10.
API Key Setup
To fetch weather data, you need to set up your own API key from RapidAPI's OpenWeatherMap.

Go to RapidAPI and create an account if you don't have one.
Search for "OpenWeatherMap" and subscribe to the API.
Replace the placeholder API key in the fetchWeather function with your own API key.

.addHeader("x-rapidapi-key", "YOUR_RAPIDAPI_KEY")
