package com.iatjrd.geld_lenen_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity{

    // Url to api register
    String registerURL = "https://geld-lenen.herokuapp.com/api/auth/register";

    @Override
    protected void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_register);

        Button registerButton = findViewById(R.id.RegisterButtonSubmit);
        registerButton.setOnClickListener(this::register);
    }

    private void register(View v) {
        //registersubmit pressed
//        fetch inputs
        EditText nameText = (EditText)findViewById(R.id.InputRegisterName);
        EditText emailText = (EditText)findViewById(R.id.InputRegisterEmail);
        EditText passwordText = (EditText)findViewById(R.id.InputRegisterPassword);
        EditText passwordConfText = (EditText)findViewById(R.id.InputRegisterPasswordConfirm);

//        alter inputs to strings
        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String passwordConf = passwordConfText.getText().toString();

        // Try to make request
        try{
            //Make request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            //Json body to send with request
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name", name);
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            jsonBody.put("password_confirmation", passwordConf);

            // Make request
            JsonObjectRequest registerRequest = new JsonObjectRequest(
                    Request.Method.POST, registerURL, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String message = (String) response.get("message");
                                if (message != null) {
                                    somethingWentRight();
                                } else {
                                    somethingWentWrong();
                                }
                            } catch (JSONException e) {
                                // catch response errors
                                toRegister();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Volley error listener
                    toRegister();
                }
            }

            );
            requestQueue.add(registerRequest);
        } catch (JSONException e) {
            // Catch error of making jsonBody
            toRegister();
        }


    }

    private void toRegister() {
        // Reload register page
        Log.d("regsiter", "failed");
        Intent toRegisterScreenIntent = new Intent(this, RegisterActivity.class);
        startActivity(toRegisterScreenIntent);
    }

    private void toLogin() {
        // Load login page
        Log.d("register", "succes");
        Intent toLoginScreenIntent = new Intent(this, LoginActivity.class);
        startActivity(toLoginScreenIntent);
    }

    private void somethingWentWrong(){
//                        Create dialogBox
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
//                        Set dialogBox title
        alertDialog.setTitle("Er ging iets mis :(");
//                        Set dialog message
        alertDialog.setMessage("Er ging iets mis met het aanmaken van je account, probeer het nog een keertje.");
//        Disable cancel and out of box closing
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
//                        Set button with listener to close dialog
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Sluit melding",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        toRegister();
                    }
                });
        alertDialog.show();
    }
    private void somethingWentRight(){
//                        Create dialogBox
        AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
//                        Set dialogBox title
        alertDialog.setTitle("Je account is aangemaakt!");
//                        Set dialog message
        alertDialog.setMessage("Na het sluiten van de melding wordt je doorgestuurd naar de inlogpagina.");
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
}


