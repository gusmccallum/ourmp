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
        setContentView(R.layout.activity_main);

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

                if (item.getItemId() == R.id.item1)
                {
                    Toast.makeText(getApplicationContext(), "Clicked Find my MP", Toast.LENGTH_SHORT).show();
                    //intent = new Intent(BaseActivity.this, FirstActivity.class);
                }

                if (item.getItemId() == R.id.item2)
                {
                    Toast.makeText(getApplicationContext(), "Clicked Home", Toast.LENGTH_SHORT).show();
                    intent = new Intent(BaseActivity.this, MainActivity.class);
                }

                if (item.getItemId() == R.id.item3)
                {
                    Toast.makeText(getApplicationContext(), "Clicked Calendar", Toast.LENGTH_SHORT).show();
                    //intent = new Intent(BaseActivity.this, ThirdActivity.class);
                }

                if (item.getItemId() == R.id.item4)
                {
                    Toast.makeText(getApplicationContext(), "Clicked Search", Toast.LENGTH_SHORT).show();
                    //intent = new Intent(BaseActivity.this, Search.class);
                }

                if (item.getItemId() == R.id.item5)
                {
                    Toast.makeText(getApplicationContext(), "Clicked Sign Up", Toast.LENGTH_SHORT).show();
                    //intent = new Intent(BaseActivity.this, SignUp.class);
                }

                if (item.getItemId() == R.id.item6)
                {
                    Toast.makeText(getApplicationContext(), "Clicked Settings", Toast.LENGTH_SHORT).show();
                    //intent = new Intent(BaseActivity.this, SixthActivity.class);
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