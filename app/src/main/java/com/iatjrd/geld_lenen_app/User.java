package com.iatjrd.geld_lenen_app;

import java.io.Serializable;

public class User implements Serializable {
    private String email;
    private String token;

    public User(String email, String token){
        this.email = email;
        this.token = token;
    }

    public String getEmail(){
        return this.email;
    }

    public String getToken(){
        return this.token;
    }
}
