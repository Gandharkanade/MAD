package com.example.inputcontrol;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText etName;
    RadioGroup radioGroup;
    CheckBox cbMusic, cbSports, cbDrawing, cbDance;
    Spinner spCity;
    ToggleButton toggleSubscribe;
    Switch switchNotify;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        radioGroup = findViewById(R.id.radioGroup);
        cbMusic = findViewById(R.id.cbMusic);
        cbSports = findViewById(R.id.cbSports);
        cbDrawing = findViewById(R.id.cbDrawing);
        cbDance = findViewById(R.id.cbDance);
        spCity = findViewById(R.id.spCity);
        toggleSubscribe = findViewById(R.id.toggleSubscribe);
        switchNotify = findViewById(R.id.switchNotify);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Spinner data
        String[] cities = {"Pune", "Mumbai", "Delhi", "Bangalore"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                cities);
        spCity.setAdapter(adapter);

        btnSubmit.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();

            // gender safe
            int selectedId = radioGroup.getCheckedRadioButtonId();
            String gender = "Not selected";
            if (selectedId != -1) {
                RadioButton rb = findViewById(selectedId);
                gender = rb.getText().toString();
            }

            // hobbies
            StringBuilder hobbies = new StringBuilder();
            if (cbMusic.isChecked()) hobbies.append("Music ");
            if (cbSports.isChecked()) hobbies.append("Sports ");
            if (cbDrawing.isChecked()) hobbies.append("Drawing ");
            if (cbDance.isChecked()) hobbies.append("Dance ");

            // spinner
            String city = spCity.getSelectedItem().toString();

            // toggle
            String subscription = toggleSubscribe.isChecked()
                    ? "Subscribed"
                    : "Not Subscribed";

            // switch
            String notify = switchNotify.isChecked()
                    ? "Enabled"
                    : "Disabled";

            Toast.makeText(
                    MainActivity.this,
                    "Name: " + name +
                            "\nGender: " + gender +
                            "\nHobbies: " + hobbies +
                            "\nCity: " + city +
                            "\nSubscription: " + subscription +
                            "\nNotifications: " + notify,
                    Toast.LENGTH_LONG
            ).show();
        });
    }
}