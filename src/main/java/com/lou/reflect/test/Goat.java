package com.lou.reflect.test;

public class Goat extends Animal implements Locomotion {
    public Goat(String name) {
        super(name);
    }

    @Override
    protected String getSound() {
        return "山羊叫";
    }

    @Override
    public String eats() {
        return "吃草";
    }

    @Override
    public String getLocomotion() {
        return "行走";
    }
}
