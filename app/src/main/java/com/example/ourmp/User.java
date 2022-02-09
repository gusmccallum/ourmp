package com.example.ourmp;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.realm.RealmObject;

public class User extends RealmObject {

    private int id;
    private String email;
    private String password;
    private String google;
    private String facebook;

    public User(){ }

    public User(String email, String password, String google, String facebook){
        this.email = email;
        this.password = password;
        this.google = google;
        this.facebook = facebook;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public void setGoogle(String google) {
        this.google = google;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getGoogle() {
        return google;
    }

    public String getPassword() {
        return password;
    }
}



