package com.iatjrd.geld_lenen_app;

import java.util.List;

public class InsertLoanTask implements Runnable{
    AppDatabase db;
    List<Loan> loans;

    public InsertLoanTask(AppDatabase db, List<Loan> loans){
        this.db = db;
        this.loans = loans;
    }
    @Override
    public void run() {
        for(int i = 0; i < this.loans.size(); i++){
            db.loanDAO().InsertLoan(this.loans.get(i));
            int id = db.loanDAO().getAll().get(i).getId();
            String amount = db.loanDAO().getAll().get(i).getAmount();
            String firstName = db.loanDAO().getAll().get(i).getFirstName();
            String lastName = db.loanDAO().getAll().get(i).getLastName();
            String title = db.loanDAO().getAll().get(i).getTitle();
            String createdAt = db.loanDAO().getAll().get(i).getCreatedAt();
            String reason = db.loanDAO().getAll().get(i).getReason();
            String phoneNumber = db.loanDAO().getAll().get(i).getPhoneNumber();
            String payedOn = db.loanDAO().getAll().get(i).getPayedOn();
        }
    }
}
