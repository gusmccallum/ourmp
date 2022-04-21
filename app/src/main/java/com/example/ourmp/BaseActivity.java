package com.example.ourmp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity
{
    protected DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    MenuItem logInLogOut;





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

        Menu menu = navigationView.getMenu();
        logInLogOut = menu.findItem(R.id.login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (( (MainApplication)getApplication()).getLogInStatus() == true) {
            logInLogOut.setTitle("Log Out");
        }
        else {
            logInLogOut.setTitle("Log In");
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                Intent intent = getIntent();

                if (item.getItemId() == R.id.home)
                {
                     intent = new Intent(BaseActivity.this, MainActivity.class);
                }

                if (item.getItemId() == R.id.findMP)
                {
                    intent = new Intent(BaseActivity.this, LocationActivity.class);
                }

                if (item.getItemId() == R.id.actFeed)
                {
                    intent = new Intent(BaseActivity.this, ActivityFeed.class);
                }

                if (item.getItemId() == R.id.search)
                {
                     intent = new Intent(BaseActivity.this, Search.class);
                }

                if (item.getItemId() == R.id.login)
                {
                    if (logInLogOut.getTitle().equals("Log In")) {
                        intent = new Intent(BaseActivity.this, LogIn.class);
                    }
                    else {
                        ((MainApplication)getApplication()).logOut();
                    }


                }

                if (item.getItemId() == R.id.events)
                {
                    intent = new Intent(BaseActivity.this, Events.class);
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