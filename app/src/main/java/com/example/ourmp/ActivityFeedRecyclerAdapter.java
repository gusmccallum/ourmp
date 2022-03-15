package com.example.ourmp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivityFeedRecyclerAdapter  extends
        RecyclerView.Adapter<ActivityFeedRecyclerAdapter.ActivityFeedViewHolder> {
    ArrayList<Activity> activities;
    Context context;
    public ActivityFeedRecyclerAdapter(ArrayList<Activity> list, Context context){
        this.activities = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ActivityFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_feed_row, parent,false);
        return new ActivityFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityFeedRecyclerAdapter.ActivityFeedViewHolder holder, int i) {
        if(activities.get(i).activityPicture != null){
            holder.activityPicture.setImageBitmap(activities.get(i).activityPicture);
        }else{
            holder.activityPicture.setImageResource(R.drawable.law);
        }

        holder.activityTitle.setText(String.valueOf(activities.get(i).activityTitle));
        holder.activityDescription.setText(String.valueOf(activities.get(i).activityDescription));
        holder.activityDate.setText(String.valueOf(activities.get(i).activityDate));
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public static class ActivityFeedViewHolder extends RecyclerView.ViewHolder{
        TextView activityTitle;
        TextView activityDescription;
        TextView activityDate;
        ImageView activityPicture;
        public ActivityFeedViewHolder(@NonNull View view){
            super(view);
            activityTitle = view.findViewById(R.id.activityTitle);
            activityDescription = view.findViewById(R.id.activityDescription);
            activityDate = view.findViewById(R.id.activityDate);
            activityPicture = view.findViewById(R.id.activityPicture);
        }
    }
}
