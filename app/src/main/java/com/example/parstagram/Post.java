package com.example.parstagram;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject{
    public static final String KEY_CAPTION = "Caption";
    public static final String KEY_IMAGE = "Image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_KEY = "createdAt";
    public static final String KEY_LIKED_BY = "likedBy";

    public String getCaption() {
        return getString(KEY_CAPTION);
    }

    public void setCaption(String caption) {
        put(KEY_CAPTION, caption);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public User getUser() {
        return (User) getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public String likeCountDisplayText() {
        String likesText = String.valueOf(getLikedBy().size());
        likesText += getLikedBy().size() == 1 ? " like" : " likes";
        return likesText;
    }

    public List<String> getLikedBy() {
        List<String> likedBy = getList(KEY_LIKED_BY);
        if(likedBy == null) {
            likedBy = new ArrayList<>();
        }
        return likedBy;
    }

    public void setLikedBy(List<String> likedBy) {
        put(KEY_LIKED_BY, likedBy);
    }
}

