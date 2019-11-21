package com.example.tripmaker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaker.R;
import com.example.tripmaker.models.Trip;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    Context mContext;
    private ArrayList<Trip> mData;

    public TripAdapter(ArrayList<Trip> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
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
        Trip trip = mData.get(position);
        holder.createdByTV.setText(trip.getCreatedByName());
        holder.dateTV.setText(trip.getDate());
        holder.titleTV.setText(trip.getTitle());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV;
        TextView dateTV;
        TextView createdByTV;
        ImageView mapIV;
        ImageView chatIV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.tripTitle);
            dateTV = itemView.findViewById(R.id.tripDateTV);
            createdByTV = itemView.findViewById(R.id.createdByTV);
            mapIV = itemView.findViewById(R.id.mapIV);
            chatIV = itemView.findViewById(R.id.chatIV);
        }
    }
}
