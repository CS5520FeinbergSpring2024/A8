package edu.northeastern.groupproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class ForecastActivity extends AppCompatActivity {
    private List<Forecast> forecastList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ForecastAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        recyclerView = findViewById(R.id.forecastRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ForecastAdapter(forecastList);
        recyclerView.setAdapter(adapter);

        // Retrieve and parse the forecast data
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("forecastData")) {
            String forecastData = intent.getStringExtra("forecastData");
            parseForecastData(forecastData);
        }
    }

    private void parseForecastData(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray list = jsonObject.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject item = list.getJSONObject(i);
                String date = item.getString("dt_txt"); // Assuming you have a datetime field
                JSONObject main = item.getJSONObject("main");
                double temp = main.getDouble("temp");
                JSONArray weatherArray = item.getJSONArray("weather");
                String description = weatherArray.getJSONObject(0).getString("description");
                String iconCode = weatherArray.getJSONObject(0).getString("icon");

                Forecast forecast = new Forecast(date, temp, description, iconCode);
                forecastList.add(forecast);
            }
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
