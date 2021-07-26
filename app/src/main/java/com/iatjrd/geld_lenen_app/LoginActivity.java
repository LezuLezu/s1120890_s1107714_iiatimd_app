package com.iatjrd.geld_lenen_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity{

    // User model class
    User user;
    // URL to api login
    String loginURL = "https://geld-lenen.herokuapp.com/api/auth/login";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //fetch buttons
        Button loginButton = findViewById(R.id.loginButton);
        Button registerButton = findViewById(R.id.registerButton);
        // add listeners to buttons
        loginButton.setOnClickListener(this::loginClick);
        registerButton.setOnClickListener(this::registerClick);
    }

//    login button is pressed
    public void loginClick(View v) {
        //fetch input fields
        EditText emailText = (EditText)findViewById(R.id.inputEmail);
        EditText passwordText = (EditText)findViewById(R.id.inputPassword);

        // alter inputs to strings
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        // try to make request
        try {
            // make request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            // Json body to send with request
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", email);
            jsonBody.put("password", password);

            //make request
            JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, loginURL,
                    jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String bearerToken = "Bearer " + (String) response.get("access_token");
                        jsonBody.put("token", bearerToken);
                        user = new User(email, bearerToken);
                        toMain();

                    } catch (JSONException e) {
                        // catch error on response
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                     // catch error on Volley
                    Log.e("Gefaald login", error.getMessage());
                    somethingWentWrong();
                }
            });
            requestQueue.add(loginRequest);
        } catch (JSONException e) {
            // catch error on creating jsonbody
            e.printStackTrace();
            somethingWentWrong();
        }
    }

    private void toMain() {
        if(user.getToken() != null) {
            // if user has a token go to loans overview
            Intent toOverviewScreenIntent = new Intent(this, MainActivity.class);
            toOverviewScreenIntent.putExtra("User", user);
            startActivity(toOverviewScreenIntent);
        }else{
            somethingWentWrong();
        }
    }

    //    register button is pressed
    public void registerClick(View v){
        // go to register page
        Intent toRegisterScreenIntent = new Intent(this, RegisterActivity.class);
        startActivity(toRegisterScreenIntent);
    }

    private void somethingWentWrong(){
//                        Create dialogBox
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
//                        Set dialogBox title
        alertDialog.setTitle("Er ging iets mis :(");
//                        Set dialog message
        alertDialog.setMessage("Er ging iets mis met het inloggen, probeer het nog een keertje.");
//        Disable cancel and out of box closing
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
//                        Set button with listener to close dialog
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Sluit melding",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        toLogin();
                    }
                });
        alertDialog.show();
    }
    private void toLogin() {
        // Load login page
        Log.d("register", "succes");
        Intent toLoginScreenIntent = new Intent(this, LoginActivity.class);
        startActivity(toLoginScreenIntent);
    }
}
