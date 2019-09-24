package com.example.tourmate.pojos;

public class WalletCost {
    private String expenseid,costtitle;
    private int costamount;

    public WalletCost() {
    }

    public WalletCost(String expenseid, String costtitle, int costamount) {
        this.expenseid = expenseid;
        this.costtitle = costtitle;
        this.costamount = costamount;
    }

    public String getExpenseid() {
        return expenseid;
    }

    public void setExpenseid(String expenseid) {
        this.expenseid = expenseid;
    }

    public String getCosttitle() {
        return costtitle;
    }

    public void setCosttitle(String costtitle) {
        this.costtitle = costtitle;
    }

    public int getCostamount() {
        return costamount;
    }

    public void setCostamount(int costamount) {
        this.costamount = costamount;
    }
}
