package com.sadek.apps.freelance7rfeen.model;

/**
 * Created by Mahmoud Sadek on 6/18/2017.
 */

public class User {

    String name,  email,  password,  phone,  address,  country,  city;

    public User(String phone, String address, String country, String city, String name) {
        this.phone = phone;
        this.address = address;
        this.country = country;
        this.city = city;
        this.name = name;
    }

    public User(String name, String email, String password, String phone, String address, String country, String city) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.country = country;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }
}
