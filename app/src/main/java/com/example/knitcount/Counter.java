package com.example.knitcount;

public class Counter {

    private int value;
    private String name;

    public Counter(String name, int initialValue) {
        this.name = name;
        this.value = initialValue;
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