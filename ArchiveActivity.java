package com.example.nutritracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ArchiveActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archives);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadVideos();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadVideos() {
        new AsyncTask<Void, Void, List<Video>>() {
            @Override
            protected List<Video> doInBackground(Void... voids) {
                return AppDatabase.getDatabase(getApplicationContext()).videoDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Video> videos) {
                videoAdapter = new VideoAdapter(videos, video -> {
                    Log.d("ArchiveActivity", "Selected Video URL: " + video.url + ", Title: " + video.title);
                    Intent intent = new Intent(ArchiveActivity.this, ArchiveVideoActivity.class);
                    intent.putExtra("VIDEO_URL", video.url);
                    intent.putExtra("VIDEO_TITLE", video.title);
                    startActivity(intent);
                });
                recyclerView.setAdapter(videoAdapter);
            }
        }.execute();
    }
}
