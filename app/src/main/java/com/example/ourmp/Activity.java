package com.example.ourmp;

import android.graphics.Bitmap;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Activity  implements Comparable<Activity>{
    Bitmap activityPicture;
    String activityTitle;
    String activityDescription;
    String activityDate;
    String activityStatus;

    public Activity(Bitmap pic, String title, String desc, String date){
        this.activityPicture = null;
        this.activityTitle = title;
        this.activityDescription = desc;
        this.activityDate = date;
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
