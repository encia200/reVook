package com.jy.revook_1111.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by remna on 2017-12-20.
 */

public class ChatModel {


    public Map<String, Boolean> users = new HashMap<>(); // 채팅방 유저들
    public Map<String, Comment> comments = new HashMap<>(); // 채팅방 대화

    public static class Comment {
        public String uid;
        public String message;
    }

}
