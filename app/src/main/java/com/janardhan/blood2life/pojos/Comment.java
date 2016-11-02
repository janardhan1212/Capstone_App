package com.janardhan.blood2life.pojos;

import com.google.firebase.database.IgnoreExtraProperties;

// [START comment_class]
@IgnoreExtraProperties
public class Comment {
    private String author;
    private String text;
    private Object timestamp;
    private String user_id;

    public String getUser_propic() {
        return user_propic;
    }

    public String getUser_id() {
        return user_id;
    }

    private String user_propic;
    public Comment() {
        // empty default constructor, necessary for Firebase to be able to deserialize comments
    }

    public Comment(String author, String text, Object timestamp,String user_id,String user_propic) {
        this.author = author;
        this.text = text;
        this.timestamp = timestamp;
        this.user_id = user_id;
        this.user_propic = user_propic;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public Object getTimestamp() {
        return timestamp;
    }
}
