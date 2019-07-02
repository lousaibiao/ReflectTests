package com.lou.reflect.test;

public abstract class Animal implements Eating {
    public static String CATEGORY = "domestic";
    private String name;

    protected abstract String getSound();

    public Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
