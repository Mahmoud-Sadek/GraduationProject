package com.sadek.apps.freelance7rfeen.model;

import java.util.List;

/**
 * Created by Mahmoud Sadek on 6/23/2017.
 */

public class ClientProfile {
    String name, address, skills, education, experience, phone;
    int id, years_experience, global_evaluation, available, government, city, carrier;
    float rate;
    long lat =0;
    long log=0;
    List<Rating> ratings;
    ClientProfile otherWorkersInfo;

    public ClientProfile() {
    }

    public long getLat() {
        return lat;
    }

    public long getLog() {
        return log;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public void setLog(long log) {
        this.log = log;
    }

    public String getName() {
        return name;
    }

    public int getCarrier() {
        return carrier;
    }

    public String getAddress() {
        return address;
    }

    public String getSkills() {
        return skills;
    }

    public String getEducation() {
        return education;
    }

    public String getExperience() {
        return experience;
    }

    public int getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public int getYears_experience() {
        return years_experience;
    }

    public int getGlobal_evaluation() {
        return global_evaluation;
    }

    public int getAvailable() {
        return available;
    }

    public int getGovernment() {
        return government;
    }

    public int getCity() {
        return city;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public ClientProfile getOtherWorkersInfo() {
        return otherWorkersInfo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCarrier(int carrier) {
        this.carrier = carrier;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setYears_experience(int years_experience) {
        this.years_experience = years_experience;
    }

    public void setGlobal_evaluation(int global_evaluation) {
        this.global_evaluation = global_evaluation;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public void setGovernment(int government) {
        this.government = government;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public void setOtherWorkersInfo(ClientProfile otherWorkersInfo) {
        this.otherWorkersInfo = otherWorkersInfo;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
