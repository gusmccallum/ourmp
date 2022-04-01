package com.example.ourmp;

public class Bill
{
    private String billNum;
    private String billSession;
    private String billDate;
    private String billResult;
    private String billDesc;
    private String yesVotes;
    private String noVotes;
    private String voteURL;
    private boolean expanded;

    public Bill(String billNum, String billSession, String billDate, String billResult, String billDesc, String yesVotes, String noVotes, String voteURL)
    {
        this.billNum = billNum;
        this.billSession = billSession;
        this.billDate = billDate;
        this.billResult = billResult;
        this.billDesc = billDesc;
        this.yesVotes = yesVotes;
        this.noVotes = noVotes;
        this.expanded = false;
        if(voteURL != "") {
            this.voteURL = "https://api.openparliament.ca" + voteURL + "?format=json";
        }else{
            this.voteURL = "";
        }
    }

    public String getBillNum()
    {
        return billNum;
    }

    public void setBillNum(String billNum)
    {
        this.billNum = billNum;
    }

    public String getBillSession()
    {
        return billSession;
    }

    public void setBillSession(String billSession)
    {
        this.billSession = billSession;
    }

    public String getBillDate()
    {
        return billDate;
    }

    public void setBillDate(String billDate)
    {
        this.billDate = billDate;
    }

    public String getBillResult()
    {
        return billResult;
    }

    public void setBillResult(String billResult)
    {
        this.billResult = billResult;
    }

    public String getBillDesc()
    {
        return billDesc;
    }

    public void setBillDesc(String billDesc)
    {
        this.billDesc = billDesc;
    }

    public String getYesVotes()
    {
        return yesVotes;
    }

    public void setYesVotes(String yesVotes)
    {
        this.yesVotes = yesVotes;
    }

    public String getNoVotes()
    {
        return noVotes;
    }

    public void setNoVotes(String noVotes)
    {
        this.noVotes = noVotes;
    }

    public boolean isExpanded()
    {
        return expanded;
    }

    public void setExpanded(boolean expanded)
    {
        this.expanded = expanded;
    }

    public String getVoteURl(){ return voteURL; };
}
