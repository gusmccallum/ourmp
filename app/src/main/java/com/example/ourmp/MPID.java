package com.example.ourmp;

public class MPID {
    private String MPName;
    private String MPID;

    public String getMPParty() {
        return MPParty;
    }

    public void setMPParty(String MPParty) {
        this.MPParty = MPParty;
    }

    private String MPParty;

    public MPID(String name, String ID, String party) {
        this.MPName = name;
        this.MPID = ID;
        this.MPParty = party;
    }

    public String getMPName() {
        return MPName;
    }

    public void setMPName(String MPName) {
        this.MPName = MPName;
    }

    public String getMPID() {
        return MPID;
    }

    public void setMPID(String MPID) {
        this.MPID = MPID;
    }
}
