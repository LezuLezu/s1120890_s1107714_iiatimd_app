package com.iatjrd.geld_lenen_app;

public class GetLoanTask implements Runnable{

    MainActivity mainActivity;
    AppDatabase db;

    public GetLoanTask(MainActivity mainActivity, AppDatabase db){
        this.mainActivity = mainActivity;
        this.db = db;
    }

    @Override
    public void run() {
        Loan[] loans = db.loanDAO().getAll().toArray(new Loan[0]);
        mainActivity.procesData(loans);
    }
}
