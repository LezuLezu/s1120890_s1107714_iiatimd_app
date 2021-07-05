package com.iatjrd.geld_lenen_app;

import java.util.List;

public class GetLoanTask implements Runnable{

//    MainActivity mainActivity;
    AppDatabase db;

    public GetLoanTask(AppDatabase db){
//        this.mainActivity = mainActivity;
        this.db = db;
    }

    @Override
    public void run() {
        Loan[] loans = db.loanDAO().getAll().toArray(new Loan[0]);
//        mainActivity.procesData(loans);
    }
}
