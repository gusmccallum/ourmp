package com.example.ourmp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BallotsAdapter extends RecyclerView.Adapter<BallotsAdapter.TasksViewHolder> {
    private Context context;
    public List<Ballot> ballotList;

    public BallotsAdapter(Context context, List<Ballot> ballotList){
        this.context = context;
        this.ballotList = ballotList;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_ballots, parent, false);
        return new TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BallotsAdapter.TasksViewHolder holder, int position) {
        Ballot ballot = ballotList.get(position);

        if(ballot.getBallot().equals("")){
            holder.voted_txt.setText("");
            holder.ballot_txt.setText("Didn't vote on ");
        }
        else{
            holder.ballot_txt.setText(ballot.getBallot());
        }

        holder.billnum_txt.setText(ballot.getBillNum());
    }

    @Override
    public int getItemCount() {
        return  ballotList.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView ballot_txt;
        private final TextView billnum_txt;
        private final TextView voted_txt;

        public TasksViewHolder(View itemView) {
            super(itemView);
            ballot_txt = itemView.findViewById(R.id.ballot_txt);
            billnum_txt = itemView.findViewById(R.id.billnum_txt);
            voted_txt = itemView.findViewById(R.id.voted_txt);
        }
        @Override
        public void onClick(View view) {
        }
    }

}
