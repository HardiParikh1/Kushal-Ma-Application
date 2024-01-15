package com.example.nutritracker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.widget.ImageButton;

public class TrimesterVideo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_trimester);

        insertVideosIntoDatabase();

        ImageButton currentVideoButton = findViewById(R.id.btnCurrentVideo);
        ImageButton archiveButton = findViewById(R.id.btnArchive);


        currentVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveVideosAndStartActivity();
            }
        });
        archiveButton.setOnClickListener(v -> {
            Intent intent = new Intent(TrimesterVideo.this, ArchiveActivity.class);
            startActivity(intent);
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void retrieveVideosAndStartActivity() {
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                List<Video> videos = AppDatabase.getDatabase(getApplicationContext()).videoDao().getAll();
                List<String> urls = new ArrayList<>();
                for (Video video : videos) {
                    urls.add(video.url);
                    Log.d("MainActivity", "URL added: " + video.url); // Log each URL
                }
                return urls;
            }

            @Override
            protected void onPostExecute(List<String> videoUrls) {
                // Log the entire list
                Log.d("MainActivity", "URL list: " + videoUrls.toString());
                Intent intent = new Intent(TrimesterVideo.this, CurrentVideoActivity.class);
                intent.putStringArrayListExtra("VIDEO_URL_LIST", new ArrayList<>(videoUrls));
                startActivity(intent);
            }
        }.execute();
    }


    @SuppressLint("StaticFieldLeak")
    private void insertVideosIntoDatabase() {
        VideoDao videoDao = AppDatabase.getDatabase(getApplicationContext()).videoDao();

        // Clear existing videos before inserting new ones
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                videoDao.deleteAll();
                Video video1 = new Video();
                video1.title = "Pregnancy Tips";
                video1.description = "Helpful tips for a healthy pregnancy.";
                video1.thumbnailId = R.drawable.oats;
                video1.url = "E0i7NQsJdWY";

                Video video2 = new Video();
                video2.title = "Nutrition During Pregnancy";
                video2.description = "Understanding nutrition needs.";
                video2.thumbnailId = R.drawable.oats;
                video2.url = "N_PglaMSA2k";

                Video video3 = new Video();
                video3.title = "Nutrition During Pregnancy";
                video3.description = "Understanding nutrition needs.";
                video3.thumbnailId = R.drawable.oats;
                video3.url = "N_PglaMSA2k";

                videoDao.insertAll(video1, video3);
                return null;
            }
        }.execute();
    }


    private static class InsertAsyncTask extends AsyncTask<Video, Void, Void> {
        private VideoDao asyncTaskDao;

        InsertAsyncTask(VideoDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Video... params) {
            asyncTaskDao.insertAll(params);
            return null;
        }
    }
}