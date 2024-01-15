package com.example.nutritracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "screen_time")
public class ScreenTime {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String screenName;
    public long timeSpent;

    public int visitCount;

    public long lastEntryTime;
    public long lastExitTime;
}
