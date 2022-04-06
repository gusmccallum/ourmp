package com.example.ourmp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

public class LogIn extends BaseActivity {

    Button btn_logInEmail;
    Button btn_logInGoogle;
    Button btn_logInFacebook;
    EditText edit_email;
    EditText edit_password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceContentLayout(R.layout.activity_login);

        edit_email = (EditText) findViewById(R.id.LoginEmail_edit);
        edit_password = (EditText) findViewById(R.id.LoginPassword_edit);
        btn_logInEmail = (Button) findViewById(R.id.LoginEmailLogin_btn);
        btn_logInEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit_email.getText().toString();
                String password = edit_password.getText().toString();
                if (( (MainApplication)getApplication()).getLogInStatus() == true) {
                    Toast.makeText(v.getContext(), "Login failed - already logged in. Sign out first to log in to another account.", Toast.LENGTH_LONG).show();
                }
                else if (password.isEmpty()) {
                    Toast.makeText(v.getContext(), "Login failed - password field is empty.", Toast.LENGTH_LONG).show();
                }
                else if (email.isEmpty()) {
                    Toast.makeText(v.getContext(), "Login failed - email field is empty.", Toast.LENGTH_LONG).show();
                }
                else {
                    logUserInEmail(email, password);
                }
            }
        });

        btn_logInGoogle = (Button) findViewById(R.id.LoginGoogle_btn);
        btn_logInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logUserInGoogle();
            }
        });

        btn_logInFacebook = (Button) findViewById(R.id.LoginFacebook_btn);
        btn_logInFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logUserInFacebook();
            }
        });

        BottomNavigationView botNav = findViewById(R.id.botNav);

        botNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                Intent intent = getIntent();

                if (item.getItemId() == R.id.home)
                {
                    //Toast.makeText(getApplicationContext(), "Clicked recent events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(LogIn.this, MainActivity.class);
                }

                if (item.getItemId() == R.id.search)
                {
                    //Toast.makeText(getApplicationContext(), "Clicked live events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(LogIn.this, Search.class);
                }

                if (item.getItemId() == R.id.events)
                {
                    //Toast.makeText(getApplicationContext(), "Clicked upcoming events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(LogIn.this, Events.class);
                }

                startActivity(intent);
                return true;
            }
        });
    }


    private void logUserInEmail(String email, String password) {
        Credentials credentials = Credentials.emailPassword(email,password);
        ( (MainApplication)getApplication()).getRealmApp().loginAsync(credentials, new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if(result.isSuccess())
                {
                    Log.v("User","Logged In Successfully");
                    ((MainApplication)getApplication()).setLogInStatus(true);
                    DBManager dbManager = ((MainApplication)getApplication()).getDbManager();
                    dbManager.setUserID(((MainApplication)getApplication()).getRealmApp().currentUser().getId());
                    //Toast.makeText(getBaseContext(), "Used logged in successfully.", Toast.LENGTH_LONG).show();

                    Intent homeIntent = new Intent();
                    homeIntent.setClass(getApplicationContext(), MainActivity.class);
                    startActivity(homeIntent);
                    Toast.makeText(getApplicationContext(), "User logged in successfully.", Toast.LENGTH_LONG).show();

                }
                else
                {
                    Log.v("User","Failed to Login");
                    Toast.makeText(getBaseContext(), "Used login failed. Error: ", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void logUserInGoogle() {
    //TODO: SSO Google Login
    }

    private void logUserInFacebook() {
    //TODO: SSO Facebook Login
    }


}
