package edu.northeastern.groupproject;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocationActivity extends AppCompatActivity implements LocationListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private TextView textViewLatitudeLongitude;
    private TextView textViewTotalDistance;
    private double totalDistance = 0.0;
    private Location previousLocation;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        textViewLatitudeLongitude = findViewById(R.id.textViewLatitudeLongitude);
        textViewTotalDistance = findViewById(R.id.textViewTotalDistance);
        Button btnResetDistance = findViewById(R.id.btnResetDistance);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Request location permissions if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted
            startLocationUpdates();
        }

        // Reset distance button click listener
        btnResetDistance.setOnClickListener(v -> {
            totalDistance = 0.0;
            textViewTotalDistance.setText(String.format("%.2f meters", totalDistance));
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        // Handle location change
        if (location != null) {
            // Update UI with new location
            updateLocation(location);
        }
    }

    private void startLocationUpdates() {
        // Request location updates
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }

    private void updateLocation(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        textViewLatitudeLongitude.setText("Latitude: " + latitude + ", Longitude: " + longitude);

        if (previousLocation != null) {
            float[] results = new float[1];
            Location.distanceBetween(previousLocation.getLatitude(), previousLocation.getLongitude(),
                    location.getLatitude(), location.getLongitude(), results);
            totalDistance += results[0];
            textViewTotalDistance.setText(String.format("%.2f meters", totalDistance));
        }

        previousLocation = location;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                // Permission denied, show alert and finish activity
                showPermissionDeniedAlert();
            }
        }
    }

    private void showPermissionDeniedAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Location permission is required to use this feature.")
                .setTitle("Permission Denied")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> finish());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Implement as needed
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Implement as needed
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Implement as needed
    }
}

