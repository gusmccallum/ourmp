package com.example.ourmp;

public class MPID {
    private String MPName;
    private String MPID;

    public MPID(String name, String ID) {
        this.MPName = name;
        this.MPID = ID;
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
