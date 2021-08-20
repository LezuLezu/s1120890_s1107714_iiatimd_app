package com.iatjrd.geld_lenen_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ShowCardActivity extends AppCompatActivity implements Serializable {
//    Fetch Loan and user class
    private Loan loan;
    private User user;

    String BASE_URL = "https://geld-lenen.herokuapp.com/api/user/loans/";

//    fetch textviews
    public TextView title;
    public TextView amount;
    public TextView lastName;
    public TextView firstName;
    public TextView phoneNumber;
    public TextView description;
    public TextView createdAt;

    @Override
    protected void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_show_card);

//        Fetch loan id
        Serializable loanID = getIntent().getExtras().getSerializable("Loan");
        String loanIDStr = (String) String.valueOf(loanID);
//        fetch user from intent
        user = (User) getIntent().getExtras().getSerializable("User");

//        Set textviews
        title = findViewById(R.id.showCardTitle);
        amount = findViewById(R.id.showCardAmount);
        firstName = findViewById(R.id.showCardFirstName);
        lastName = findViewById(R.id.showCardLastName);
        phoneNumber = findViewById(R.id.showCardPhoneNumber);
        description = findViewById(R.id.showCardDescription);
        createdAt = findViewById(R.id.showCardCreatedAt);
//        Api call
        FetchData(loanIDStr);

//        Add buttons and set listeners
//        Pay button
        Button payButton = findViewById(R.id.showCardPayButton);
        payButton.setOnClickListener((view)->payClick(view, loanIDStr));
//        Back to overview button
        Button previousButton = findViewById(R.id.showCardPreviousButton);
        previousButton.setOnClickListener(this::previousClick);
    }

    private void FetchData(String loanID) {
        String API_URL = BASE_URL + loanID;
        try {
//            Request Queue via singleton
            RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).
                    getRequestQueue();
            JsonArrayRequest apiCall = new JsonArrayRequest(
                    Request.Method.GET, API_URL, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("Response", String.valueOf(response));
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject apiRead = response.getJSONObject(i);
                            Log.d("ApiRead", String.valueOf(apiRead));
                            String amountRead = (String) "€" + apiRead.get("amount");
                            String firstNameRead = (String) apiRead.get("firstName");
                            String lastNameRead = (String) apiRead.get("lastName");
                            String titleRead = (String) apiRead.get("title");
                            String createdAtRead = (String) apiRead.get("createdAt");
                            String phoneNumberRead = (String) apiRead.get("phoneNumber");
                            String reasonRead;
//                            Check for posible null vallues
                            if(!apiRead.isNull("reason")){
                                reasonRead = (String) apiRead.get("reason");
                            }else{
                                reasonRead = "Geen omschrijving";
                            }
//                            Set data
                            title.setText(titleRead);
                            amount.setText(amountRead);
                            firstName.setText(firstNameRead);
                            lastName.setText(lastNameRead);
                            phoneNumber.setText(phoneNumberRead);
                            createdAt.setText(createdAtRead);
                            description.setText(reasonRead);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("volleyError", error.getMessage());
                }
            }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", user.getToken());
                    return params;
                }
            };
//            Add to Queue
            VolleySingleton.getInstance(this).addToRequestQueue(apiCall);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    Back to previous activity
        private void previousClick(View view) {
        finish();
    }

    private void payClick(View view, String loanID) {
        //        Url to alter loan to payed
        String PAY_URL = "https://geld-lenen.herokuapp.com/api/user/pay-loan/" + loanID;
        try {
//        Queue
            RequestQueue queue = Volley.newRequestQueue(view.getContext());
//            request
            JsonObjectRequest payRequest = new JsonObjectRequest(
                    Request.Method.POST, PAY_URL, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        String message = (String) response.get("message");
                        Log.d("Betaald", message);
                        somethingWentRight(view.getContext());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        somethingWentWrong();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("PayRequestFailed", error.getMessage());
                }
            }) {
                @Override
//                  Sent token along
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", user.getToken());
                    return params;
                }
            };
//            Add to queue
            queue.add(payRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void somethingWentRight(Context view) {
//                        Create dialogBox
        AlertDialog alertDialog = new AlertDialog.Builder(ShowCardActivity.this).create();
//                        Set dialogBox title
        alertDialog.setTitle("Je lening is terugbetaald :)");
//                        Set dialog message
        alertDialog.setMessage("Top! Je lening is betaald en en van het overzicht verwijderd. \n" +
                "Als je deze melding sluit ga je terug naar het overzicht.");
//        Disable cancel and out of box closing
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
//                        Set button with listener to close dialog
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Sluit melding",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        toOverview(view);
                    }
                });
        alertDialog.show();
    }

//    Pay request went wrong
    private void somethingWentWrong(){
//                        Create dialogBox
        AlertDialog alertDialog = new AlertDialog.Builder(ShowCardActivity.this).create();
//                        Set dialogBox title
        alertDialog.setTitle("Er ging iets mis :(");
//                        Set dialog message
        alertDialog.setMessage("Er ging iets mis met het betalen van je lening, probeer het nog een keertje.");
//        Disable cancel and out of box closing
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
//                        Set button with listener to close dialog
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Sluit melding",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        toMoreInfo();
                    }
                });
        alertDialog.show();
    }

//  Back to overview
    private void toOverview(Context context){
        Intent toOverviewScreenIntent = new Intent(this, MainActivity.class);
        toOverviewScreenIntent.putExtra("User", user);
        startActivity(toOverviewScreenIntent);
    }
//    back to more Info
    private void toMoreInfo() {
        Intent toShowCard = new Intent(this, ShowCardActivity.class);
        toShowCard.putExtra("Loan", loan.getId());
        toShowCard.putExtra("User", user);
        startActivity(toShowCard);
    }
}