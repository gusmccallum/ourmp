package com.example.ourmp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    ArrayList<MP> MPsArrayList;
    ArrayList<Activity> BillsList;
    Context context;

    public interface OnItemClickListener {
        void onItemClick(int positon, MP item, View view);
    }

    public void setFilter(ArrayList<MP> filterdNames) {
        this.MPsArrayList = filterdNames;
    }

    public void update() {
        notifyDataSetChanged();
    }

    public SearchAdapter(ArrayList<MP> MPsArrayList, AppCompatActivity context) {
        this.MPsArrayList = MPsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_search_row, parent, false);
        return new SearchAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.MyViewHolder holder, final int position) {
        final MP member = MPsArrayList.get(position);

        holder.name.setText(member.getName());
        holder.party.setText(member.getParty());
        holder.image_app.setImageBitmap(member.getPhoto());

        holder.ll_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MPCardActivity.class);
                intent.putExtra("selectedMP", member);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return MPsArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, party;
        ImageView image_app;
        LinearLayout ll_;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            party = itemView.findViewById(R.id.party);
            image_app = itemView.findViewById(R.id.image);
            ll_ = itemView.findViewById(R.id.lyt_parent);
        }
    }

}


