package com.example.tripmaker.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripmaker.R;
import com.example.tripmaker.adapters.TripAdapter;
import com.example.tripmaker.models.Trip;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AllTripsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    ArrayList<Trip> tripList;
    RecyclerView recyclerView;


    public AllTripsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tripList = new ArrayList<>();
        try {
            tripList.add(new Trip("Created By: Jatin Gupte", "Smokey Mountains", new Timestamp(new SimpleDateFormat("MM-dd-yyyy").parse("11-11-2019"))));
            tripList.add(new Trip("Created By: John Doe", "New York City Tour", new Timestamp(new SimpleDateFormat("MM-dd-yyyy").parse("11-11-2019"))));
            tripList.add(new Trip("Created By: John Carter", "Yosemite National Park", new Timestamp(new SimpleDateFormat("MM-dd-yyyy").parse("11-11-2019"))));
            tripList.add(new Trip("Created By: Jatin Gupte", "Smokey Mountains", new Timestamp(new SimpleDateFormat("MM-dd-yyyy").parse("11-11-2019"))));
            tripList.add(new Trip("Created By: John Doe", "New York City Tour", new Timestamp(new SimpleDateFormat("MM-dd-yyyy").parse("11-11-2019"))));
            tripList.add(new Trip("Created By: John Carter", "Yosemite National Park", new Timestamp(new SimpleDateFormat("MM-dd-yyyy").parse("11-11-2019"))));
            tripList.add(new Trip("Created By: Jatin Gupte", "Smokey Mountains", new Timestamp(new SimpleDateFormat("MM-dd-yyyy").parse("11-11-2019"))));
            tripList.add(new Trip("Created By: John Doe", "New York City Tour", new Timestamp(new SimpleDateFormat("MM-dd-yyyy").parse("11-11-2019"))));
            tripList.add(new Trip("Created By: John Carter", "Yosemite National Park", new Timestamp(new SimpleDateFormat("MM-dd-yyyy").parse("11-11-2019"))));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_trips, container, false);
        recyclerView = v.findViewById(R.id.allTripsRV);
        TripAdapter tripAdapter = new TripAdapter(tripList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(tripAdapter);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().findViewById(R.id.actionButtonAllTrips).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onActionButtonClicked();
            }
        });
    }

    public interface OnFragmentInteractionListener {
        void onActionButtonClicked();
    }
}
