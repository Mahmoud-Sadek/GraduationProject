package com.sadek.apps.freelance7rfeen.model;

/**
 * Created by Mahmoud Sadek on 6/27/2017.
 */

public class Requests {
    String id,recived_id,user_name, content, status, end_date, address, phone, create_date;

    public Requests(String id, String recived_id, String user_name, String content, String status, String end_date, String address, String phone, String create_date) {
        this.id = id;
        this.recived_id = recived_id;
        this.user_name = user_name;
        this.content = content;
        this.status = status;
        this.end_date = end_date;
        this.address = address;
        this.phone = phone;
        this.create_date = create_date;
    }

    public String getId() {
        return id;
    }

    public String getRecived_id() {
        return recived_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getContent() {
        return content;
    }

    public String getStatus() {
        return status;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public String getCreate_date() {
        return create_date;
    }
}
