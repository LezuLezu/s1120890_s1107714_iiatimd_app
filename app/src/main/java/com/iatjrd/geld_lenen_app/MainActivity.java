package com.iatjrd.geld_lenen_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    User user;
    Loan loan;
    String BASE_URL = "https://geld-lenen.herokuapp.com/api/user/loans";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter<ContentCardAdapter.ContentCardHolder> myAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button toAddCardButton = findViewById(R.id.addButton);
        toAddCardButton.setOnClickListener(this::addCardClick);


        //LOGOUT BUTTON
        Button logoutButton = findViewById(R.id.logoutButton);
        Intent in = getIntent();
        String string = in.getStringExtra("message");
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("logout", "new Logout logout_icon.png");
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Uitloggen bevestigen").
                        setMessage("Weet u zeker dat u wil uitloggen?");
                builder.setPositiveButton("Ja",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent logoutIntent = new Intent(getApplicationContext(),
                                        LoginActivity.class);
                                startActivity(logoutIntent);
                            }
                        });
                builder.setNegativeButton("Nee",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        user = (User) getIntent().getExtras().getSerializable("User");

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();


        List<Loan> loans = new ArrayList<Loan>();


        try{
//        Request Queue via singleton
            RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).
                    getRequestQueue();
            JsonArrayRequest apiCall = new JsonArrayRequest(
                    Request.Method.GET, BASE_URL, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    // If theres no loans yet; show a popup with info
                    if (response.length() == 0) {
//                        Go to dialog function
                        noLoansDialog();
                    } else {
                        try {
//                        Loop over array
                            for (int i = 0; i < response.length(); i++) {
                                //                            fetch object from array
                                JSONObject apiRead = response.getJSONObject(i);
                                //                            fetch data from object
                                int id = (int) apiRead.get("id");
                                String amount = (String) "€" + apiRead.get("amount");
                                String firstName = (String) apiRead.get("firstName");
                                String lastName = (String) apiRead.get("lastName");
                                String title = (String) apiRead.get("title");
                                String createdAt = (String) apiRead.get("createdAt");
                                String phoneNumber = (String) apiRead.get("phoneNumber");
                                String reason;
                                //                            Check for posible null vallues
                                if (!apiRead.isNull("reason")) {
                                    reason = (String) apiRead.get("reason");
                                } else {
                                    reason = "Reason";
                                }
                                String payedOn;
                                if (!apiRead.isNull("payedOn")) {
                                    payedOn = (String) apiRead.get("payedOn");
                                } else {
                                    payedOn = "payedOn";
                                }
                                //                            fill array and add to class
                                loans.add(new Loan(id, amount, firstName, lastName, title, createdAt,
                                        reason, phoneNumber, payedOn));
                            }

                            myAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Log.e("jsonloop", e.toString());
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("volleyError", error.getMessage());
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError{
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", user.getToken());
                    return params;
                }
            };
//              Add to queue
            VolleySingleton.getInstance(this).addToRequestQueue(apiCall);
            myAdapter = new ContentCardAdapter(loans, user);
            recyclerView.setAdapter(myAdapter);

            AppDatabase db = AppDatabase.getInstance(getApplicationContext());


        } catch (Exception e) {
//            Catch request error
            e.printStackTrace();
        }
        //    Load to dao?
        AppDatabase db = AppDatabase.getInstance(this.getApplicationContext());
        new Thread(new InsertLoanTask(db, loans)).start();
//        new Thread(new GetLoanTask(db)).start();
    }

    //ADD CARD BUTTON CLICKED
    public void addCardClick(View v) {
        Intent toAddCardIntent = new Intent(this, AddCardActivity.class);
        toAddCardIntent.putExtra("User", user);
        startActivity(toAddCardIntent);
    }

    private void noLoansDialog(){
//                        Create dialogBox
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//                        Set dialogBox title
        alertDialog.setTitle("Geen leningen!");
//                        Set dialog message
        alertDialog.setMessage("Je hebt momenteel geen leningen openstaan! \n" +
                "Heb je net wat geld uitgeleend? Voeg dan de lening toe na het sluiten van dit bericht.");
//        Disable cancel and out of box closing
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
//                        Set button with listener to close dialog
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Sluit melding",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        alertDialog.show();
    }

}