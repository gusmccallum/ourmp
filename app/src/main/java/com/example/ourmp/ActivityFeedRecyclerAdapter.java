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

public class ActivityFeedRecyclerAdapter  extends
        RecyclerView.Adapter<ActivityFeedRecyclerAdapter.ActivityFeedViewHolder> {
    ArrayList<Activity> activities;
    Context context;

    public interface AdapterCallback {
        void onMethodCallback(Activity yourValue);
    }

    private ActivityFeedRecyclerAdapter.AdapterCallback mAdapterCallback;

    public ActivityFeedRecyclerAdapter(ArrayList<Activity> list, Context context){
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
        View view = LayoutInflater.from(context).inflate(R.layout.search_row, parent,false);
        return new ActivityFeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityFeedRecyclerAdapter.ActivityFeedViewHolder holder, final int i) {
        this.mAdapterCallback = ((ActivityFeedRecyclerAdapter.AdapterCallback) context);
        final Activity bill = activities.get(i);

        if(activities.get(i).activityPicture != null){
            holder.activityPicture.setImageBitmap(activities.get(i).activityPicture);
        }else{
            holder.activityPicture.setImageResource(R.drawable.law);
        }
        holder.activityTitle.setText(String.valueOf(activities.get(i).activityTitle));
        final Activity temp = activities.get(i);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterCallback.onMethodCallback(bill);
                Intent intent = new Intent(context, BillCardActivity.class);
                intent.putExtra("bill", temp);
                context.startActivity(intent);
            }
        });
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
