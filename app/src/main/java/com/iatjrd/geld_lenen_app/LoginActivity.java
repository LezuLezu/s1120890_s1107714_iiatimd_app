package com.iatjrd.geld_lenen_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);
        loginButton.setOnClickListener(this::loginClick);
        registerButton.setOnClickListener(this::registerClick);
    }

//    login button is pressed
    public void loginClick(View v) {
        Log.d("loginTest", "login clicked");
        Intent toOverviewScreenIntent = new Intent(this, MainActivity.class);
        startActivity(toOverviewScreenIntent);
    }

//    register button is pressed
    public void registerClick(View v){
        Log.d("registerTest", "register clicked");
        Intent toRegisterScreenIntent = new Intent(this, RegisterActivity.class);
        startActivity(toRegisterScreenIntent);
    }
}
