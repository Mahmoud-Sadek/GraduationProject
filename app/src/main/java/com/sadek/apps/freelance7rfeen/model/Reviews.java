package com.sadek.apps.freelance7rfeen.model;

/**
 * Created by Mahmoud Yahia on 4/18/2017.
 */

public class Reviews {

    private String Reviewer_Id;
    private String Reviewer_Email;
    private String Reviewer_Name;
    private String Reviewer_Image_Url;
    private String Review_Content;
    private String Review_Date;

    public Reviews() {

    }

    public String getReviewer_Id() {
        return Reviewer_Id;
    }

    public void setReviewer_Id(String reviewer_Id) {
        Reviewer_Id = reviewer_Id;
    }

    public String getReviewer_Email() {
        return Reviewer_Email;
    }

    public void setReviewer_Email(String reviewer_Email) {
        Reviewer_Email = reviewer_Email;
    }

    public String getReviewer_Name() {
        return Reviewer_Name;
    }

    public void setReviewer_Name(String reviewer_Name) {
        Reviewer_Name = reviewer_Name;
    }

    public String getReviewer_Image_Url() {
        return Reviewer_Image_Url;
    }

    public void setReviewer_Image_Url(String reviewer_Image_Url) {
        Reviewer_Image_Url = reviewer_Image_Url;
    }

    public String getReview_Content() {
        return Review_Content;
    }

    public void setReview_Content(String review_Content) {
        Review_Content = review_Content;
    }

    public String getReview_Date() {
        return Review_Date;
    }

    public void setReview_Date(String review_Date) {
        Review_Date = review_Date;
    }
}
