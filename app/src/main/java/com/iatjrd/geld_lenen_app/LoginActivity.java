package com.iatjrd.geld_lenen_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity{


    User user;



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
        EditText emailText = (EditText)findViewById(R.id.inputEmail);
        EditText passwordText = (EditText)findViewById(R.id.inputPassword);

        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        Log.d("email", email);
        Log.d("password", password);

        String loginURL = "https://geld-lenen.herokuapp.com/api/auth/login";
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, loginURL,
                    jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
//                        Log.d("response", response.toString());
                        String bearerToken = (String) response.get("access_token");
//                        Log.d("token", bearerToken);
                        jsonBody.put("token", bearerToken);
                        user = new User(email, password, bearerToken);
//                        Log.d("user", user.getToken());
                        toMain();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Gefaald", error.getMessage());
                }
            });
            requestQueue.add(loginRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void toMain() {
//        Log.d("UsertokenToMain", user.getToken());
        if(user.getToken() != null) {
            Intent toOverviewScreenIntent = new Intent(this, MainActivity.class);
            startActivity(toOverviewScreenIntent);
        }
    }

    //    register button is pressed
    public void registerClick(View v){
        Log.d("registerTest", "register clicked");
        Intent toRegisterScreenIntent = new Intent(this, RegisterActivity.class);
        startActivity(toRegisterScreenIntent);
    }
}
