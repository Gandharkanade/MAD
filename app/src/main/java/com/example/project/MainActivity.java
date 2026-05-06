package com.example.project;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText etCity;
    private TextView tvCityName, tvTemp, tvDesc, tvHumidity, tvFeelsLike;
    private ImageButton btnGet;
    private Button btnCurrentLocation;
    private WebView mapWebView;

    private FusedLocationProviderClient fusedLocationClient;
    private final String apiKey = "6238e37482ee2a1b0cedb56fa42b8a66";
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI
        etCity = findViewById(R.id.etCity);
        btnGet = findViewById(R.id.btnGet);
        btnCurrentLocation = findViewById(R.id.btnCurrentLocation);
        tvCityName = findViewById(R.id.tvCityName);
        tvTemp = findViewById(R.id.tvTemp);
        tvDesc = findViewById(R.id.tvDesc);
        tvHumidity = findViewById(R.id.tvHumidity);
        tvFeelsLike = findViewById(R.id.tvFeelsLike);
        mapWebView = findViewById(R.id.mapWebView);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Map Setup
        WebSettings webSettings = mapWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mapWebView.setWebViewClient(new WebViewClient());

        // Button Listeners
        btnGet.setOnClickListener(v -> {
            String city = etCity.getText().toString().trim();
            if (!city.isEmpty()) fetchWeatherByCity(city);
            else Toast.makeText(this, "Enter city name", Toast.LENGTH_SHORT).show();
        });

        btnCurrentLocation.setOnClickListener(v -> getLastLocation());

        // Default: Get current location on startup
        getLastLocation();
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                fetchWeatherByCoords(location.getLatitude(), location.getLongitude());
            } else {
                Toast.makeText(MainActivity.this, "Could not detect location. Is GPS on?", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchWeatherByCity(String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey + "&units=metric";
        executeWeatherRequest(url);
    }

    private void fetchWeatherByCoords(double lat, double lon) {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + apiKey + "&units=metric";
        executeWeatherRequest(url);
    }

    private void executeWeatherRequest(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject main = response.getJSONObject("main");
                        JSONObject coord = response.getJSONObject("coord");
                        String temp = main.getString("temp");
                        String description = response.getJSONArray("weather").getJSONObject(0).getString("description");

                        updateUI(response.getString("name"),
                                temp,
                                description,
                                main.getString("humidity"),
                                main.getString("feels_like"),
                                coord.getDouble("lat"),
                                coord.getDouble("lon"));

                    } catch (JSONException e) { e.printStackTrace(); }
                }, error -> Toast.makeText(this, "City not found", Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(request);
    }

    private void updateUI(String name, String temp, String desc, String hum, String feels, double lat, double lon) {
        tvCityName.setText(name);
        tvTemp.setText(temp + "°C");
        tvDesc.setText(desc);
        tvHumidity.setText("Humidity: " + hum + "%");
        tvFeelsLike.setText("Feels: " + feels + "°C");

        double zoom = 0.05;
        String mapUrl = "https://www.openstreetmap.org/export/embed.html?bbox=" +
                (lon - zoom) + "," + (lat - zoom) + "," + (lon + zoom) + "," + (lat + zoom) +
                "&layer=mapnik&marker=" + lat + "," + lon;
        mapWebView.loadUrl(mapUrl);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        }
    }
}