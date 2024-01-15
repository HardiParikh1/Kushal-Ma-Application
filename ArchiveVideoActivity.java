package com.example.nutritracker;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class ArchiveVideoActivity extends AppCompatActivity {

    private WebView youtubeWebView;
    private TextView videoTitleTextView;
    private String videoId;

    private AppDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getDatabase(getApplicationContext());
        setContentView(R.layout.activity_archive_video);
        videoId = getIntent().getStringExtra("VIDEO_ID");


        youtubeWebView = findViewById(R.id.webview);
        videoTitleTextView = findViewById(R.id.videoTitle);



        // Get video URL and title passed from ArchiveActivity
        String videoUrl = getIntent().getStringExtra("VIDEO_URL");
        String videoTitle = getIntent().getStringExtra("VIDEO_TITLE");
        Log.d("ArchiveVideoActivity", "Received URL: " + videoUrl + ", Title: " + videoTitle);

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

        setupWebView();

        // Set the video title and load the video
        if (videoUrl != null && videoTitle != null) {
            videoTitleTextView.setText(videoTitle);
            loadYouTubeVideo(videoUrl);
        }
    }

    private void recordVideoInteraction(String action) {
        new Thread(() -> {
            VideoInteraction interaction = new VideoInteraction();
            interaction.videoId = this.videoId; // Use the video ID
            interaction.action = action;
            interaction.timestamp = System.currentTimeMillis();

            db.videoInteractionDao().insert(interaction);
        }).start();
    }

    private void setupWebView() {
        youtubeWebView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = youtubeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        youtubeWebView.addJavascriptInterface(new JavaScriptInterface(), "Android");
    }

    private void loadYouTubeVideo(String videoId) {
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
    }

    class JavaScriptInterface {
        @android.webkit.JavascriptInterface
        public void onVideoEnd() {
            // Handle video end event
        }
    }

    private void controlVideo(String command) {
        youtubeWebView.evaluateJavascript("javascript:" + command + "();", null);
    }

}
