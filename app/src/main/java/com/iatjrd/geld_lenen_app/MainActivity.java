package com.iatjrd.geld_lenen_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    User user;
    String BASE_URL = "https://geld-lenen.herokuapp.com/api/user/loans";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button toAddCardButton = findViewById(R.id.addButton);
        toAddCardButton.setOnClickListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.hasFixedSize();

        String[] contentCards = new String[10];

        for(int i = 0; i < contentCards.length; i++){
            contentCards[i] = "bedrag " + i;
        }

        try {
            // make request queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            // Json body to send with request
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Authorization", user.getToken());

            //make request
            JsonObjectRequest apiRequest = new JsonObjectRequest(Request.Method.GET, BASE_URL,
                    jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.d("response", response.toString());
                        String name = (String) response.get("name");


                    } catch (JSONException e) {
                        // catch error on response
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // catch error on Volley
                    Log.d("Gefaald", error.getMessage());
                }

            });
            requestQueue.add(apiRequest);
        } catch (JSONException e) {
            // catch error on creating jsonbody
            e.printStackTrace();
        }




        recyclerViewAdapter = new ContentCardAdapter(contentCards);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public void onClick(View v){
        Log.d("onClickTest", "add clicked");
        Intent toAddCardIntent = new Intent(this, AddCardActivity.class);
        startActivity(toAddCardIntent);
    }
}