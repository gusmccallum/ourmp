package com.example.ourmp;

import androidx.annotation.Nullable;

public class Ballot {

    private String ballot;
    private String politicianURL;
    private String voteURL;
    private String billNum;
    private String date;


    public Ballot(){

    }
    public Ballot(String ballot, String politicianURL, String voteURL, @Nullable String billNum, @Nullable String date){
        this.ballot = ballot;
        this.politicianURL = politicianURL;
        this.voteURL = voteURL;
        this.billNum = billNum;
        this.date = date;
    }

    public String getBallot() {
        return ballot;
    }

    public String getPoliticianURL() {
        return politicianURL;
    }

    public String getVoteURL() {
        return voteURL;
    }

    public String getBillNum() {
        return billNum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setBillNum(String billNum) {
        this.billNum = billNum;
    }

    public void setBallot(String ballot) {
        this.ballot = ballot;
    }

    public void setPoliticianURL(String politicianURL) {
        this.politicianURL = politicianURL;
    }

    public void setVoteURL(String voteURL) {
        this.voteURL = voteURL;
    }
}

