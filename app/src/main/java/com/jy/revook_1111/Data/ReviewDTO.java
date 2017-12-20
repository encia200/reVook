package com.jy.revook_1111.Data;

import java.util.HashMap;
import java.util.Map;

public class ReviewDTO {

    public String imageUrl;
    //public String title;
    public String content;
    public String uid;
    public String reviewkey;
    public String userName;

    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();
}
