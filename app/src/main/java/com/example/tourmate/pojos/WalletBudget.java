package com.example.tourmate.pojos;

public class WalletBudget {
    private String budgetid,budgettitle;
    private int budgetamount;

    public WalletBudget() {
    }

    public WalletBudget(String budgetid, String budgettitle, int budgetamount) {
        this.budgetid = budgetid;
        this.budgettitle = budgettitle;
        this.budgetamount = budgetamount;
    }

    public String getBudgetid() {
        return budgetid;
    }

    public void setBudgetid(String budgetid) {
        this.budgetid = budgetid;
    }

    public String getBudgettitle() {
        return budgettitle;
    }

    public void setBudgettitle(String budgettitle) {
        this.budgettitle = budgettitle;
    }

    public int getBudgetamount() {
        return budgetamount;
    }

    public void setBudgetamount(int budgetamount) {
        this.budgetamount = budgetamount;
    }
}
