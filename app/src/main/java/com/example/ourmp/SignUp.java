package com.example.ourmp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.auth.GoogleAuthType;
import kotlin.text.Regex;

public class SignUp extends BaseActivity {

    Button btn_signUpEmail;
    EditText edit_email;
    EditText edit_password;
    EditText edit_confirmPassword;
    TextView txt_login;

    Pattern emailFormat = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceContentLayout(R.layout.activity_signup);

        txt_login = (TextView) findViewById(R.id.SignUpTitle4_txt);
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logInIntent = new Intent();
                logInIntent.setClass(getApplicationContext(), LogIn.class);
                startActivity(logInIntent);
            }
        });

        edit_email = (EditText) findViewById(R.id.SignUpEmail_edit);
        edit_password = (EditText) findViewById(R.id.SignUpPassword_edit);
        edit_confirmPassword = (EditText) findViewById(R.id.SignUpPasswordConfirm_edit);
        btn_signUpEmail = (Button) findViewById(R.id.SignUpEmailSignUp_btn);
        btn_signUpEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit_email.getText().toString();
                String password = edit_password.getText().toString();
                String confirmPassword = edit_confirmPassword.getText().toString();

                Matcher emailMatches = emailFormat.matcher(email);

                if (password.equals(confirmPassword) && emailMatches.matches()) {
                    signUserUpEmail(email, password);
                }
                else if (!password.equals(confirmPassword)) {
                    Toast.makeText(v.getContext(), "Password confirmation doesn't match. Please try again.", Toast.LENGTH_LONG).show();
                }
                else if (!emailMatches.matches()) {
                    Toast.makeText(v.getContext(), "Invalid email. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
        /*
        btn_signUpGoogle = (Button) findViewById(R.id.SignUpGoogle_btn);
        btn_signUpGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
        */

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
                    intent = new Intent(SignUp.this, MainActivity.class);
                }

                if (item.getItemId() == R.id.search)
                {
                    //Toast.makeText(getApplicationContext(), "Clicked live events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(SignUp.this, Search.class);
                }

                if (item.getItemId() == R.id.events)
                {
                    //Toast.makeText(getApplicationContext(), "Clicked upcoming events", Toast.LENGTH_SHORT).show();
                    intent = new Intent(SignUp.this, Events.class);
                }

                startActivity(intent);
                return true;
            }
        });
    }

    private void signUserUpEmail(String email, String password) {
        ( (MainApplication)getApplication()).getRealmApp().getEmailPassword().registerUserAsync(email,password,it->{
            if(it.isSuccess())
            {

                Log.v("User","Registered with email successfully");


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
                            dbManager.addNewUserSubscription();
                            dbManager.getSubscriptionObject();

                            Intent homeIntent = new Intent();
                            homeIntent.setClass(getApplicationContext(), MainActivity.class);
                            startActivity(homeIntent);
                            Toast.makeText(getApplicationContext(), "Account created successfully!", Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "User logged in successfully.", Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Log.v("User","Failed to Login");
                            Toast.makeText(getBaseContext(), "User login failed. Error: " + result.getError().getErrorMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
            else
            {
                Log.v("User","Registration Failed");
                Toast.makeText(getApplicationContext(), "Error creating account."+ it.getError().getErrorMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }
    /*
    private void signInWithGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.server_client_id))
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = googleSignInClient.getSignInIntent();
        ActivityResultLauncher<Intent> resultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                Task<GoogleSignInAccount> task =
                                        GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                                handleSignInResult(task);
                            }
                        });
        resultLauncher.launch(signInIntent);
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            if (completedTask.isSuccessful()) {
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                String token = account.getIdToken();
                Credentials googleCredentials =
                        Credentials.google(token, GoogleAuthType.ID_TOKEN);
                ( (MainApplication)getApplication()).getRealmApp().loginAsync(googleCredentials, it -> {
                    if (it.isSuccess()) {
                        Log.v("AUTH",
                                "Successfully logged in to MongoDB Realm using Google OAuth.");
                    } else {
                        Log.e("AUTH",
                                "Failed to log in to MongoDB Realm: ", it.getError());
                    }
                });
            } else {
                Log.e("AUTH", "Google Auth failed: "
                        + completedTask.getException().toString());
            }
        } catch (ApiException e) {
            Log.w("AUTH", "Failed to log in with Google OAuth: " + e.getMessage());
        }
    }*/



}
