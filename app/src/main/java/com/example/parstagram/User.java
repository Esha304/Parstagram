package com.example.parstagram;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {
    public static final String KEY_PROFILE = "profile";

    public ParseFile getProfile() {
        return getParseFile(KEY_PROFILE);
    }

    public void setProfile(ParseFile parseFile) {

        put(KEY_PROFILE, parseFile);
    }
}