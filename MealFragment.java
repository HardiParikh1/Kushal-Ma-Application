package com.example.nutritracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MealFragment extends Fragment {

    private long startTime = 0;
    private SharedPreferences preferences;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private AppDatabase db;
    private ExecutorService databaseExecutor;

    public MealFragment() {
        // Required empty public constructor
    }

    public static MealFragment newInstance(String param1, String param2) {
        MealFragment fragment = new MealFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getDatabase(getContext());
        databaseExecutor = Executors.newSingleThreadExecutor();
        preferences = getActivity().getSharedPreferences("AppUsageStats", Context.MODE_PRIVATE);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal, container, false);

        ImageButton imageButton = view.findViewById(R.id.brButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Printing a message when the button is clicked
                Toast.makeText(getContext(), "User clicked on Trimester 1 button!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), RecbScreen.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        long entryTime = System.currentTimeMillis();

        databaseExecutor.execute(() -> {
            ScreenTime currentScreenTime = db.screenTimeDao().getScreenTime("MealFragment");
            if (currentScreenTime == null) {
                currentScreenTime = new ScreenTime();
                currentScreenTime.screenName = "MealFragment";
                currentScreenTime.timeSpent = 0;
                currentScreenTime.visitCount = 1;
                currentScreenTime.lastEntryTime = entryTime;
                db.screenTimeDao().insertScreenTime(currentScreenTime);
            } else {
                db.screenTimeDao().incrementVisitCount("MealFragment");
                db.screenTimeDao().updateEntryTime("MealFragment", entryTime);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        long exitTime = System.currentTimeMillis();
        long duration = exitTime - startTime;

        databaseExecutor.execute(() -> {
            db.screenTimeDao().updateExitTimeAndDuration("MealFragment", exitTime, duration);
        });
    }
}