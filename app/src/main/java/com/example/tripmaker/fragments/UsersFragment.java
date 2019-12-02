package com.example.tripmaker.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripmaker.R;
import com.example.tripmaker.adapters.TripAdapter;
import com.example.tripmaker.adapters.UsersAdapter;
import com.example.tripmaker.models.Gender;
import com.example.tripmaker.models.Location;
import com.example.tripmaker.models.Trip;
import com.example.tripmaker.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UsersFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private List<User> userList;
    private FirebaseFirestore db;
    RecyclerView recyclerView;
    UsersAdapter usersAdapter;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_users, container, false);
        recyclerView = v.findViewById(R.id.all_users_RV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getAllUsers();
        return v;
    }

    private void getAllUsers() {
        userList = new ArrayList<>();
        db.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                //Map<String, Object> map = documentSnapshot.getData();
                User user = new User();
                user.setId(documentSnapshot.getId());
                user.setLastName((documentSnapshot.getData().get("lastName") != null) ? documentSnapshot.getData().get("lastName").toString() : "");
                user.setEmail((documentSnapshot.getData().get("email") != null) ? documentSnapshot.getData().get("email").toString() : "");
                user.setFirstName((documentSnapshot.getData().get("firstName") != null) ? documentSnapshot.getData().get("firstName").toString() : "");
                user.setGender((documentSnapshot.getData().get("gender") != null) ? documentSnapshot.getData().get("gender").equals("MALE")? Gender.MALE:Gender.FEMALE : null);
                user.setImageUrl((documentSnapshot.getData().get("imageUrl") != null) ? documentSnapshot.getData().get("imageUrl").toString() : null);
                userList.add(user);
            }
            // tripList = (ArrayList<Trip>) queryDocumentSnapshots.toObjects(Trip.class);
            //   tripAdapter.notifyDataSetChanged();
            usersAdapter = new UsersAdapter(userList);
            recyclerView.setAdapter(usersAdapter);
            //progressBar.setVisibility(View.INVISIBLE);
            }
        });
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
