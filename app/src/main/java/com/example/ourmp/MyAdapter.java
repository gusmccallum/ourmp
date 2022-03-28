package com.example.ourmp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
{
    private static final String TAG = "MyAdapter";
    List<Bill> billList;

    public MyAdapter(List<Bill> billList)
    {
        this.billList = billList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position)
    {
        Bill bill = billList.get(position);
        holder.billNumber.setText(bill.getBillNum());
        holder.billSession.setText("Session: " + bill.getBillSession());
        holder.billDate.setText(bill.getBillDate());
        holder.billResult.setAllCaps(true);
        holder.billResult.setText(bill.getBillResult());
        holder.yesVote.setText(bill.getYesVotes());
        holder.noVote.setText(bill.getNoVotes());
        holder.billDesc.setText(bill.getBillDesc());

        boolean isExpanded = billList.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount()
    {
        return billList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private static final String TAG = "MyViewHolder";
        ConstraintLayout expandableLayout;
        TextView billNumber, billSession, billDate, billResult;
        TextView yesVote, noVote, billDesc;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            billNumber = itemView.findViewById(R.id.billNumber);
            billSession = itemView.findViewById(R.id.billSession);
            billDate = itemView.findViewById(R.id.billDate);
            billResult = itemView.findViewById(R.id.billResult);
            yesVote = itemView.findViewById(R.id.yesVote);
            noVote = itemView.findViewById(R.id.noVote);
            billDesc = itemView.findViewById(R.id.billDesc);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);

            billNumber.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Bill bill = billList.get(getAdapterPosition());
                    bill.setExpanded(!bill.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
