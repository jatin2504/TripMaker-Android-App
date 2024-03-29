package com.example.tripmaker.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaker.R;
import com.example.tripmaker.activites.TripDetailsActivity;
import com.example.tripmaker.models.Trip;
import com.example.tripmaker.models.User;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    Context mContext;
    private ArrayList<Trip> mData;
    User user;

    public TripAdapter(ArrayList<Trip> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;

        SharedPreferences mPrefs = mContext.getSharedPreferences("mypref", mContext.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("userObj", "");
        user = gson.fromJson(json, User.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item_layout, parent, false);

        TripAdapter.ViewHolder viewHolder = new TripAdapter.ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Trip trip = mData.get(position);
        holder.createdByTV.setText(trip.getCreatedByEmail().equals(user.getEmail()) ? "Created by: You" : "Created by: " + trip.getCreatedByName());
        Date tripDate = trip.getDate().toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        holder.dateTV.setText(simpleDateFormat.format(tripDate));
        holder.titleTV.setText(trip.getTitle());
        holder.position = position;
        holder.context = mContext;
        holder.adapter = this;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV;
        TextView dateTV;
        TextView createdByTV;
        int position;
        Context context;
        TripAdapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.tripTitle);
            dateTV = itemView.findViewById(R.id.tripDateTV);
            createdByTV = itemView.findViewById(R.id.createdByTV);
//            mapIV = itemView.findViewById(R.id.mapIV);
//            chatIV = itemView.findViewById(R.id.chatIV);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context, TripDetailsActivity.class);
                    i.putExtra("tripObj", adapter.mData.get(position));
                    context.startActivity(i);
                }
            });
        }
    }
}
