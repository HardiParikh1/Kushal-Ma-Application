package com.example.nutritracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private long startTime;
    private long endTime;
    private SharedPreferences preferences;

    private ExecutorService databaseExecutor;
    private AppDatabase db;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences("AppUsageStats", Context.MODE_PRIVATE);
        db = AppDatabase.getDatabase(getContext());
        databaseExecutor = Executors.newSingleThreadExecutor();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageButton imageButton = view.findViewById(R.id.myImageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "User Clicked on Profile Button", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        long entryTime = System.currentTimeMillis();

        databaseExecutor.execute(() -> {
            ScreenTime currentScreenTime = db.screenTimeDao().getScreenTime("HomeFragment");
            if (currentScreenTime == null) {
                currentScreenTime = new ScreenTime();
                currentScreenTime.screenName = "HomeFragment";
                currentScreenTime.timeSpent = 0;
                currentScreenTime.visitCount = 1;
                currentScreenTime.lastEntryTime = entryTime;
                db.screenTimeDao().insertScreenTime(currentScreenTime);
            } else {
                db.screenTimeDao().incrementVisitCount("HomeFragment");
                db.screenTimeDao().updateEntryTime("HomeFragment", entryTime);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        long exitTime = System.currentTimeMillis();
        long duration = exitTime - startTime;

        databaseExecutor.execute(() -> {
            db.screenTimeDao().updateExitTimeAndDuration("HomeFragment", exitTime, duration);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseExecutor.shutdownNow();
    }
}