package edu.northeastern.groupproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LogWeightActivity extends AppCompatActivity {

    private TextView tvDate, tvWeight, tvBodyFat;
    private RadioGroup rgWeightUnit;
    private Button btnSaveWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_weight);

        tvDate = findViewById(R.id.tvDate);
        tvWeight = findViewById(R.id.tvWeight);
        tvBodyFat = findViewById(R.id.tvBodyFat);
        rgWeightUnit = findViewById(R.id.rgWeightUnit);
        btnSaveWeight = findViewById(R.id.btnSaveWeight);

        btnSaveWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveWeight();
            }
        });
    }

    private void saveWeight() {
        String date = tvDate.getText().toString();
        String weightInput = tvWeight.getText().toString();
        String bodyFat = tvBodyFat.getText().toString();
        boolean isKg = rgWeightUnit.getCheckedRadioButtonId() == R.id.rbKg;
        String weightUnit = isKg ? "kg" : "lbs";

        if(date.isEmpty() || weightInput.isEmpty() || bodyFat.isEmpty()) {
            return;
        }


        // Convert weight
        double weight = Double.parseDouble(weightInput);
        if (!isKg) {
            // If the user has selected LBS, convert to KG (optional)
            weight = convertLbsToKg(weight);
        }
        Log.d("LogWeightActivity", "Date: " + date + ", Weight: " + weight + " kg, Body Fat: " + bodyFat + "%");
    }

    private double convertLbsToKg(double lbs) {
        return lbs * 0.453592;
    }
}
