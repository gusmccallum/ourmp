package com.example.ourmp;

public class MP {
    private String name;
    private String party;
    private String label;
    private String riding;
    private String photoURL;

    public MP() {
    }

    public MP(String name, String party, String label, String riding, String photoURL) {
        this.name = name;
        this.party = party;
        this.label = label;
        this.riding = riding;
        this.photoURL = photoURL;
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
}
