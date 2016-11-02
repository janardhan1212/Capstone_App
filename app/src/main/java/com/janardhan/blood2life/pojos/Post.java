package com.janardhan.blood2life.pojos;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START post_class]
@IgnoreExtraProperties
public class Post {

    public String uid;
    public String name;
    public String profile_pic;
    public String phone_number;
    public String blood_group;
    public String city;
    public String state;
    public String hospital_address;
    public String fb_post;
    public String received,time_stamp;
    //public int starCount = 0;
    //public Map<String, Boolean> stars = new HashMap<>();

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String uid, String name, String phone_number, String blood_group, String city, String state, String hospital_address, String fb_post, String received, String time_stamp,String profile_pic) {
        this.uid = uid;
        this.name = name;
        this.phone_number = phone_number;
        this.blood_group = blood_group;
        this.city = city;
        this.state = state;
        this.hospital_address=hospital_address;
        this.fb_post = fb_post;
        this.received = received;
        this.time_stamp=time_stamp;
        this.profile_pic=profile_pic;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name);
        result.put("phone_number", phone_number);
        result.put("blood_group", blood_group);
        result.put("city", city);
        result.put("state", state);
        result.put("hospital_address", hospital_address);
        result.put("fb_post", fb_post);
        result.put("received", received);
        result.put("time_stamp",time_stamp);
        result.put("profile_pic",profile_pic);

       // result.put("starCount", starCount);
       // result.put("stars", stars);

        return result;
    }
    // [END post_to_map]

}
// [END post_class]
