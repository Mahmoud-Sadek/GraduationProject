package com.sadek.apps.freelance7rfeen.model;

/**
 * Created by Mahmoud Sadek on 5/1/2017.
 */

public class Speciality {
    String name;
    int img;

    public Speciality(String name, int img) {
        this.name = name;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public int getImg() {
        return img;
    }
}
