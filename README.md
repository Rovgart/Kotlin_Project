Weather App
Welcome to WeatherApp! This is a simple weather application built using Jetpack Compose for Android. The app fetches weather data from the OpenWeather API and displays the current weather for a specified city.

Features
Enter the name of a city to get the current weather.
Displays the city name, temperature (in Celsius), and a brief weather description.
Shows a loading indicator while fetching data.
Screenshots
 <!-- Placeholder for actual screenshot -->

How to Run
Clone the repository:

sh
Skopiuj kod
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
kotlin
Skopiuj kod
.addHeader("x-rapidapi-key", "YOUR_RAPIDAPI_KEY")
Code Overview
MainActivity
The MainActivity is the entry point of the application. It sets up the Compose content.

kotlin
Skopiuj kod
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp()
        }
    }
}
WeatherApp Composable
The WeatherApp composable function defines the UI of the application. It contains:

A title with a cloud icon.
An input field for the city name.
A button to fetch weather data.
Weather information display.
A loading indicator.
kotlin
Skopiuj kod
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherApp() {
    var city by remember { mutableStateOf("") }
    var weather by remember { mutableStateOf<WeatherResponse?>(null) }
    var loading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Title with cloud icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cloud),
                    contentDescription = "Cloud Icon",
                    modifier = Modifier
                        .size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "WeatherApp",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center,
                    color = Color(android.graphics.Color.parseColor("#02a9ea"))
                )
            }

            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = { Text("Enter city name") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(android.graphics.Color.parseColor("#02a9ea")),
                    unfocusedBorderColor = Color(android.graphics.Color.parseColor("#02a9ea"))
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        loading = true
                        delay(2000) // Simulate loading for 2 seconds
                        weather = fetchWeather(city)
                        loading = false
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(android.graphics.Color.parseColor("#02a9ea"))
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "Get Weather",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 1.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            weather?.let {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "City: ${it.name}",
                        fontSize = 18.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = "Temperature: ${it.main.temp.roundToInt()}°C",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = "Description: ${it.weather[0].description}",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Black)
            }
        }
    }
}
Fetch Weather Function
The fetchWeather function makes a network request to the OpenWeatherMap API to get weather data for the specified city.

kotlin
Skopiuj kod
suspend fun fetchWeather(city: String): WeatherResponse? {
    return withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://open-weather13.p.rapidapi.com/city/$city/EN")
                .get()
                .addHeader("x-rapidapi-key", "YOUR_RAPIDAPI_KEY")
                .addHeader("x-rapidapi-host", "open-weather13.p.rapidapi.com")
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                Log.e("fetchWeather", "Unexpected response code: ${response.code}:${response.networkResponse}")
                return@withContext null
            }

            val responseBody = response.body?.string() ?: return@withContext null
            val weatherResponse = Gson().fromJson(responseBody, WeatherResponse::class.java)

            // Convert Fahrenheit to Celsius
            val tempCelsius = ((weatherResponse.main.temp - 32) * 5 / 9).roundToInt()
            weatherResponse.copy(main = weatherResponse.main.copy(temp = tempCelsius.toFloat()))
        } catch (e: Exception) {
            Log.e("fetchWeather", "Error fetching weather", e)
            null
        }
    }
}
Dependencies
Jetpack Compose
OkHttp
Gson
Kotlin Coroutines
Ensure these dependencies are included in your build.gradle file.

gradle
Skopiuj kod
dependencies {
    implementation "androidx.activity:activity-compose:1.3.1"
    implementation "androidx.compose.ui:ui:1.0.1"
    implementation "androidx.compose.material3:material3:1.0.0-alpha02"
    implementation "com.squareup.okhttp3:okhttp:4.9.1"
    implementation "com.google.code.gson:gson:2.8.7"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1"
}
License
This project is licensed under the MIT License - see the LICENSE file for details.

Feel free to fork and contribute to the project. For any issues or feature requests, please open an issue on the GitHub repository. Enjoy coding!
