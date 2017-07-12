package com.sadek.apps.freelance7rfeen.parse;

import com.sadek.apps.freelance7rfeen.model.Client;
import com.sadek.apps.freelance7rfeen.model.ClientProfile;
import com.sadek.apps.freelance7rfeen.model.Favorite;
import com.sadek.apps.freelance7rfeen.model.Rating;
import com.sadek.apps.freelance7rfeen.model.Requests;
import com.sadek.apps.freelance7rfeen.utils.ConstantsFreelance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahmoud Sadek on 5/3/2017.
 */
public class ParseJSON {
    //    login
    public static int type, id, complete;
    public static boolean status_login, status_register;


    public static final String KEY_TYPE = "type";
    public static final String KEY_ID = "id";
    public static final String KEY_COMPLETE = "complete";
    public static final String KEY_LOGIN_STATUS = "login";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_TYPE = "user_type";

    private String json;

    public ParseJSON(String json) {
        this.json = json;
    }

    public void parseLogin() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            status_login = jsonObject.getBoolean(KEY_LOGIN_STATUS);
            type = jsonObject.getInt(KEY_TYPE);
            id = jsonObject.getInt(KEY_ID);
            complete = jsonObject.getInt(KEY_COMPLETE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseRegister() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            type = jsonObject.getInt(KEY_USER_TYPE);
            id = jsonObject.getInt(KEY_USER_ID);
            status_register = (type == 0) && (!(id + "").equals("unique_email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseRegister2() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            type = jsonObject.getInt(KEY_USER_TYPE);
            id = jsonObject.getInt(KEY_USER_ID);
            status_register = (type == 1) && (!(id + "").equals("unique_email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void parseSaveRegister2() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONObject workerObject = jsonObject.getJSONObject(KEY_WORKER_OBJECT);
            status_register = workerObject.has(KEY_WORKER_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //    profile client
    public static ClientProfile clientProfile;
    public static boolean status_client_profile;
    public static List<Rating> ratingList;


    public static final String KEY_BASIC_INFO_OBJECT = "basic_info";
    public static final String KEY_WORKER_OBJECT = "worker";
    public static final String KEY_WORKER_SKILLS_OBJECT = "skills";
    public static final String KEY_WORKER_EDUCATION_OBJECT = "educations";
    public static final String KEY_WORKER_EXPERIENCE_OBJECT = "experiance";
    public static final String KEY_WORKER_RATE_OBJECT = "ratings";
    public static final String KEY_WORKER_ID = "worker_id";
    private static final String KEY_NAME = "user_full_name";
    private static final String KEY_ADDRESS = "user_address";
    private static final String KEY_PHONE = "user_mobile";
    private static final String KEY_YEARS_EX = "worker_years_experience";
    private static final String KEY_AVAILABLE = "worker_avilable";
    private static final String KEY_GENDER = "worker_gender";
    private static final String KEY_SKILL = "worker_skill";
    private static final String KEY_EDUCATION = "worker_education";
    private static final String KEY_EXPERIENCE = "worker_experience";
    private static final String KEY_SENT_ID = "request_sent_id";
    private static final String KEY_RECIVED_iD = "request_recived_id";
    private static final String KEY_COMPLETION_TIME = "completion_time";
    private static final String KEY_WORK_PERFICTION = "work_perfiction";
    private static final String KEY_PRICE = "price";
    private static final String KEY_CLEAN_WORK = "clean_work";
    private static final String KEY_DEADLINE = "deadline";
    private static final String KEY_WORK_SUMMARY = "work_summary";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LONG = "long";


    public void parseProfileClient() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            clientProfile = new ClientProfile();
            JSONObject basicInfoObject = jsonObject.getJSONObject(KEY_BASIC_INFO_OBJECT);
            JSONObject workerObject = jsonObject.getJSONObject(KEY_WORKER_OBJECT);
            JSONArray skillsArray = workerObject.getJSONArray(KEY_WORKER_SKILLS_OBJECT);
            JSONArray educationsArray = jsonObject.getJSONArray(KEY_WORKER_EDUCATION_OBJECT);
            JSONArray experiencesArray = jsonObject.getJSONArray(KEY_WORKER_EXPERIENCE_OBJECT);
            clientProfile.setId(workerObject.getInt(KEY_WORKER_ID));
            clientProfile.setName(basicInfoObject.getString(KEY_NAME));
            clientProfile.setAddress(basicInfoObject.getString(KEY_ADDRESS));
            clientProfile.setAvailable(workerObject.getInt(KEY_AVAILABLE));
            clientProfile.setLat(workerObject.getLong(KEY_LAT));
            clientProfile.setLog(workerObject.getLong(KEY_LONG));
            clientProfile.setYears_experience(workerObject.getInt(KEY_YEARS_EX));
            clientProfile.setPhone(basicInfoObject.getString(KEY_PHONE));
            String skills = "";
            for (int i = 0; i < skillsArray.length() - 1; i++) {
                JSONObject skill = skillsArray.getJSONObject(i);
                skills += skill.getString(KEY_SKILL) + ",";
            }
            if (skillsArray.length() != 0) {
                JSONObject skill = skillsArray.getJSONObject(skillsArray.length() - 1);
                skills += skill.getString(KEY_SKILL);
            }
            clientProfile.setSkills(skills);
            String educations = "";
            for (int i = 0; i < educationsArray.length() - 1; i++) {
                JSONObject education = educationsArray.getJSONObject(i);
                educations += education.getString(KEY_EDUCATION) + ",";
            }
            if (educationsArray.length() != 0) {
                JSONObject education = educationsArray.getJSONObject(educationsArray.length() - 1);
                educations += education.getString(KEY_EDUCATION);
            }
            clientProfile.setEducation(educations);
            String experiences = "";
            for (int i = 0; i < experiencesArray.length() - 1; i++) {
                JSONObject experience = experiencesArray.getJSONObject(i);
                experiences += experience.getString(KEY_EXPERIENCE) + ",";
            }
            if (experiencesArray.length() != 0) {
                JSONObject experience = experiencesArray.getJSONObject(experiencesArray.length() - 1);
                experiences += experience.getString(KEY_EXPERIENCE);
            }
            clientProfile.setExperience(experiences);
            status_client_profile = clientProfile.getId() != 0;
//rate
            JSONArray rateArray = jsonObject.getJSONArray(KEY_WORKER_RATE_OBJECT);
            ratingList = new ArrayList<>();
            float clientReview = 0f;
            for (int i = 0; i < rateArray.length(); i++) {
                JSONObject rate = rateArray.getJSONObject(i);
                Rating rating = new Rating(rate.getInt(KEY_SENT_ID), rate.getInt(KEY_RECIVED_iD),
                        rate.getInt(KEY_COMPLETION_TIME), rate.getInt(KEY_WORK_PERFICTION), rate.getInt(KEY_PRICE),
                        rate.getInt(KEY_CLEAN_WORK), rate.getInt(KEY_DEADLINE), rate.getString(KEY_WORK_SUMMARY), rate.getString(KEY_CREATED_AT));
                float allReview = (rate.getInt(KEY_COMPLETION_TIME) + rate.getInt(KEY_WORK_PERFICTION) + rate.getInt(KEY_PRICE) +
                        rate.getInt(KEY_CLEAN_WORK) + rate.getInt(KEY_DEADLINE)) / 5f;
                rating.setReview(allReview);
                ratingList.add(rating);
                clientReview += allReview;
            }
            clientProfile.setRatings(ratingList);
            clientProfile.setRate(clientReview / rateArray.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<Requests> requestsList = new ArrayList<>();
    public static boolean status_rqst;
    public static final String KEY_REQUEST_DETAIL_OBJECT = "requests";
    public static final String KEY_USER_RECIVED_ID = "user_recived_id";
    public static final String KEY_REQUEST_CONTENT = "request_content";
    public static final String KEY_REQUEST_STATUS = "request_status";
    public static final String KEY_END_DATE = "updated_at";
    public static final String KEY_REQUEST_ADDRESS = "request_address";
    public static final String KEY_REQUEST_PHONE = "request_phone";

    public void parseRequests() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONArray requestArray = jsonObject.getJSONArray(KEY_REQUEST_DETAIL_OBJECT);
            JSONArray userInfoArray = jsonObject.getJSONArray(KEY_USER_INFO_OBJECT);
            status_rqst = requestArray.getJSONObject(0).has(KEY_USER_RECIVED_ID);
            requestsList = new ArrayList<>();
            for (int i = 0; i < requestArray.length(); i++) {
                JSONObject requestObject = requestArray.getJSONObject(i);
                JSONObject userInfoObject = userInfoArray.getJSONObject(i);
                Requests request = new Requests(requestObject.getInt("request_id") + "", requestObject.getInt(KEY_USER_RECIVED_ID) + "", userInfoObject.getString(KEY__FAV_NAME), requestObject.getString(KEY_REQUEST_CONTENT),
                        requestObject.getInt(KEY_REQUEST_STATUS) + "", requestObject.getString(KEY_END_DATE), requestObject.getString(KEY_REQUEST_ADDRESS),
                        requestObject.getString(KEY_REQUEST_PHONE), requestObject.getString(KEY_CREATED_AT));
                requestsList.add(request);
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    public static Requests request;
    public static boolean status_rqst_detail;

    public void parseRequestDetail() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);

            JSONObject requestObject = jsonObject.getJSONObject("request_detail");
            JSONObject userInfoObject = jsonObject.getJSONObject("user");
            status_rqst_detail = requestObject.has(KEY_USER_RECIVED_ID);
            request = new Requests(requestObject.getInt("request_id") + "",requestObject.getInt(KEY_USER_RECIVED_ID) + "", userInfoObject.getString(KEY__FAV_NAME), requestObject.getString(KEY_REQUEST_CONTENT),
                    requestObject.getInt(KEY_REQUEST_STATUS) + "", requestObject.getString(KEY_END_DATE), requestObject.getString(KEY_REQUEST_ADDRESS),
                    requestObject.getString(KEY_REQUEST_PHONE), requestObject.getString(KEY_CREATED_AT));
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    public static boolean status_fav;
    public static List<Favorite> favoriteList;
    public static final String KEY_FAVORITE_OBJECT = "favourites";
    public static final String KEY_USER_INFO_OBJECT = "other";
    public static final String KEY_USER_WORK_OBJECT = "worker";
    public static final String KEY_USER_RATE_OBJECT = "rating";
    private static final String KEY__FAV_NAME = "user_full_name";
    public static final String KEY_FAV_USER_ID = "user_id";
    public static final String KEY_FAV_USER_GOVERNMENT = "user_governate";
    public static final String KEY_FAV_USER_CITY = "user_city";
    public static final String KEY_FAV_USER_JOB = "worker_job";


    public void parseFavorite() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
//            JSONArray favoriteArray = jsonObject.getJSONArray(KEY_FAVORITE_OBJECT);
            JSONArray userInfoArray = jsonObject.getJSONArray(KEY_USER_INFO_OBJECT);
            JSONArray user_workArray = jsonObject.getJSONArray(KEY_USER_WORK_OBJECT);
            JSONArray rateArray = jsonObject.getJSONArray(KEY_USER_RATE_OBJECT);
            status_fav = userInfoArray.getJSONObject(0).has("user_id");
            favoriteList = new ArrayList<>();
            for (int i = 0; i < userInfoArray.length(); i++) {
                JSONObject userInfoObject = userInfoArray.getJSONObject(i);
                JSONObject userWorkObject = user_workArray.getJSONObject(i);
                String job_name = "", government = "", city = "";
                for (int j = 0; j < ConstantsFreelance.JOBS_ID1.length; j++) {
                    for (int k = 0; k < ConstantsFreelance.JOBS_ID1[j].length; k++) {
                        if (userWorkObject.getInt(KEY_FAV_USER_JOB) == ConstantsFreelance.JOBS_ID1[j][k]) {
                            job_name = ConstantsFreelance.JOBS_NAMES[j][k];
                            break;
                        }
                    }
                }
                int k;
                for (k = 0; k < ConstantsFreelance.GOVERNMENT_NAMES.length; k++) {
                    if (userInfoObject.getInt(KEY_FAV_USER_GOVERNMENT) == ConstantsFreelance.GOVERNMENT_IDS[k]) {
                        government = ConstantsFreelance.GOVERNMENT_NAMES[k];
                        for (int h = 0; h < ConstantsFreelance.CITY_IDS[k].length; h++) {
                            if (userInfoObject.getInt(KEY_FAV_USER_CITY) == ConstantsFreelance.CITY_IDS[k][h]) {
                                city = ConstantsFreelance.CITY_NAMES[k][h];
                                break;
                            }
                        }
                        break;
                    }
                }

                //rate
                JSONArray ratesArray = rateArray.getJSONArray(i);
                float clientReview = 0f;
                for (int y = 0; y < ratesArray.length(); y++) {
                    JSONObject rate = ratesArray.getJSONObject(y);
                    float allReview = (rate.getInt(KEY_COMPLETION_TIME) + rate.getInt(KEY_WORK_PERFICTION) + rate.getInt(KEY_PRICE) +
                            rate.getInt(KEY_CLEAN_WORK) + rate.getInt(KEY_DEADLINE)) / 5f;
                    clientReview += allReview;
                }
                Favorite favorite = new Favorite(userInfoObject.getString(KEY__FAV_NAME), userInfoObject.getInt(KEY_FAV_USER_ID) + "",
                        government, city, job_name, clientReview);
                favoriteList.add(favorite);
            }

        } catch (JSONException e) {
            e.printStackTrace();

        }
    }


    public static boolean status_user_profile;
    public static Client userProfile;

    public void parseProfileUser() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            JSONObject userObject = jsonObject.getJSONObject("user");
            status_user_profile = userObject.has(KEY_NAME);
            userProfile = new Client(userObject.getString(KEY_NAME), userObject.getString("user_email"), userObject.getString("user_username"), userObject.getString(KEY_PHONE), userObject.getString(KEY_ADDRESS), "1", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}