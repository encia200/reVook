package com.jy.revook_1111.model;

import java.util.HashMap;
import java.util.Map;

public class UserModel {
    public String uid;
    public String email;
    public String password;
    public String userName;
    public String profileImageUrl;

    public int followerCount = 0;
    public Map<String, Boolean> followers = new HashMap<>();
    public int followingCount = 0;
    public Map<String, Boolean> followings = new HashMap<>();
}