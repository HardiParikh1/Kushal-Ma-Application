package com.example.nutritracker;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecbScreen extends AppCompatActivity {

    private ListView ingredientsList, instructionsList;
    private Button  checkRecipeButton;
    private ImageButton videoButton;

    private WebView webView;

    private long startTime;

    private SharedPreferences sharedPreferences;
    private MediaEventDao mediaEventDao;

    private ExecutorService databaseExecutor;

    private AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_recb);

        db = AppDatabase.getDatabase(getApplicationContext());
        databaseExecutor = Executors.newSingleThreadExecutor();

        ingredientsList = findViewById(R.id.ingredientsList);
        instructionsList = findViewById(R.id.instructionsList);
        checkRecipeButton = findViewById(R.id.checkRecipeButton);
        videoButton = findViewById(R.id.videoButton);
        setupImageButton();


        sharedPreferences = getSharedPreferences("AppUsageStats", Context.MODE_PRIVATE);

        setupListAdapters();
        setupCheckRecipeButton();
        restorePreviousTime();
    }

    private void setupImageButton() {
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecbScreen.this, TrimesterVideo.class);
                startActivity(intent);
            }
        });
    }
    private void setupListAdapters() {
        String[] ingredients = {"1. Sore breasts\n" +
                "2. Nausea\n" +
                "3. Mood Swings\n" +
                "4. Feeling Tired, Skin Changes\n" +
                "5. Mild shortness of breath after exercises"};
        ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ingredients);
        ingredientsList.setAdapter(ingredientsAdapter);

        String[] instructions = {"1. A physical exam your weight and blood pressure.\n" +
                "2. A pelvic exam.\n" +
                "3. A Pap test\n" +
                "4. Tests to check for certain sexually transmitted infections.\n"+
                "5. Check your pee for bacteria, protein and glucose (sugar).\n"+
                "6. Check the fetal heart rate.\n"};
        ArrayAdapter<String> instructionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, instructions);
        instructionsList.setAdapter(instructionsAdapter);

    }
    private void setupCheckRecipeButton() {
        checkRecipeButton.setOnClickListener(v -> {
            // Launch the WebView
            setContentView(R.layout.webview);  // Switch to your WebView layout

            WebView webView = findViewById(R.id.website);
            webView.getSettings().setJavaScriptEnabled(true);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(request.getUrl().toString());
                    return true;
                }
            });

            String url = "https://www.webmd.com/baby/first-trimester-of-pregnancy#:~:text=The%20first%20trimester%20is%20the,both%20you%20and%20your%20baby.";
            webView.loadUrl(url);

            restorePreviousTime();
        });
    }

    private void restorePreviousTime() {
        startTime = sharedPreferences.getLong("TimeOnRecbScreen", System.currentTimeMillis());
    }
    @Override
    protected void onResume() {
        super.onResume();
        long previousDuration = sharedPreferences.getLong("TimeOnRecbScreen", 0);
        Toast.makeText(this, "Resuming timer from " + (previousDuration / 1000) + " seconds", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        long previousDuration = sharedPreferences.getLong("TimeOnRecbScreen", 0);
        long newDuration = previousDuration + duration;
        sharedPreferences.edit().putLong("TimeOnRecbScreen", newDuration).apply();

        Toast.makeText(this, "Total time spent on RecbScreen: " + (newDuration / 1000) + " seconds", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseExecutor.shutdown();
        // Additional cleanup if needed
    }
}