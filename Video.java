package com.example.nutritracker;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Video {
    @PrimaryKey(autoGenerate = true)
    public int id;


    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "thumbnail_id")
    public int thumbnailId;  // Resource ID for the thumbnail

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "url")
    public String url;


    // Getters and setters or public fields depending on your preference
}

