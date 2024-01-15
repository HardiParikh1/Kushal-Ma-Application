package com.example.nutritracker;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class VideoInteraction {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String videoId;
    public String action; // "pause", "play", "stop"
    public long timestamp;
}
