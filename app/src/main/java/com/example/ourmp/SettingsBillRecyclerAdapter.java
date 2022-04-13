package com.example.ourmp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SettingsBillRecyclerAdapter extends RecyclerView.Adapter<SettingsBillRecyclerAdapter.ViewHolder> {

    private SettingsBillRecyclerInterface billRecyclerInterface;
    private ArrayList<Bill> subscribedBills;

    public SettingsBillRecyclerAdapter(ArrayList<Bill> subscribedBills, SettingsBillRecyclerInterface billRecyclerInterface) {
        this.subscribedBills = subscribedBills;
        this.billRecyclerInterface = billRecyclerInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.settings_bill_rowitem, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    billRecyclerInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }

}
