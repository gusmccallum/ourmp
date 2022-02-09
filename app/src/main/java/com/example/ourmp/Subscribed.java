package com.example.ourmp;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Subscribed extends RealmObject {
    private int id;
    private RealmList<String> MPs;
    private RealmList<String> legislations;

    public Subscribed(){}
    public Subscribed(int id, @Nullable RealmList<String> MPs, @Nullable RealmList<String> legislations){
        this.id = id;
        this.MPs = MPs;
        this.legislations = legislations;
    }

    public int getId() {
        return id;
    }

    public RealmList<String> getMPs() {
        return MPs;
    }

    public RealmList<String> getLegislations() {
        return legislations;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addMPs(String MPs) {
        this.MPs.add(MPs);
    }

    public void addLegislations(String legislations) {
        this.legislations.add(legislations);
    }
}
