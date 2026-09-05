package com.example.knitcount;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "counters")
public class Counter {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private int value;
    private String name;

    public Counter(long id, String name, int value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void increment() {
        value++;
    }

    public void decrement() {
        if (value > 0) {
            value--;
        }
    }

    public void reset() {
        value = 0;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}