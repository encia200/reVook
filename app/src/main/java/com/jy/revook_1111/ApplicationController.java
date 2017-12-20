package com.jy.revook_1111;

import android.app.Application;

import com.jy.revook_1111.model.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ApplicationController extends Application{
    public static UserModel currentUser;
    public static ArrayList<String> currentUser_followers;

    public static void initFollowers()
    {
        currentUser_followers = new ArrayList<>();
        for(String str : currentUser.followings.keySet())
        {
            currentUser_followers.add(str);
        }
    }

    public static int getFollowersSize()
    {
        return currentUser_followers.size();
    }

}
