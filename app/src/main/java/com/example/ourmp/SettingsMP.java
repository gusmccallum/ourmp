package com.example.ourmp;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

public class SettingsMP extends BaseActivity implements SettingsMPRecyclerInterface {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_mp);

        recyclerView = findViewById(R.id.SettingsMP_recycler);
    }

    @Override
    public void onLongItemClick(int position) {

    }
}
