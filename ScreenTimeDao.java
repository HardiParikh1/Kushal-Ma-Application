package com.example.nutritracker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public interface ScreenTimeDao {
    @Query("SELECT * FROM screen_time WHERE screenName = :screenName LIMIT 1")
    ScreenTime getScreenTime(String screenName);

    @Insert
    void insertScreenTime(ScreenTime screenTime);

    @Update
    void updateScreenTime(ScreenTime screenTime);

    @Transaction
    @Query("UPDATE screen_time SET visitCount = visitCount + 1 WHERE screenName = :screenName")
    void incrementVisitCount(String screenName);

    @Query("UPDATE screen_time SET lastEntryTime = :entryTime WHERE screenName = :screenName")
    void updateEntryTime(String screenName, long entryTime);

    @Query("UPDATE screen_time SET lastExitTime = :exitTime, timeSpent = timeSpent + :timeSpent WHERE screenName = :screenName")
    void updateExitTimeAndDuration(String screenName, long exitTime, long timeSpent);
}
