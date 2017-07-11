package com.sadek.apps.freelance7rfeen.model;

/**
 * Created by Mahmoud Sadek on 7/5/2017.
 */

public class Favorite {
    String userName, id, government, city, job;
    float rate;

    public Favorite(String userName, String id, String government, String city, String job, float rate) {
        this.userName = userName;
        this.id = id;
        this.government = government;
        this.city = city;
        this.job = job;
        this.rate = rate;
    }

    public String getUserName() {
        return userName;
    }

    public String getId() {
        return id;
    }

    public String getGovernment() {
        return government;
    }

    public String getCity() {
        return city;
    }

    public String getJob() {
        return job;
    }

    public float getRate() {
        return rate;
    }
}
