package com.example.ourmp;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

public class SettingsBill extends BaseActivity implements SettingsBillRecyclerInterface {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_bill);

        recyclerView = findViewById(R.id.SettingsBill_recycler);
    }

    @Override
    public void onLongItemClick(int position) {

    }
}
