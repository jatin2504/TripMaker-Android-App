package com.example.tripmaker.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tripmaker.R;
import com.example.tripmaker.adapters.TripAdapter;
import com.example.tripmaker.models.Location;
import com.example.tripmaker.models.Member;
import com.example.tripmaker.models.Trip;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllTripsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    ArrayList<Trip> tripList;
    RecyclerView recyclerView;
    TripAdapter tripAdapter;
    private FirebaseFirestore db;
    private ProgressBar progressBar;

    public AllTripsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tripList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
    }

    private void getAllTrips() {
        tripList = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        db.collection("trips").orderBy("date").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot documentSnapshots : queryDocumentSnapshots.getDocuments()) {
                    Map<String, Object> map = documentSnapshots.getData();
                    Trip trip = new Trip();
                    trip.setId(documentSnapshots.getId());
                    Map<String, Object> locationMap = (Map<String, Object>) map.get("location");
                    trip.setLocation(new Location((double) locationMap.get("lat"), (double) locationMap.get("lng")));
                    trip.setCoverPhotoUrl(map.get("coverPhotoUrl") != null ? map.get("coverPhotoUrl").toString() : "");
                    ArrayList<Member> members = new ArrayList<>();
                    ArrayList<HashMap<String, String>> membersMapList = (ArrayList<HashMap<String, String>>) map.get("members");
                    for (HashMap<String, String> item : membersMapList) {
                        Member member = new Member();
                        member.setName(item.get("name"));
                        member.setId(item.get("id"));
                        members.add(member);
                    }
                    trip.setMembers(members);
                    trip.setCreatedByEmail(map.get("createdByEmail").toString());
                    trip.setCreatedByName(map.get("createdByName").toString());
                    trip.setLocationName(map.get("locationName").toString());
                    trip.setTitle(map.get("title").toString());
                    trip.setDate((Timestamp) map.get("date"));
                    tripList.add(trip);
                }
                // tripList = (ArrayList<Trip>) queryDocumentSnapshots.toObjects(Trip.class);
                //   tripAdapter.notifyDataSetChanged();
                tripAdapter = new TripAdapter(tripList, getContext());
                recyclerView.setAdapter(tripAdapter);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_trips, container, false);
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
        recyclerView = getView().findViewById(R.id.allTripsRV);
        progressBar = getView().findViewById(R.id.allTripsPB);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        getAllTrips();
        getView().findViewById(R.id.actionBtnChat).setOnClickListener(new View.OnClickListener() {
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
