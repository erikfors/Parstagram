package com.fors.erik.parstagram;

import android.app.Application;

import com.fors.erik.parstagram.Models.Comment;
import com.fors.erik.parstagram.Models.Like;
import com.fors.erik.parstagram.Models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);
        ParseObject.registerSubclass(Like.class);

        // set applicationId, and server server based on the values in the back4app settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("QvujMHSHck6IJC1EQ17bfvjjMApgNvZ9PEvBQ9F9")
                .clientKey("e7c0A7qeHfwoPIVIWadzgGJmftLFPJC9yOQqx3uZ")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
