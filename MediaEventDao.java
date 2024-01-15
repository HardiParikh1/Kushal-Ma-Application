package com.example.nutritracker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.nutritracker.MediaEvent;

import java.util.List;

@Dao
public interface MediaEventDao {
    @Insert
    void insertMediaEvent(MediaEvent mediaEvent);

    @Query("SELECT * FROM media_events WHERE type = :type")
    LiveData<List<MediaEvent>> getAllEventsByType(String type);

    // You may need more queries to fetch specific data, e.g., time spent, count of plays/pauses, etc.
}
