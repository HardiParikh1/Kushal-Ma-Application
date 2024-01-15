// VideoInteractionDao.java
package com.example.nutritracker;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface VideoInteractionDao {
    @Insert
    long insert(VideoInteraction interaction);

    @Query("SELECT * FROM VideoInteraction WHERE videoId = :videoId")
    List<VideoInteraction> getInteractionsByVideoId(String videoId);
}
