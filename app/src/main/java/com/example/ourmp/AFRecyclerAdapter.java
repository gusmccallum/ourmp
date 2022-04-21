package com.example.ourmp;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

public class AFRecyclerAdapter  extends
        RecyclerView.Adapter<AFRecyclerAdapter.ActivityFeedViewHolder> {
    ArrayList<Activity> activities;
    Context context;
    public AFRecyclerAdapter(ArrayList<Activity> list, Context context){
        this.activities = list;
        this.context = context;
    }

    public void setFilter(ArrayList<Activity> filteredBills) {
        this.activities = filteredBills;
    }

    public void update() {
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ActivityFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_feed_row, parent,false);
        return new ActivityFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AFRecyclerAdapter.ActivityFeedViewHolder holder, final int i) {
        if(activities.get(i).activityPicture != null){
            holder.activityPicture.setImageBitmap(activities.get(i).activityPicture);
        }
        else if (activities.get(i).activityPicture == null && activities.get(i).party != null){

            String party = activities.get(i).party;

            if (party == null) {
                holder.activityPicture.setImageResource(R.drawable.bill_other);
            }
            else {
                switch (party) {
                    case "Conservative":
                        holder.activityPicture.setImageResource(R.drawable.bill_con);
                        break;
                    case "Liberal":
                        holder.activityPicture.setImageResource(R.drawable.bill_lib);
                        break;
                    case "NDP":
                        holder.activityPicture.setImageResource(R.drawable.bill_ndp);
                        break;
                    case "Bloc Québécois":
                        holder.activityPicture.setImageResource(R.drawable.bill_bloc);
                        break;
                    case "Green Party":
                        holder.activityPicture.setImageResource(R.drawable.bill_green);
                        break;
                    default:
                        holder.activityPicture.setImageResource(R.drawable.bill_other);
                }
            }



        }
        else {
            holder.activityPicture.setImageResource(R.drawable.mp);
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
        LinearLayout linearLayout;
        TextView activityTitle;
        TextView activityDescription;
        TextView activityDate;
        ImageView activityPicture;
        public ActivityFeedViewHolder(@NonNull View view){
            super(view);
            linearLayout = view.findViewById(R.id.linearLayout);
            activityTitle = view.findViewById(R.id.activityTitle);
            activityDescription = view.findViewById(R.id.activityDescription);
            activityDate = view.findViewById(R.id.activityDate);
            activityPicture = view.findViewById(R.id.activityPicture);
        }
    }
}