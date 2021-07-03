package com.iatjrd.geld_lenen_app;

public class User {
    private String name;
    private String email;
    private String token;

    public User(String name, String email, String token){
        this.name = name;
        this.email = email;
        this.token = token;
    }

    public String getName(){
        return this.name;
    }
    public String getEmail(){
        return this.email;
    }

    public String getToken(){
        return this.token;
    }
}
