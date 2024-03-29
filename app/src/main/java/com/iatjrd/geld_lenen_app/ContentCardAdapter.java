package com.iatjrd.geld_lenen_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentCardAdapter extends RecyclerView.Adapter<ContentCardAdapter.ContentCardHolder> {

    private List<Loan> loans;
    private User user;

    public ContentCardAdapter(List<Loan> loans, User user){
        this.loans = loans;
        this.user = user;
    }

    public static class ContentCardHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView amount;
        public TextView lastName;
        public TextView firstName;
        public TextView description;
        public Button payButton;
        public TextView laonId;
        public Button moreInfoButton;


        public ContentCardHolder(View v){
            super(v);
            title = v.findViewById(R.id.cardTitle);
            amount = v.findViewById(R.id.cardAmount);
            lastName = v.findViewById(R.id.cardLastName);
            firstName = v.findViewById(R.id.cardFirstName);
            description = v.findViewById(R.id.cardDescription);

            payButton = v.findViewById(R.id.payButton);
            moreInfoButton = v.findViewById(R.id.moreInfo);
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
        holder.payButton.setOnClickListener((view) -> payButtonClick(view, loans.get(position)));
        holder.moreInfoButton.setOnClickListener((view -> moreInfoClick(view, loans.get(position))));
    }

    //  PayButton
    private void payButtonClick(View view, Loan loan) {

//        Url to alter loan to payed
        String PAY_URL = "https://geld-lenen.herokuapp.com/api/user/pay-loan/" + String.valueOf(loan.getId());
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
                        somethingWentRight(view.getContext());
                    } catch (JSONException e) {
                        e.printStackTrace();
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

    private void backToOverview(Context context) {
        Intent toOverViewIntent = new Intent(context, MainActivity.class);
        toOverViewIntent.putExtra("User", user);
        context.startActivity(toOverViewIntent);

    }
    private void somethingWentRight(Context view){
//                        Create dialogBox
        AlertDialog alertDialog = new AlertDialog.Builder(view).create();
//                        Set dialogBox title
        alertDialog.setTitle("Je lening is terugbetaald :)");
//                        Set dialog message
        alertDialog.setMessage("Top! Je lening is betaald en hier verwijderd.");
//        Disable cancel and out of box closing
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
//                        Set button with listener to close dialog
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Sluit melding",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        backToOverview(view);
                    }
                });
        alertDialog.show();
    }

//    More info button
private void moreInfoClick(View view, Loan loan) {
    Context context = view.getContext();
    Intent toShowCard = new Intent(context, ShowCardActivity.class);
    toShowCard.putExtra("Loan", loan.getId());
    toShowCard.putExtra("User", user);
    context.startActivity(toShowCard);
}

    @Override
    public int getItemCount() {
        return loans.size();
    }
}