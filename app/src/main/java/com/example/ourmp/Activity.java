package com.example.ourmp;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Activity  implements Comparable<Activity>{
    String activityPicture;
    String activityTitle;
    String activityDescription;
    String activityDate;
    String activitySession;
    String activityStatus;

    public Activity(String pic, String title, String desc, String date, String session){
        this.activityPicture = pic;
        this.activityTitle = title;
        this.activityDescription = desc;
        this.activityDate = date;
        this.activitySession = session;
    }

    public Activity(){

    }

    public void setStatus(String status){
        this.activityStatus = status;
    }

    @Override
    public int compareTo(Activity obj) {
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = (Date) format.parse(activityDate);
            Date d2 = (Date) format.parse(obj.activityDate);
            return d1.compareTo(d2);
        }catch (Exception e){
            return 0;
        }
    }
}
