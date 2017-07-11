package com.sadek.apps.freelance7rfeen.model;

/**
 * Created by Mahmoud Sadek on 6/23/2017.
 */
public class Rating {
    int sent_id, received_id, completion_time, work_perfection, price, clean_work, deadline;
    float review;
    String work_summary, date;

    public Rating(int sent_id, int received_id, int completion_time, int work_perfection, int price, int clean_work, int deadline, String work_summary, String date) {
        this.sent_id = sent_id;
        this.received_id = received_id;
        this.completion_time = completion_time;
        this.work_perfection = work_perfection;
        this.price = price;
        this.clean_work = clean_work;
        this.deadline = deadline;
        this.work_summary = work_summary;
        this.date = date;
    }

    public int getSent_id() {
        return sent_id;
    }

    public int getReceived_id() {
        return received_id;
    }

    public int getCompletion_time() {
        return completion_time;
    }

    public int getWork_perfection() {
        return work_perfection;
    }

    public int getPrice() {
        return price;
    }

    public int getClean_work() {
        return clean_work;
    }

    public int getDeadline() {
        return deadline;
    }

    public String getWork_summary() {
        return work_summary;
    }

    public String getDate() {
        return date;
    }

    public float getReview() {
        return review;
    }

    public void setReview(float review) {
        this.review = review;
    }
}
