package edu.northeastern.groupproject;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailedWeatherActivity extends AppCompatActivity {

    private TextView placeTextView; // Use this if you still want to show the place name
    private RecyclerView weatherInfoRecyclerView; // Add RecyclerView member


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_weather);

        placeTextView = findViewById(R.id.placeTextView);
        weatherInfoRecyclerView = findViewById(R.id.weatherInfoRecyclerView); // Initialize RecyclerView
        weatherInfoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("weatherData")) {
                String weatherData = intent.getStringExtra("weatherData");
                displayWeatherData(weatherData);
            }
            if (intent.hasExtra("placeName")) {
                String placeName = intent.getStringExtra("placeName");
                placeTextView.setText(placeName);
            }
        }
    }

    private void displayWeatherData(String data) {
        List<String> weatherDetails = new ArrayList<>();
        //-------------------------------------------------
        String iconUrl = getIconUrlFromIntent();

        if (iconUrl != null) {
            try {
                ImageView weatherIconImageView = findViewById(R.id.weatherIconImageView);
                Glide.with(this).load(iconUrl).into(weatherIconImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //--------------------------------------------------------------------------
        try {
            JSONObject jsonObject = new JSONObject(data);
            String condition = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            double tempKelvin = jsonObject.getJSONObject("main").getDouble("temp");
            double tempCelsius = tempKelvin - 273.15;


            // Adding parsed data to the list
            weatherDetails.add("Condition: " + condition);
            weatherDetails.add("Temperature: " + String.format("%.2f°C", tempCelsius));

            // More details can be added here as needed
        } catch (Exception e) {
            e.printStackTrace();
            weatherDetails.add("Error parsing weather data");
        }

        // Set the adapter with the list
        WeatherInfoAdapter adapter = new WeatherInfoAdapter(weatherDetails);
        weatherInfoRecyclerView.setAdapter(adapter);
    }
    //----------------------------------------------------
    private String getIconUrlFromIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("weatherIconUrl")) {
            return intent.getStringExtra("weatherIconUrl");
        }
        return null;
    }
    //------------------------------------------------------





}


