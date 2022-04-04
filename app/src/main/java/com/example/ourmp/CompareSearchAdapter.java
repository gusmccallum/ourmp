package com.example.ourmp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CompareSearchAdapter extends RecyclerView.Adapter<CompareSearchAdapter.TasksViewHolder> {
    ArrayList<MP> MPsArrayList;
    Context context;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MP mpObj);
    }

    public void setFilter(ArrayList<MP> filterdNames) {
        this.MPsArrayList = filterdNames;
    }

    public void update() {
        notifyDataSetChanged();
    }

    public CompareSearchAdapter(ArrayList<MP> MPsArrayList, Context context) {
        this.MPsArrayList = MPsArrayList;
        this.context = context;
        listener = (OnItemClickListener) context;
    }

    @Override
    public CompareSearchAdapter.TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row_small, parent, false);
        return new TasksViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CompareSearchAdapter.TasksViewHolder holder, final int position) {
        final MP member = MPsArrayList.get(position);

        holder.name.setText(member.getName());
        holder.party.setText(member.getParty());
//        holder.image_app.setImageBitmap(member.getPhoto());

        holder.ll_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(member);
            }
        });
    }

    @Override
    public int getItemCount() {
        return MPsArrayList.size();
    }

    public static class TasksViewHolder extends RecyclerView.ViewHolder {
        TextView name, party;
        //ImageView image_app;
        LinearLayout ll_;

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            party = itemView.findViewById(R.id.party);
           // image_app = itemView.findViewById(R.id.image);
            ll_ = itemView.findViewById(R.id.lyt_parent);
        }
    }
}
