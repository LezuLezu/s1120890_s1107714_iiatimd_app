package com.iatjrd.geld_lenen_app;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Build;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
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
        toAddCardButton.setOnClickListener(this);

//        Button payButton = findViewById(R.id.payButton);
//        payButton.setOnClickListener(this::payClick);

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
//                    Log.d("apiResponse", String.valueOf(response));
                    try{
//                        Loop over array
                        for(int i = 0; i < response.length(); i ++){
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
                            if(!apiRead.isNull("reason")){
                                reason = (String) apiRead.get("reason");
                            }else{
                                reason = "Reason";
                            }
                            String payedOn;
                            if(!apiRead.isNull("payedOn")){
                                payedOn = (String) apiRead.get("payedOn");
                            }else{
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
//                    Log.d("header", params.toString());
                    return params;
                }
            };
//              Add to queue
            VolleySingleton.getInstance(this).addToRequestQueue(apiCall);
            myAdapter = new ContentCardAdapter(loans, user);
            recyclerView.setAdapter(myAdapter);

//            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "LoanDb").allowMainThreadQueries().build();

        } catch (Exception e) {
//            Catch request error
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Log.d("onClickTest", "add clicked");
        Intent toAddCardIntent = new Intent(this, AddCardActivity.class);
        toAddCardIntent.putExtra("User", user);
        startActivity(toAddCardIntent);
    }
}