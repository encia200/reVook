package com.jy.revook_1111.model;

/**
 * Created by remna on 2017-12-21.
 */

public class NotificationModel {

    public String to;
    public Notification notification = new Notification();
    public Data data = new Data();

    public static class Notification {
        public String title;
        public String text;
    }

    public static class Data{
        public String title;
        public String text;
    }
}
