package com.janardhan.blood2life.pojos;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User_s {
    public String getRefreshedToken() {
        return refreshedToken;
    }

    public void setRefreshedToken(String refreshedToken) {
        this.refreshedToken = refreshedToken;
    }

    public String refreshedToken;

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String profile_pic;

    public String getFullName() {
        return fullName;
    }

    public String fullName;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String from;
    public String sex;
    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getphone_number() {
        return phone_number;
    }

    public void setphone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String email_id;
    public String phone_number;
    public String group;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }


    public void setState(String state) {
        this.state = state;
    }

    public String city;
    public String state;

    public User_s() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User_s(String fullName, String email_id, String phone_number, String profile_pic, String group, String state, String city, String from, String sex, String refreshedToken) {

        this.fullName = fullName;
        this.email_id=email_id;
        this.phone_number=phone_number;
        this.profile_pic=profile_pic;
        this.group=group;
        this.state=state;
        this.city=city;
        this.from=from;
       this.sex=sex;
        this.refreshedToken=refreshedToken;
    }

}
// [END blog_user_class]
