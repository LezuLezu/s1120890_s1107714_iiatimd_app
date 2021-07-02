package com.iatjrd.geld_lenen_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
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

        recyclerViewAdapter = new ContentCardAdapter(contentCards);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public void onClick(View v){
        Log.d("onClickTest", "add clicked");
        Intent toAddCardIntent = new Intent(this, AddCardActivity.class);
        startActivity(toAddCardIntent);
    }
}