package com.example.parstagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("hxu7r5OCI4AujUcQe8kJLlIPzvYcdgKNwxrDfOeq")
                .clientKey("16TVVVFg7ZwhHvsw0L5HKgSgetRb6UZH3pU79dFY")
                .server("https://parseapi.back4app.com")
                .build()
        );

    }
}
