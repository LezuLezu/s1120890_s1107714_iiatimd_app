package com.iatjrd.geld_lenen_app;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Loan implements Serializable {
    private String amount;
    private String firstName;
    private String lastName;
    private String title;
    private String createdAt;
    private String reason;
    private String phoneNumber;
    private String payedOn;

    public Loan(String amount, String firstName, String lastName,
                String title, String createdAt, String reason,
                String phoneNumber, String payedOn
                ){
        this.amount = amount;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.createdAt = createdAt;
        this.reason = reason;
        this.phoneNumber = phoneNumber;
        this.payedOn = payedOn;
    }

    public String getAmount(){
        return this.amount;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getTitle(){
        return this.title;
    }
    public String getCreatedAt(){
        return this.createdAt;
    }
    public String getReason(){
        return this.reason;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getPayedOn(){
        return this.payedOn;
    }
}
