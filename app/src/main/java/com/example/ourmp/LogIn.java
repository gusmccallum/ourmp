package com.example.ourmp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import io.realm.mongodb.App;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.auth.GoogleAuthType;

public class LogIn extends BaseActivity {

    Button btn_logInEmail;
    SignInButton btn_logInGoogle;
    Button btn_logInFacebook;
    EditText edit_email;
    EditText edit_password;

    GoogleSignInClient googleSignInClient;

    ActivityResultLauncher<Intent> resultLauncher;

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LogIn";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceContentLayout(R.layout.activity_login);

        //Set up Google SSO options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        resultLauncher =
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

        btn_logInGoogle = findViewById(R.id.LoginGoogle_btn);
        btn_logInGoogle.setSize(SignInButton.SIZE_WIDE);
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


    // Google SSO Methods

    private void logUserInGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();

        resultLauncher.launch(signInIntent);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            if (completedTask.isSuccessful()) {
                GoogleSignInAccount account = completedTask.getResult(ApiException.class);
                String token = account.getIdToken();
                Credentials googleCredentials =
                        Credentials.google(token, GoogleAuthType.ID_TOKEN);
                ((MainApplication)getApplication()).getRealmApp().loginAsync(googleCredentials, it -> {
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
    }

    private void logUserInFacebook() {
    //TODO: SSO Facebook Login
    }


}
