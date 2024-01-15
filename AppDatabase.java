package com.example.nutritracker;

import static com.google.android.material.transition.MaterialSharedAxis.X;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Video.class, ScreenTime.class, MediaEvent.class,VideoInteraction.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract VideoDao videoDao();
    public abstract ScreenTimeDao screenTimeDao();
    public abstract MediaEventDao mediaEventDao();
    public abstract VideoInteractionDao videoInteractionDao();


    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "unified_database")
                            .fallbackToDestructiveMigration() // Keep the configuration from Code 2
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
