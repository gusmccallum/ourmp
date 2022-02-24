package com.example.ourmp;

import androidx.annotation.Nullable;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Subscribed{
    private String id;
    private List<String> MPs;
    private List<String> legislations;

    public Subscribed(){}
    public Subscribed(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public List<String> getMPs() {
        return MPs;
    }

    public List<String> getLegislations() {
        return legislations;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addMPs(String MPs) {
        this.MPs.add(MPs);
    }

    public void addLegislations(String legislations) {
        this.legislations.add(legislations);
    }
 /* @PrimaryKey
  @Required
  private ObjectId _id;
    private RealmList<String> bills;
    private RealmList<String> MPs;
    @Required
    private String userId;

    public Subscribed(){}
    public Subscribed(String id){
        this.userId = id;
    }
    // Standard getters & setters
    public ObjectId get_id() { return _id; }
    public void set_id(ObjectId _id) { this._id = _id; }
    public RealmList<String> getBills() { return bills; }
    public void setBills(RealmList<String> Bills) { this.bills = Bills; }
    public RealmList<String> getMPs() { return MPs; }
    public void setMPs(RealmList<String> MPs) { this.MPs = MPs; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public void addMPs(String MPs) {
        this.MPs.add(MPs);
    }

    public void addBills(String bill) {
        this.bills.add(bill);
    }*/
}
