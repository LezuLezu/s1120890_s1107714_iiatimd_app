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

    private Loan[] loans;

    public ContentCardAdapter(List<Loan> loans){
        this.loans = loans.toArray(new Loan[0]);
    }

    public static class ContentCardHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView amount;
        public TextView lastName;
        public TextView firstName;


        public ContentCardHolder(View v){
            super(v);
//            textView = v.findViewById(R.id.textView);
            title = v.findViewById(R.id.cardTitle);
            amount = v.findViewById(R.id.cardAmount);
            lastName = v.findViewById(R.id.cardLastName);
            firstName = v.findViewById(R.id.cardFirstName);
        }
    }

    @NonNull
    @Override
    public ContentCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        ContentCardHolder contentCardHolder = new ContentCardHolder(v);
        return contentCardHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContentCardHolder holder, int position) {
        holder.title.setText((CharSequence) loans[position].getTitle());
        holder.amount.setText((CharSequence) loans[position].getAmount());
        holder.lastName.setText((CharSequence) loans[position].getLastName());
        holder.firstName.setText((CharSequence) loans[position].getFirstName());
        Log.d("In Adapter", "In Adapter after binding");
    }

    @Override
    public int getItemCount() {
        return loans.length;
    }
}