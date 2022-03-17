package com.example.ourmp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity
{
    protected DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navView);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.open, R.string.closed);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                Intent intent = getIntent();

                if (item.getItemId() == R.id.home)
                {
                    Toast.makeText(getApplicationContext(), "Clicked Home", Toast.LENGTH_SHORT).show();
                    intent = new Intent(BaseActivity.this, MainActivity.class);
                }

                if (item.getItemId() == R.id.findMP)
                {
                    Toast.makeText(getApplicationContext(), "Clicked Find MP", Toast.LENGTH_SHORT).show();
                    intent = new Intent(BaseActivity.this, LocationActivity.class);
                }

                if (item.getItemId() == R.id.actFeed)
                {
                    Toast.makeText(getApplicationContext(), "Clicked Activity Feed", Toast.LENGTH_SHORT).show();
                    intent = new Intent(BaseActivity.this, ActivityFeed.class);
                }

                if (item.getItemId() == R.id.search)
                {
                    Toast.makeText(getApplicationContext(), "Clicked Search", Toast.LENGTH_SHORT).show();
                    intent = new Intent(BaseActivity.this, Search.class);
                }

                if (item.getItemId() == R.id.signup)
                {
                    Toast.makeText(getApplicationContext(), "Clicked Sign Up", Toast.LENGTH_SHORT).show();
                    //intent = new Intent(BaseActivity.this, SignUp.class);
                }

                if (item.getItemId() == R.id.login)
                {
                    Toast.makeText(getApplicationContext(), "Clicked Login", Toast.LENGTH_SHORT).show();
                    //intent = new Intent(BaseActivity.this, Login.class);
                }

                if (item.getItemId() == R.id.events)
                {
                    Toast.makeText(getApplicationContext(), "Clicked Events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(BaseActivity.this, Events.class);
                }

                if (item.getItemId() == R.id.settings)
                {
                    Toast.makeText(getApplicationContext(), "Clicked Settings", Toast.LENGTH_SHORT).show();
                    //intent = new Intent(BaseActivity.this, Settings.class);
                }

                startActivity(intent);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (toggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void replaceContentLayout(int source)
    {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(source, null, false);
        drawerLayout.addView(contentView, 0);
    }
}