package com.example.tripmaker.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tripmaker.R;
import com.example.tripmaker.adapters.TripAdapter;
import com.example.tripmaker.models.Trip;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MyTripsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    ArrayList<Trip> tripList;
    RecyclerView recyclerView;
    TripAdapter tripAdapter;
    private FirebaseFirestore db;
    private ProgressBar progressBar;

    public MyTripsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Toast.makeText(getContext(), "My trips on create", Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_my_trips, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

