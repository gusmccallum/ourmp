package com.example.ourmp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class RecentFragment extends Fragment
{
    List<Bill> billList;
    RecyclerView recyclerView;

    public RecentFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        initData();
        initRecyclerView();
        return view;
    }

    private void initRecyclerView()
    {
        MyAdapter myAdapter = new MyAdapter(billList);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initData()
    {
        billList = new ArrayList<>();
        billList.add(new Bill("37", "44-1", "2022-03-02", "PASSED", "Government Business No. 9 (Parliamentary review committee pursuant to the Emergencies Act)", "214", "115"));
        billList.add(new Bill("36", "44-1", "2022-03-02", "FAILED", "Government Business No. 9 (Parliamentary review committee pursuant to the Emergencies Act) (amendment)", "146", "181"));
        billList.add(new Bill("35", "44-1", "2022-03-02", "PASSED", "Motion for closure", "181", "151"));
        billList.add(new Bill("34", "44-1", "2022-03-02", "PASSED", "Motion to proceed to orders of the day", "183", "150"));
        billList.add(new Bill("33", "44-1", "2022-03-02", "PASSED", "Opposition Motion (Representation of Quebec in the House of Commons)", "262", "66"));
        billList.add(new Bill("32", "44-1", "2022-02-21", "PASSED", "Motion for confirmation of the declaration of emergency", "185", "151"));
        billList.add(new Bill("31", "44-1", "2022-02-16", "PASSED", "2nd reading of Bill C-12, An Act to amend the Old Age Security Act (Guaranteed Income Supplement)", "335", "0"));
        billList.add(new Bill("30", "44-1", "2022-02-15", "PASSED", "Government Business No. 7 (Proceedings on Bill C-12, An Act to amend the Old Age Security Act (Guaranteed Income Supplement) (amended by unanimous consent))", "214", "118"));
        billList.add(new Bill("29", "44-1", "2022-02-15", "FAILED", "Government Business No. 7 (Proceedings on Bill C-12, An Act to amend the Old Age Security Act (Guaranteed Income Supplement)) (amendment)", "150", "182"));
        billList.add(new Bill("28", "44-1", "2022-02-15", "PASSED", "2nd reading of Bill C-10, An Act respecting certain measures related to COVID-19", "333", "0"));
    }
}