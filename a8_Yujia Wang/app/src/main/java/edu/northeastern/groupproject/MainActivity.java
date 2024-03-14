package edu.northeastern.groupproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import android.util.Log;


//import com.bumptech.glide.Glide;
//import com.squareup.picasso.Picasso;

import org.json.JSONArray;


public class MainActivity extends AppCompatActivity {

    private EditText etCityName;
    private TextView tvWeatherInfo;
    private ProgressBar progressBar;

    private Button btnA7;

    private Button btnAbout;

    private Button btnLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        etCityName = findViewById(R.id.etCityName);
        Button btnFetchWeather = findViewById(R.id.btnFetchWeather);
        Button btnFetchForecast = findViewById(R.id.btnFetchForecast); // Add button in your layout
        tvWeatherInfo = findViewById(R.id.tvWeatherInfo);

        btnAbout = findViewById(R.id.btnAbout);

        btnLocation = findViewById(R.id.btnLocation);

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LocationActivity.class));
            }
        });

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, About.class));
            }
        });

        btnA7 = findViewById(R.id.btnA7);

        btnA7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the new activity here
                Intent intent = new Intent(MainActivity.this, edu.northeastern.groupproject.LoginActivity.class); // Replace YourNewActivity with the actual class name of your new activity
                startActivity(intent);
            }
        });

        btnFetchWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = etCityName.getText().toString();
                fetchWeatherData(city);
            }
        });

        btnFetchForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = etCityName.getText().toString();
                fetchForecastData(city); // Fetch forecast data
            }
        });
    }

    private void fetchWeatherData(String city) {
        runOnUiThread(() -> progressBar.setVisibility(View.VISIBLE));

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String encodedCityName = URLEncoder.encode(city, "UTF-8");
                    URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=16da9d7c3541c3b512b84ece90f86781");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();


                        // 获取图标 URL
                        String iconUrl = getWeatherIconUrl(stringBuilder.toString());

                        // Introduce a delay of 5 seconds
                        try {
                            Thread.sleep(5000); //  pause 5 s
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(() -> {
                            Intent intent = new Intent(MainActivity.this, DetailedWeatherActivity.class);
                            intent.putExtra("weatherData", stringBuilder.toString()); // Pass the weather data
                            intent.putExtra("placeName", city); // Also pass the city name
                            intent.putExtra("weatherIconUrl", iconUrl); // Pass the icon URL
                            startActivity(intent);
                        });

                    } finally {
                        urlConnection.disconnect();
                        runOnUiThread(() -> progressBar.setVisibility(View.GONE));

                    }
                } catch (Exception e) {
                    runOnUiThread(() -> tvWeatherInfo.setText("Failed to fetch data"));
                    e.printStackTrace();
                }finally {
                    runOnUiThread(() -> progressBar.setVisibility(View.GONE)); // Hide progress bar after fetching data
                }
            }
        }).start();
    }

    private void fetchForecastData(String city) {
        new Thread(() -> {
            try {
                // Use URLEncoder to handle cities with spaces and special characters
                String encodedCityName = URLEncoder.encode(city, "UTF-8");
                // Update the API call to use the correct endpoint
                URL url = new URL("https://api.openweathermap.org/data/2.5/forecast?q=" + encodedCityName + "&units=imperial&appid=16da9d7c3541c3b512b84ece90f86781");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    String forecastData = stringBuilder.toString();
                    // Log the HTTP response code for debugging
                    int responseCode = urlConnection.getResponseCode();
                    Log.d("MainActivity", "Forecast fetch HTTP response code: " + responseCode);

                    // Handle the forecast data
                    runOnUiThread(() -> updateForecastUI(forecastData));
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("MainActivity", "Failed to fetch forecast data", e);
                runOnUiThread(() -> tvWeatherInfo.setText("Failed to fetch forecast data"));
            }
        }).start();
    }


    private void updateForecastUI(String forecastData) {
        Intent intent = new Intent(MainActivity.this, ForecastActivity.class);
        intent.putExtra("forecastData", forecastData); // Pass the forecast data to ForecastActivity
        startActivity(intent);
    }

    private void parseWeatherData(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            // Extract data as needed, e.g., temperature, weather condition, etc.
            // Update the UI with the fetched data
            String weatherInfo = "Example Weather Info: " + jsonObject.toString(); // Simplified, replace with actual parsing.
            tvWeatherInfo.setText(weatherInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //---------------------------------------
    private String getWeatherIconUrl(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray weatherArray = jsonObject.getJSONArray("weather");
            if (weatherArray.length() > 0) {
                String iconCode = weatherArray.getJSONObject(0).getString("icon");
                return "https://api.openweathermap.org/img/w/" + iconCode + ".png";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}


