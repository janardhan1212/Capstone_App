package com.janardhan.blood2life.pojos;

/**
 * Created by janardhanyerranagu on 2/9/16.
 */
public class User {
    private String email_id;
    private String city;
    private String state;
    private String group;
    private String sex;
    private String from;
    private String phone_number;

    private String fullName;
    public User() {}
    public User(String fullName, String email_id, String city, String state, String group, String sex, String phone_number, String from) {
        this.email_id = email_id;
        this.city = city;
        this.state = state;
        this.group = group;
        this.sex = sex;
        this.phone_number = phone_number;
        this.fullName=fullName;
        this.from=from;
    }
    public String getphone_number() {
        return phone_number;
    }
    public String getemail_id() {
        return email_id;
    }

    public String getfullName() {
        return fullName;
    }
    public String getcity() {
        return city;
    }

    public String getstate() {
        return state;
    }
    public String getgroup() {
        return group;
    }
    public String getsex() {
        return sex;
    }
    public String getfrom() {
        return from;
    }
}