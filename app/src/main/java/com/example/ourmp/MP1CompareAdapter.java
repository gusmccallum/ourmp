package com.example.ourmp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MP1CompareAdapter extends RecyclerView.Adapter<MP1CompareAdapter.TasksViewHolder> {
    private Context context;
    public List<Ballot> ballotList;
    public int flag = 0;

    public MP1CompareAdapter(Context context, List<Ballot> ballotList, int flag){
        this.context = context;
        this.ballotList = ballotList;
        this.flag = flag;
    }

    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_compare, parent, false);
        return new MP1CompareAdapter.TasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MP1CompareAdapter.TasksViewHolder holder, int position) {
        Ballot ballot = ballotList.get(position);

        holder.billnum_txt.setText(ballot.getBillNum());
        if(ballot.getBallot().equals("")){
            holder.voted_txt.setText("");
            holder.ballot_txt.setText("Didn't vote on ");
            holder.relativeLayoutFront.setVisibility(View.GONE);
            holder.relativeLayoutBack.setVisibility(View.GONE);
        }
        else{
            holder.ballot_txt.setText(ballot.getBallot());

            if(flag == 1){
                holder.relativeLayoutFront.setVisibility(View.GONE);
                holder.relativeLayoutBack.setVisibility(View.VISIBLE);

                if(ballot.getBallot().equals("yes")){
                    holder.yesOrNo1.setImageResource(R.drawable.yes_icon);
                }
                else{
                    holder.yesOrNo1.setImageResource(R.drawable.no_icon);
                }
            }
            else if(flag == 2){
                holder.relativeLayoutFront.setVisibility(View.VISIBLE);
                holder.relativeLayoutBack.setVisibility(View.GONE);

                if(ballot.getBallot().equals("yes")){
                    holder.yesOrNo2.setImageResource(R.drawable.yes_icon);
                }
                else{
                    holder.yesOrNo2.setImageResource(R.drawable.no_icon);
                }
            }
        }

    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView ballot_txt;
        private final TextView billnum_txt;
        private RelativeLayout relativeLayoutFront;
        private RelativeLayout relativeLayoutBack;
        private ImageView yesOrNo1;
        private ImageView yesOrNo2;
        private final TextView voted_txt;

        public TasksViewHolder(View itemView) {
            super(itemView);
            ballot_txt = itemView.findViewById(R.id.ballot_txt);
            billnum_txt = itemView.findViewById(R.id.billnum_txt);
            relativeLayoutFront = itemView.findViewById(R.id.mp2_icon_relative);
            relativeLayoutBack = itemView.findViewById(R.id.mp1_icon_relative);
            yesOrNo1 = itemView.findViewById(R.id.mp1_icon);
            yesOrNo2 = itemView.findViewById(R.id.mp2_icon);
            voted_txt = itemView.findViewById(R.id.voted_txt);
        }
        @Override
        public void onClick(View view) {
        }
    }
    @Override
    public int getItemCount() {
        return  ballotList.size();
    }
}
