package com.iatjrd.geld_lenen_app;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Loan{
    @PrimaryKey
    private int id;

    @ColumnInfo
    private String amount;
    @ColumnInfo
    private String firstName;
    @ColumnInfo
    private String lastName;
    @ColumnInfo
    private String title;
    @ColumnInfo
    private String createdAt;
    @ColumnInfo
    private String reason;
    @ColumnInfo
    private String phoneNumber;
    @ColumnInfo
    private String payedOn;

    public Loan(int id, String amount, String firstName, String lastName,
                String title, String createdAt, String reason,
                String phoneNumber, String payedOn
                ){
        this.id = id;
        this.amount = amount;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.createdAt = createdAt;
        this.reason = reason;
        this.phoneNumber = phoneNumber;
        this.payedOn = payedOn;
    }

    public int getId(){return this.id;}
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
