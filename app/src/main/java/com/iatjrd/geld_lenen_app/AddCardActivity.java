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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

public class AddCardActivity extends AppCompatActivity implements Serializable {

//         URL to add card
    String BASE_URL = "https://geld-lenen.herokuapp.com/api/user/add-loan";

    //      Fetch Userclass
    User user;

    @Override
    protected void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_add_card);

//        Fetch user from Intent to use token
        user = (User) getIntent().getExtras().getSerializable("User");
//        Fetch button
        Button addButton = findViewById(R.id.addCardButton);
        Button previousButton = findViewById(R.id.previousButton);
//         add listener to call method
        addButton.setOnClickListener(this::addClick);
        previousButton.setOnClickListener(this::previousClick);
    }

    private void addClick(View view) {

//        Fetch input fields
        EditText titleInput = (EditText) findViewById(R.id.addCardInputTitle);
        EditText amountInput = (EditText) findViewById(R.id.addCardInputAmount);
        EditText firstNameInput = (EditText) findViewById(R.id.addCardInputFirstName);
        EditText lastNameInput = (EditText) findViewById(R.id.addCardInputLastName);
        EditText phoneNumberInput = (EditText) findViewById(R.id.addCardInputPhoneNumber);
        EditText reasonInput = (EditText) findViewById(R.id.addCardInputReason);

//      fetch input strings
        String title = titleInput.getText().toString();
        String amount = amountInput.getText().toString();
        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        String phoneNumber = phoneNumberInput.getText().toString();
        String reason;
//        Handle no input on reason
        if(reasonInput.getText().toString() == ""){
            reason = null;
        }else{
            reason = reasonInput.getText().toString();
        }
//        request
        try{
//            Make requestQueue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
//            Make body to send with request
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("title", title);
            jsonBody.put("amount", amount);
            jsonBody.put("firstName", firstName);
            jsonBody.put("lastName", lastName);
            jsonBody.put("phoneNumber", phoneNumber);
            jsonBody.put("reason", reason);
//            Create Request
            JsonObjectRequest addLoanRequest = new JsonObjectRequest(
                    Request.Method.POST, BASE_URL, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String message = (String) response.get("message");
                                if(message.equals("Loan added succesfully")){
                                    somethingWentRight();
                                }else if(message == "Adding loan failed"){
                                    somethingWentWrong();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", user.getToken());
                    return params;
                }
            };
            requestQueue.add(addLoanRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //PREVIOUS BUTTON IS PRESSED
    private void previousClick(View v){
        finish();
    }

    //SOMETHING INPUT WASNT GOOD SO TRY AGAIN
    private void toAdd() {
        Intent toAddCardIntent = new Intent(this, AddCardActivity.class);
        toAddCardIntent.putExtra("User", user);
        startActivity(toAddCardIntent);
        somethingWentWrong();
    }

    //USER MADE A CARD AND IS SEND TO THE OVERVIEW PAGE
    private void toOverview(){
        Intent toOverviewScreenIntent = new Intent(this, MainActivity.class);
        toOverviewScreenIntent.putExtra("User", user);
        startActivity(toOverviewScreenIntent);
    }

    private void somethingWentRight() {
//                        Create dialogBox
        AlertDialog alertDialog = new AlertDialog.Builder(AddCardActivity.this).create();
//                        Set dialogBox title
        alertDialog.setTitle("Lening toegevoegd!");
//                        Set dialog message
        alertDialog.setMessage("Je lening is toegevoegd, als je deze melding sluit ga je terug naar het overzicht van je openstaande leningen.");
//        Disable cancel and out of box closing
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
//                        Set button with listener to close dialog
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Sluit melding",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        toOverview();
                    }
                });
        alertDialog.show();
    }

    private void somethingWentWrong(){
//                        Create dialogBox
        AlertDialog alertDialog = new AlertDialog.Builder(AddCardActivity.this).create();
//                        Set dialogBox title
        alertDialog.setTitle("Er ging iets mis :(");
//                        Set dialog message
        alertDialog.setMessage("Er ging iets mis met het aanmaken van je lening, probeer het nog een keertje.");
//        Disable cancel and out of box closing
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
//                        Set button with listener to close dialog
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Sluit melding",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        toAdd();
                    }
                });
        alertDialog.show();
    }
}
