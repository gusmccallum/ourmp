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

public class BillFeedRecyclerAdapter  extends
        RecyclerView.Adapter<BillFeedRecyclerAdapter.BillFeedViewHolder> {
    ArrayList<Activity> activities;
    Context context;
    BillFeedRecyclerAdapter.OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(Activity bill);
    }

    public BillFeedRecyclerAdapter(ArrayList<Activity> list, Context context){
        this.activities = list;
        this.context = context;
        listener = (OnItemClickListener) context;
    }

    public void setFilter(ArrayList<Activity> filteredBills) {
        this.activities = filteredBills;
    }

    public void update() {
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BillFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_bill_search_row, parent,false);
        return new BillFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillFeedRecyclerAdapter.BillFeedViewHolder holder, final int i) {
        if(activities.get(i).activityPicture != null){
            holder.activityPicture.setImageBitmap(activities.get(i).activityPicture);
        }else{
            holder.activityPicture.setImageResource(R.drawable.law);
        }
        holder.activityTitle.setText(String.valueOf(activities.get(i).activityTitle));
        holder.activityDate.setText(String.valueOf(activities.get(i).activityDate));
        final Activity temp = activities.get(i);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(temp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public static class BillFeedViewHolder extends RecyclerView.ViewHolder{
        LinearLayout linearLayout;
        TextView activityTitle;
        TextView activityDate;
        ImageView activityPicture;
        public BillFeedViewHolder(@NonNull View view){
            super(view);
            linearLayout = view.findViewById(R.id.linearLayout);
            activityTitle = view.findViewById(R.id.activityTitle);
            activityDate = view.findViewById(R.id.activityDate);
            activityPicture = view.findViewById(R.id.activityPicture);
        }
    }


}
