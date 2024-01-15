package com.example.nutritracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProgFragment extends Fragment {

    private TextView dateText;
    private long startTime = 0;
    private SharedPreferences preferences;

    private AppDatabase db;
    private ExecutorService databaseExecutor;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ProgFragment() {
        // Required empty public constructor
    }

    public static ProgFragment newInstance(String param1, String param2) {
        ProgFragment fragment = new ProgFragment();
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
        View view = inflater.inflate(R.layout.fragment_prog, container, false);

        dateText = view.findViewById(R.id.dateText);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                dateText.setText(day + "/" + (month + 1) + "/" + year);
                                // Printing a message when the date is changed
                                Toast.makeText(getContext(), "User changed the date to: " + day + "/" + (month + 1) + "/" + year, Toast.LENGTH_LONG).show();
                            }
                        }, year, month, day);
                datePickerDialog.show();
                // Printing a message when the dateText is clicked
                Toast.makeText(getContext(), "User clicked on date!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        long entryTime = System.currentTimeMillis();

        databaseExecutor.execute(() -> {
            ScreenTime currentScreenTime = db.screenTimeDao().getScreenTime("ProgFragment");
            if (currentScreenTime == null) {
                currentScreenTime = new ScreenTime();
                currentScreenTime.screenName = "ProgFragment";
                currentScreenTime.timeSpent = 0;
                currentScreenTime.visitCount = 1;
                currentScreenTime.lastEntryTime = entryTime;
                db.screenTimeDao().insertScreenTime(currentScreenTime);
            } else {
                db.screenTimeDao().incrementVisitCount("ProgFragment");
                db.screenTimeDao().updateEntryTime("ProgFragment", entryTime);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        long exitTime = System.currentTimeMillis();
        long duration = exitTime - startTime;

        databaseExecutor.execute(() -> {
            db.screenTimeDao().updateExitTimeAndDuration("ProgFragment", exitTime, duration);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseExecutor.shutdownNow();
    }
}
