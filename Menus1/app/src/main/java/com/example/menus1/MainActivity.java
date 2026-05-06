package com.example.menus1;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        // Enable Context Menu on TextView
        registerForContextMenu(textView);

        // Popup Menu on Button Click
        button.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(MainActivity.this, button);
            popup.getMenu().add("Option 1");
            popup.getMenu().add("Option 2");

            popup.setOnMenuItemClickListener(item -> {
                Toast.makeText(MainActivity.this,
                        item.getTitle() + " clicked",
                        Toast.LENGTH_SHORT).show();
                return true;
            });

            popup.show();
        });
    }

    // ------------------ OPTIONS MENU ------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_home) {
            Toast.makeText(this, "Home Selected", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.menu_settings) {
            Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    // ------------------ CONTEXT MENU ------------------
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Context Menu");
        menu.add(0, 1, 0, "Edit");
        menu.add(0, 2, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            Toast.makeText(this, "Edit Selected", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == 2) {
            Toast.makeText(this, "Delete Selected", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}