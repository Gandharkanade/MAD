package com.example.explicityintent;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnExplicit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnExplicit = findViewById(R.id.btnExplicit);

        btnExplicit.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondFragment.class);
            startActivity(intent);
        });
    }
}