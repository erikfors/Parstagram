package com.fors.erik.parstagram.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

//recreates a comment object in instagram
@ParseClassName("Comments")
public class Comment extends ParseObject {

    public static final String KEY_COMMENT_TEXT = "comment_text";
    public static final String KEY_USER = "user";
    public static final String KEY_POST = "postId";
    public static final String KEY_CREATED_AT = "createdAt";

    public String getComment(){return getString(KEY_COMMENT_TEXT);}

    public void setComment(String comment){put(KEY_COMMENT_TEXT,comment);}

    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser parseUser){
        put(KEY_USER,parseUser);
    }

    public Post getPost(){return (Post) getParseObject(KEY_POST);}

    public void setPost(Post post){put(KEY_POST, post);}

    public Date getCreatedDate() { return getCreatedAt(); }
}
