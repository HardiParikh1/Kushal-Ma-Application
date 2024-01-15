package com.example.nutritracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class b5Screen extends AppCompatActivity{

    private VideoView videoView;
    private MediaPlayer audioPlayer;
    private ListView ingredientsList, instructionsList;
    private Button playAudioButton, pauseAudioButton, stopAudioButton, checkRecipeButton;
    private WebView webView;

    private long startTime;
    private long elapsedTime;
    private int videoPosition = 0;
    private int audioPosition = 0;

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_b5);

        videoView = findViewById(R.id.videoView5);
        ingredientsList = findViewById(R.id.ingredientsList5);
        instructionsList = findViewById(R.id.instructionsList5);
        playAudioButton = findViewById(R.id.playAudioButton5);
        pauseAudioButton = findViewById(R.id.pauseAudioButton5);
        stopAudioButton = findViewById(R.id.stopAudioButton5);
        checkRecipeButton = findViewById(R.id.checkRecipeButton5);

        sharedPreferences = getSharedPreferences("AppUsageStats", Context.MODE_PRIVATE);


        // Sample video URL. You can replace this with your own.
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);
        videoView.setVideoURI(videoUri);
        //videoView.start();
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);


        // Sample ingredients and instructions lists. You can replace these with your own.
        String[] ingredients = {"1. 1 cup of Greek yogurt (or your favorite yogurt)\n" +
                "2. 1/2 cup of mixed berries (strawberries, blueberries, raspberries)\n" +
                "3. 1 ripe banana, sliced\n" +
                "4. 1/4 cup of granola\n" +
                "5. 1 tablespoon of honey or maple syrup (optional)\n"+
                "6. A sprinkle of chia seeds or flaxseeds (optional)"};
        ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ingredients);
        ingredientsList.setAdapter(ingredientsAdapter);

        String[] instructions = {"1. Spoon the Greek yogurt into a bowl, spreading it evenly.\n" +
                "2. Wash and prepare the mixed berries. Add them on top of the yogurt.\n" +
                "3. Slice a ripe banana and arrange the slices in the bowl. Sprinkle granola over the yogurt and fruit for a crunchy texture.\n" +
                "4. For a touch of sweetness, drizzle honey or maple syrup over the bowl.\n " +
                "5. Boost the nutritional content by adding a sprinkle of chia seeds or flaxseeds.\n" +
                "6. Gently mix the ingredients in the bowl to combine flavors and textures. Enjoy your delicious and nutritious fruit yogurt bowl immediately."};
        ArrayAdapter<String> instructionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, instructions);
        instructionsList.setAdapter(instructionsAdapter);


        playAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioPlayer == null) {
                    audioPlayer = MediaPlayer.create(b5Screen.this, R.raw.audio);
                    audioPlayer.seekTo(sharedPreferences.getInt("SavedAudioPosition", 0));
                    audioPlayer.start();
                } else if (!audioPlayer.isPlaying()) {
                    audioPlayer.seekTo(sharedPreferences.getInt("SavedAudioPosition", 0));
                    audioPlayer.start();
                }
            }
        });

        pauseAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioPlayer != null && audioPlayer.isPlaying()) {
                    audioPlayer.pause();
                    audioPosition = audioPlayer.getCurrentPosition();
                    sharedPreferences.edit().putInt("SavedAudioPosition", audioPosition).apply();
                }
            }
        });

        stopAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioPlayer != null && audioPlayer.isPlaying()) {
                    audioPlayer.pause();
                    audioPosition = audioPlayer.getCurrentPosition();
                    sharedPreferences.edit().putInt("SavedAudioPosition", audioPosition).apply();
                }
            }
        });

        checkRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Launch the WebView
                setContentView(R.layout.webview);  // Switch to your WebView layout

                webView = findViewById(R.id.website);
                webView.getSettings().setJavaScriptEnabled(true);

                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        view.loadUrl(request.getUrl().toString());
                        return true;
                    }
                });

                String url = "https://pinchofwellness.com/greek-yogurt-bowl/#:~:text=1%20Greek%20Yogurt%20Bowl%20contains%20450%20calories%20and%2024%20grams%20of%20protein.";
                webView.loadUrl(url);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        int savedVideoPosition = sharedPreferences.getInt("SavedVideoPosition", 0);
        int savedAudioPosition = sharedPreferences.getInt("SavedAudioPosition", 0);

        videoView.seekTo(savedVideoPosition);


        if (audioPlayer != null) {
            audioPlayer.seekTo(savedAudioPosition);
        }
        startTime = System.currentTimeMillis();

        long previousDuration =sharedPreferences.getLong("TimeOnRecbScreen", 0);
        Toast.makeText(b5Screen.this, "Timer is starting at " + (previousDuration / 1000) + " seconds", Toast.LENGTH_SHORT).show();
        Toast.makeText(b5Screen.this, "Video is starting at " + (savedVideoPosition / 1000) + " seconds", Toast.LENGTH_SHORT).show();
        Toast.makeText(b5Screen.this, "Audio is starting at " + (savedAudioPosition / 1000) + " seconds", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        videoPosition = videoView.getCurrentPosition();
        sharedPreferences.edit().putInt("SavedVideoPosition", videoPosition).apply();

        if (audioPlayer != null) {
            audioPosition = audioPlayer.getCurrentPosition();
            sharedPreferences.edit().putInt("SavedAudioPosition", audioPosition).apply();
            audioPlayer.pause();
        }

        long endTime = System.currentTimeMillis();
        long durationMillis = endTime - startTime;
        long previousDuration = sharedPreferences.getLong("TimeOnRecbScreen", 0);
        long newDuration = previousDuration + durationMillis;
        sharedPreferences.edit().putLong("TimeOnRecbScreen", newDuration).apply();

        Toast.makeText(b5Screen.this, "Timer is ending at " + (newDuration / 1000) + " seconds", Toast.LENGTH_SHORT).show();
        Toast.makeText(b5Screen.this, "Total time user spent on RecbScreen: " + (newDuration / 1000) + " seconds", Toast.LENGTH_SHORT).show();
        Toast.makeText(b5Screen.this, "Video is ending at " + (videoPosition / 1000) + " seconds", Toast.LENGTH_SHORT).show();
        if (audioPlayer != null) {
            Toast.makeText(b5Screen.this, "Audio is ending at " + (audioPosition / 1000) + " seconds", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioPlayer != null) {
            audioPlayer.release();
            audioPlayer = null;
        }
    }
}
