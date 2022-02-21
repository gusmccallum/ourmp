package com.example.ourmp;

import android.os.Parcel;
import android.os.Parcelable;
import android.graphics.Bitmap;
import androidx.annotation.Nullable;

public class MP implements Parcelable {
    private String name;
    private String party;
    private String label;
    private String riding;
    private String photoURL;
    private String twitter;
    private String phone;
    private String email;
    private String ballotURL;
    private Bitmap photo;

    public MP() { }

    public MP(@Nullable String name, @Nullable String party,
              @Nullable String label, @Nullable String riding, @Nullable String photoURL,
              @Nullable String twitter, @Nullable String phone,
              @Nullable String email, @Nullable String ballotURL) {
        this.name = name;
        this.party = party;
        this.label = label;
        this.riding = riding;
        this.photoURL = photoURL;
        this.twitter = twitter;
        this.phone = phone;
        this.email = email;
        this.ballotURL = ballotURL;
    }
    protected MP(Parcel in) {
        name = in.readString();
        party = in.readString();
        label = in.readString();
        riding = in.readString();
        photoURL = in.readString();
        twitter = in.readString();
        phone = in.readString();
        email = in.readString();
        ballotURL = in.readString();
    }
    public void setName(String nameIn) {
        name = nameIn;
    }

    public void setParty(String partyIn) {
        party = partyIn;
    }

    public void setLabel(String labelIn) {
        label = labelIn;
    }

    public void setRiding(String riding) {
        this.riding = riding;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBallotURL(String ballotURL) {
        this.ballotURL = ballotURL;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public String getParty() {
        return party;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getRiding() {
        return riding;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getBallotURL() {
        return ballotURL;
    }

    public static final Creator<MP> CREATOR = new Creator<MP>() {
        @Override
        public MP createFromParcel(Parcel in) {
            return new MP(in);
        }

        @Override
        public MP[] newArray(int size) {
            return new MP[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(party);
        dest.writeString(label);
        dest.writeString(riding);
        dest.writeString(photoURL);
        dest.writeString(twitter);
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeString(ballotURL);
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
