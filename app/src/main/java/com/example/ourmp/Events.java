package com.example.ourmp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Events extends BaseActivity
    {
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            //setContentView(R.layout.activity_third);

            replaceContentLayout(R.layout.activity_events);

            BottomNavigationView botNav = findViewById(R.id.botNav);

            botNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
            {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item)
                {
                    Fragment fragment = null;

                    if (item.getItemId() == R.id.recent)
                    {
                        //Toast.makeText(getApplicationContext(), "Clicked recent events", Toast.LENGTH_SHORT).show();
                        fragment = new RecentFragment();
                    }

                    if (item.getItemId() == R.id.live)
                    {
                        //Toast.makeText(getApplicationContext(), "Clicked live events", Toast.LENGTH_SHORT).show();
                        fragment = new LiveFragment();
                    }

                    if (item.getItemId() == R.id.upcoming)
                    {
                        //Toast.makeText(getApplicationContext(), "Clicked upcoming events", Toast.LENGTH_SHORT).show();
                        fragment = new UpcomingFragment();
                    }

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainerView, fragment).commit();
                    return true;
                }
            });
        }
    }