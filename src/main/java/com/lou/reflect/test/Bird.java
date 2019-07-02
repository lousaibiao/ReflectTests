package com.lou.reflect.test;

public class Bird extends Animal {
    private boolean walks;


    public Bird() {
        super("鸟");
    }

    public Bird(String name) {
        super(name);
    }

    public Bird(String name, boolean walks) {
        super(name);
        this.walks = walks;
    }

    @Override
    protected String getSound() {
        return null;
    }

    @Override
    public String eats() {
        return null;
    }

    public boolean isWalks() {
        return walks;
    }

    public void setWalks(boolean walks) {
        this.walks = walks;
    }
}
