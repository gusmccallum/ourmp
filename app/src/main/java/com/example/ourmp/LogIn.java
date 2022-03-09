package com.example.ourmp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;

public class LogIn extends AppCompatActivity {

    Button btn_logInEmail;
    Button btn_logInGoogle;
    Button btn_logInFacebook;
    EditText edit_email;
    EditText edit_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edit_email = (EditText) findViewById(R.id.LoginEmail_edit);
        edit_password = (EditText) findViewById(R.id.LoginPassword_edit);
        btn_logInEmail = (Button) findViewById(R.id.LoginEmailLogin_btn);
        btn_logInEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit_email.getText().toString();
                String password = edit_password.getText().toString();
                logUserInEmail(email, password);
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

    }


    private void logUserInEmail(String email, String password) {
        Credentials credentials = Credentials.emailPassword(email,password);
        ( (MainApplication)getApplication()).getRealmApp().loginAsync(credentials, new App.Callback<User>() {
            @Override
            public void onResult(App.Result<User> result) {
                if(result.isSuccess())
                {
                    Log.v("User","Logged In Successfully");

                }
                else
                {
                    Log.v("User","Failed to Login");
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
