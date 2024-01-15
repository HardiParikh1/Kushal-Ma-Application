package com.example.nutritracker;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class BreakfastScreen extends AppCompatActivity {

    private long startTime = 0;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializing SharedPreferences
        preferences = getSharedPreferences("AppUsageStats", Context.MODE_PRIVATE);

        // Setting the content view to your XML layout
        setContentView(R.layout.screen_breakfast);

        // Reference to the ImageButton
        ImageButton item1Button = findViewById(R.id.item_1);
        ImageButton item2Button = findViewById(R.id.item_2);
        ImageButton item3Button = findViewById(R.id.item_3);
        ImageButton item4Button = findViewById(R.id.item_4);
        ImageButton item5Button = findViewById(R.id.item_5);

        // Set OnClickListener to navigate to RecbScreen and print a message
        item1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Printing a message when the button is clicked
                Toast.makeText(BreakfastScreen.this, "User clicked on Greek Yogurt choice!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BreakfastScreen.this, RecbScreen.class);
                startActivity(intent);
            }
        });

        item2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Printing a message when the button is clicked
                Toast.makeText(BreakfastScreen.this, "User clicked on Avocado and Egg Toastie choice!", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(BreakfastScreen.this, b2Screen.class);
                startActivity(intent2);
            }
        });

        item3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Printing a message when the button is clicked
                Toast.makeText(BreakfastScreen.this, "User clicked on Vanilla Oats choice!", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(BreakfastScreen.this, b3Screen.class);
                startActivity(intent3);
            }
        });

        item4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Printing a message when the button is clicked
                Toast.makeText(BreakfastScreen.this, "User clicked on Yogurt Bowl with Fruits choice!", Toast.LENGTH_SHORT).show();
                Intent intent4 = new Intent(BreakfastScreen.this, b4Screen.class);
                startActivity(intent4);
            }
        });

        item5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Printing a message when the button is clicked
                Toast.makeText(BreakfastScreen.this, "User clicked on High Protein Savoury Oatmeal choice!", Toast.LENGTH_SHORT).show();
                Intent intent5 = new Intent(BreakfastScreen.this, b5Screen.class);
                startActivity(intent5);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        long previousDuration = preferences.getLong("TimeOnBreakfastScreen", 0);
        Toast.makeText(this, "Timer is starting at " + (previousDuration / 1000) + " seconds", Toast.LENGTH_SHORT).show();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        long endTime = System.currentTimeMillis();
        long durationMillis = endTime - startTime;
        long previousDuration = preferences.getLong("TimeOnBreakfastScreen", 0);
        long newDuration = previousDuration + durationMillis;
        preferences.edit().putLong("TimeOnBreakfastScreen", newDuration).apply();
        Toast.makeText(this, "Timer is ending at " + (newDuration / 1000) + " seconds", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Total time user spent on BreakfastScreen: " + (newDuration / 1000) + " seconds", Toast.LENGTH_SHORT).show();
    }
}
