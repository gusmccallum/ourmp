package com.example.ourmp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivityFeedBaseAdapter extends BaseAdapter {
    ArrayList<Activity> activityList;
    Context context;

    public ActivityFeedBaseAdapter(ArrayList<Activity> activityList, Context context){
        this.activityList = activityList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return activityList.size();
    }

    @Override
    public Object getItem(int i) {
        return activityList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.activity_feed_row, null);
        TextView activityTitle = view.findViewById(R.id.activityTitle);
        TextView activityDescription = view.findViewById(R.id.activityDescription);
        TextView activityDate = view.findViewById(R.id.activityDate);
        ImageView activityPicture = view.findViewById(R.id.activityPicture);
        if(activityList.get(i).activityPicture != null){
            activityPicture.setImageBitmap(activityList.get(i).activityPicture);
        }else{
            activityPicture.setImageResource(R.drawable.law);
        }

        activityTitle.setText(String.valueOf(activityList.get(i).activityTitle));
        activityDescription.setText(String.valueOf(activityList.get(i).activityDescription));
        activityDate.setText(String.valueOf(activityList.get(i).activityDate));

        return view;
    }
}
