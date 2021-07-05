package com.iatjrd.geld_lenen_app;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContentCardAdapter extends RecyclerView.Adapter<ContentCardAdapter.ContentCardHolder> {

    private List<Loan> loans;

    public ContentCardAdapter(List<Loan> loans){
        this.loans = loans;
        Log.d("constructor", String.valueOf(loans));
    }

    public static class ContentCardHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView amount;
        public TextView lastName;
        public TextView firstName;
        public TextView description;


        public ContentCardHolder(View v){
            super(v);
            title = v.findViewById(R.id.cardTitle);
            amount = v.findViewById(R.id.cardAmount);
            lastName = v.findViewById(R.id.cardLastName);
            firstName = v.findViewById(R.id.cardFirstName);
            description = v.findViewById(R.id.cardDescription);
        }
    }

    @NonNull
    @Override
    public ContentCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        ContentCardHolder contentCardHolder = new ContentCardHolder(v);
        return contentCardHolder;
    }

    //ADDING ALL TEXTS FROM API
    @Override
    public void onBindViewHolder(@NonNull ContentCardHolder holder, int position) {
        holder.title.setText(loans.get(position).getTitle());
        holder.amount.setText(loans.get(position).getAmount());
        holder.firstName.setText(loans.get(position).getFirstName());
        holder.lastName.setText(loans.get(position).getLastName());
        holder.description.setText(loans.get(position).getReason());
    }

    @Override
    public int getItemCount() {
        return loans.size();
    }
}