package com.example.nutritracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import java.util.List;

public class CurrentVideoActivity extends AppCompatActivity {

    private WebView youtubeWebView;
    private List<String> videoUrls;
    private int currentVideoIndex = 0;
    private AppDatabase db;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_video);

        db = AppDatabase.getDatabase(getApplicationContext());

        youtubeWebView = findViewById(R.id.webview);
        setupWebView();

        videoUrls = getIntent().getStringArrayListExtra("VIDEO_URL_LIST");
        assert videoUrls != null;

        SharedPreferences sharedPreferences = getSharedPreferences("VideoPrefs", MODE_PRIVATE);
        currentVideoIndex = sharedPreferences.getInt("lastVideoIndex", 0);

        if (videoUrls != null && currentVideoIndex < videoUrls.size()) {
            loadYouTubeVideo(videoUrls.get(currentVideoIndex));
        }

        findViewById(R.id.btnPlay).setOnClickListener(v -> {
            controlVideo("playVideo");
            recordVideoInteraction("play");
        });
        findViewById(R.id.btnPause).setOnClickListener(v -> {
            controlVideo("pauseVideo");
            recordVideoInteraction("pause");
        });
        findViewById(R.id.btnStop).setOnClickListener(v -> {
            controlVideo("stopVideo");
            recordVideoInteraction("stop");
        });
        findViewById(R.id.btnBack).setOnClickListener(v -> goToPreviousVideo());
        findViewById(R.id.btnNext).setOnClickListener(v -> playNextVideo());

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").build();

    }

    private void setupWebView() {
        youtubeWebView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = youtubeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        youtubeWebView.addJavascriptInterface(new JavaScriptInterface(), "Android");
    }

    private void recordVideoInteraction(String action) {
        new Thread(() -> {
            VideoInteraction interaction = new VideoInteraction();
            interaction.videoId = videoUrls.get(currentVideoIndex);
            interaction.action = action;
            interaction.timestamp = System.currentTimeMillis();

            // Logging the result of the insert operation
            long result = db.videoInteractionDao().insert(interaction);
            Log.d("DatabaseOperation", "Insert result: " + result);
        }).start();
    }


    private void loadYouTubeVideo(String videoId) {
        SharedPreferences sharedPreferences = getSharedPreferences("VideoPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("lastVideoIndex", currentVideoIndex);
        editor.apply();

        String frameVideo = "<html>" +
                "<head>" +
                "<style type=\"text/css\">" +
                "body {" +
                "  margin: 0;" +
                "  padding: 0;" +
                "  display: flex;" +
                "  justify-content: center;" +
                "  align-items: center;" +
                "  height: 100vh;" +
                "}" +
                "iframe {" +
                "  width: 100%;" +
                "  height: 100%;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<iframe id=\"player\" src=\"https://www.youtube.com/embed/" + videoId +
                "?enablejsapi=1\" frameborder=\"0\" allowfullscreen></iframe>" +
                "<script src=\"https://www.youtube.com/iframe_api\"></script>" +
                "<script>" +
                "var player;" +
                "function onYouTubeIframeAPIReady() {" +
                "  player = new YT.Player('player', {" +
                "    events: {" +
                "      'onStateChange': onPlayerStateChange" +
                "    }" +
                "  });" +
                "}" +
                "function onPlayerStateChange(event) {" +
                "  if (event.data == YT.PlayerState.ENDED) {" +
                "    Android.onVideoEnd();" +
                "  }" +
                "}" +
                "function playVideo() { player.playVideo(); }" +
                "function pauseVideo() { player.pauseVideo(); }" +
                "function stopVideo() { player.pauseVideo(); player.seekTo(0); }" +
                "</script>" +
                "</body></html>";

        youtubeWebView.loadData(frameVideo, "text/html", "utf-8");

        Log.d("VideoActivity", "Saved index: " + currentVideoIndex);
    }

    private void playNextVideo() {
        currentVideoIndex++;
        if (currentVideoIndex < videoUrls.size()) {
            saveCurrentVideoIndex();
            loadYouTubeVideo(videoUrls.get(currentVideoIndex));
        } else {
            finish(); // or restart the playlist
        }
    }

    private void goToPreviousVideo() {
        if (currentVideoIndex > 0) {
            currentVideoIndex--;
            saveCurrentVideoIndex();
            loadYouTubeVideo(videoUrls.get(currentVideoIndex));
        } else {
            // Handle the case when there are no previous videos
        }
    }

    private void saveCurrentVideoIndex() {
        SharedPreferences sharedPreferences = getSharedPreferences("VideoPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("lastVideoIndex", currentVideoIndex);
        editor.apply();
    }

    class JavaScriptInterface {
        @android.webkit.JavascriptInterface
        public void onVideoEnd() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    playNextVideo();
                }
            });
        }
    }

    private void controlVideo(String command) {
        youtubeWebView.evaluateJavascript("javascript:" + command + "();", null);
    }
}
