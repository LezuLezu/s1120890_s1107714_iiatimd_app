package com.iatjrd.geld_lenen_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
//         add listener to call method
        addButton.setOnClickListener(this::addClick);


    }

    private void addClick(View view) {
        Log.d("addClick", "Add button Clicked");

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
                                Log.d("response", String.valueOf(response));
                                String message = (String) response.get("message");
                                Log.d("message", message);
                                if(message == "Loan added succesfully"){
                                    toOverview();
                                }else{
                                    toAdd();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Gefaald", error.getMessage());
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", user.getToken());
//                    Log.d("header", params.toString());
                    return params;
                }
            };
            requestQueue.add(addLoanRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void toAdd() {
        Intent toAddCardIntent = new Intent(this, AddCardActivity.class);
        toAddCardIntent.putExtra("User", user);
        startActivity(toAddCardIntent);
    }

    private void toOverview() {
        Intent toOverviewScreenIntent = new Intent(this, MainActivity.class);
        toOverviewScreenIntent.putExtra("User", user);
        startActivity(toOverviewScreenIntent);
    }
}
