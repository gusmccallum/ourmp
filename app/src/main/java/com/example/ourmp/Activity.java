package com.example.ourmp;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class Activity  implements Comparable<Activity>, Parcelable {
    Bitmap activityPicture;
    String activityTitle;
    String activityDescription;
    String activityDate;
    String activityStatus;
    String billSponsor;

    public Activity(@Nullable Bitmap pic, @Nullable String title, @Nullable String desc,
                    String date, @Nullable String billSponsor){
        this.activityPicture = pic;
        this.activityTitle = title;
        this.activityDescription = desc;
        this.activityDate = date;
        this.billSponsor = billSponsor;
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

    public Activity(Parcel in) {
        activityTitle = in.readString();
        activityDescription = in.readString();
        activityStatus = in.readString();
        activityDate = in.readString();
    }

    public static final Creator<Activity> CREATOR = new Creator<Activity>() {
        @Override
        public Activity createFromParcel(Parcel in) {
            return new Activity(in);
        }

        @Override
        public Activity[] newArray(int size) {
            return new Activity[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(activityTitle);
        parcel.writeString(activityDescription);
        parcel.writeString(activityStatus);
        parcel.writeString(activityDate);
    }
}