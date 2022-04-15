package com.example.ourmp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Settings extends BaseActivity {

    Button btn_MPSettings;
    Button btn_BillSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceContentLayout(R.layout.activity_settings);

        btn_MPSettings = (Button) findViewById(R.id.SettingsMP_btn);
        btn_MPSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findMPIntent = new Intent();
                findMPIntent.setClass(getApplicationContext(), SettingsMP.class);
                startActivity(findMPIntent);
            }
        });

        btn_BillSettings = (Button) findViewById(R.id.SettingsBill_btn);
        btn_BillSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findMPIntent = new Intent();
                findMPIntent.setClass(getApplicationContext(), SettingsBill.class);
                startActivity(findMPIntent);
            }
        });
    }


}
