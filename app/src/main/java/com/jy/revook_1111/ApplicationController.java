package com.jy.revook_1111;

import android.app.Application;

import com.jy.revook_1111.model.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ApplicationController extends Application{
    public static UserModel currentUser;
    public static ArrayList<String> currentUser_followers;
    public static ArrayList<String> currentUser_followings;
    public static ArrayList<String> currentUser_reviews;

    public static void initCurrentUserInfo()
    {
        currentUser_followers = new ArrayList<>();
        for(String str : currentUser.followers.keySet())
        {
            currentUser_followers.add(str);
        }

        currentUser_followings = new ArrayList<>();
        for(String str : currentUser.followings.keySet())
        {
            currentUser_followings.add(str);
        }

        currentUser_reviews = new ArrayList<>();
        for(String str : currentUser.reviews.keySet())
        {
            currentUser_reviews.add(str);
        }
    }

    public static int getFollowersSize()
    {
        return currentUser_followers.size();
    }

}
