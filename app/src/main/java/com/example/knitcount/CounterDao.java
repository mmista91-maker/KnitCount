package com.example.knitcount;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CounterDao {

    @Insert
    long insert(Counter counter);

    @Update
    void update(Counter counter);

    @Delete
    void delete(Counter counter);

    @Query("SELECT * FROM counters")
    List<Counter> getAll();
}