package com.iatjrd.geld_lenen_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ContentCardAdapter extends RecyclerView.Adapter<ContentCardAdapter.ContentCardHolder> {
    private String[] contentCards;

    public ContentCardAdapter(String[] contentCards){
        this.contentCards = contentCards;
    }

    public static class ContentCardHolder extends RecyclerView.ViewHolder{
        public TextView textView;

        public ContentCardHolder(View v){
            super(v);
            textView = v.findViewById(R.id.textView);
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
        holder.textView.setText(contentCards[position]);

    }

    @Override
    public int getItemCount() {
        return contentCards.length;
    }
}
