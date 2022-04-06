package com.example.ourmp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class Ballot implements Parcelable {

    private String ballot;
    private String politicianURL;
    private String voteURL;
    private String billNum;
    private String date;
    //added these very last mins, Bill and Ballot can be combined
    private String session;
    private String desc;


    public Ballot(){

    }
    public Ballot(String ballot, String politicianURL, String voteURL,
                  @Nullable String billNum, @Nullable String date, @Nullable String session, @Nullable String desc){
        this.ballot = ballot;
        this.politicianURL = politicianURL;
        this.voteURL = voteURL;
        this.billNum = billNum;
        this.date = date;
        this.session = session;
        this.desc = desc;
    }

    protected Ballot(Parcel in) {
        ballot = in.readString();
        politicianURL = in.readString();
        voteURL = in.readString();
        billNum = in.readString();
        date = in.readString();
        session = in.readString();
        desc = in.readString();
    }

    public static final Creator<Ballot> CREATOR = new Creator<Ballot>() {
        @Override
        public Ballot createFromParcel(Parcel in) {
            return new Ballot(in);
        }

        @Override
        public Ballot[] newArray(int size) {
            return new Ballot[size];
        }
    };

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

    public String getSession() {
        return session;
    }

    public String getDesc() {
        return desc;
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

    public void setSession(String session) {
        this.session = session;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ballot);
        dest.writeString(politicianURL);
        dest.writeString(voteURL);
        dest.writeString(billNum);
        dest.writeString(date);
        dest.writeString(session);
        dest.writeString(desc);
    }
}

