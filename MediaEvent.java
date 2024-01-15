package com.example.nutritracker;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "media_events")
public class MediaEvent {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "type") // "audio" or "video"
    public String type;

    @ColumnInfo(name = "action") // Changed from "event" to "action"
    public String action;

    @ColumnInfo(name = "timestamp")
    public long timestamp;

    @ColumnInfo(name = "duration") // Added duration field
    public long duration;
}
