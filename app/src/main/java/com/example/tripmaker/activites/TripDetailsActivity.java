package com.example.tripmaker.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripmaker.R;
import com.example.tripmaker.models.JoinedTrip;
import com.example.tripmaker.models.Member;
import com.example.tripmaker.models.Trip;
import com.example.tripmaker.models.User;
import com.example.tripmaker.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TripDetailsActivity extends AppCompatActivity {

    Trip currentTrip;
    ImageView coverPicIV;
    TextView titleTV;
    TextView locationTV;
    TextView longitude;
    TextView latitudeTV;
    ListView membersLV;
    Button bottomButton;
    private FloatingActionButton actionButton;
    private String userState;
    private final String USER_STATE_ADMIN = "admin";
    private final String USER_STATE_MEMBER = "member";
    private final String USER_STATE_DEFAULT = "default";
    private User userObjSharedPref;
    private FirebaseFirestore db;
    JoinedTrip tripToLeave;
    Member memberToLeave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);

        db = FirebaseFirestore.getInstance();
        SharedPreferences mPrefs = getSharedPreferences("mypref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("userObj", "");
        userObjSharedPref = gson.fromJson(json, User.class);

        coverPicIV = findViewById(R.id.tripDetailsCoverIV);
        titleTV = findViewById(R.id.tripDetailsTitle);
        locationTV = findViewById(R.id.tripDetailsLocVal);
        longitude = findViewById(R.id.lngValTV);
        latitudeTV = findViewById(R.id.latValueTV);
        membersLV = findViewById(R.id.membersLV);
        bottomButton = findViewById(R.id.bottomBtn);
        actionButton = findViewById(R.id.actionBtnChat);


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userState.equals(USER_STATE_DEFAULT)) {
                    Intent i = new Intent(TripDetailsActivity.this, ChatActivity.class);
                    i.putExtra(Constants.TRIP_OBJ_EXTRA, currentTrip);
                    startActivity(i);
                } else {
                    Toast.makeText(TripDetailsActivity.this, "You are not a memebr of this trip", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userState.equals(USER_STATE_DEFAULT)) {
                    joinTrip();
                } else if (userState.equals(USER_STATE_ADMIN)) {
                    deleteTrip();
                } else if (userState.equals(USER_STATE_MEMBER)) {
                    leaveTrip();
                }
            }
        });

        if (getIntent() != null && getIntent().getSerializableExtra(Constants.TRIP_OBJ_EXTRA) != null) {
            currentTrip = (Trip) getIntent().getSerializableExtra(Constants.TRIP_OBJ_EXTRA);
        }

        checkUserState();
        renderUI();
        if (!currentTrip.getCoverPhotoUrl().equals("") && currentTrip.getCoverPhotoUrl() != null) {
            Picasso.get().load(currentTrip.getCoverPhotoUrl()).into(coverPicIV);
        }
        titleTV.setText(currentTrip.getTitle());
        locationTV.setText(currentTrip.getLocationName());
        longitude.setText(Double.toString(currentTrip.getLocation().getLng()));
        latitudeTV.setText(Double.toString(currentTrip.getLocation().getLat()));

        List<Member> members = currentTrip.getMembers();
        List<String> membersName = new ArrayList<>();

        for (Member member : members) {
            membersName.add(member.getName());
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                        android.R.id.text1, membersName);

        membersLV.setAdapter(adapter);
    }

    private void leaveTrip() {

        db.collection("users").document(userObjSharedPref.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                HashMap<String, Object> map = (HashMap<String, Object>) documentSnapshot.getData();
                ArrayList<HashMap<String, Object>> tripsMap = (ArrayList<HashMap<String, Object>>) map.get("trips");
                for (HashMap<String, Object> item : tripsMap) {
                    if (item.get("tripId").equals(currentTrip.getId())) {
                        tripToLeave = new JoinedTrip();
                        tripToLeave.setJoinedDate((Timestamp) item.get("joinedDate"));
                        tripToLeave.setTripId((String) item.get("tripId"));
                        db.collection("users").document(userObjSharedPref.getId()).update("trips", FieldValue.arrayRemove(tripToLeave)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                removeCurrentUserFromTrip();
                            }
                        });
                    }
                }
                userObjSharedPref.removeTrip(tripToLeave);
                storeUserInsharedPreferences(userObjSharedPref);
            }
        });
    }

    private void removeCurrentUserFromTrip() {
        db.collection("trips").document(currentTrip.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                HashMap<String, Object> map = (HashMap<String, Object>) documentSnapshot.getData();
                ArrayList<HashMap<String, Object>> membersMap = (ArrayList<HashMap<String, Object>>) map.get("members");
                for (HashMap<String, Object> item : membersMap) {
                    if (item.get("id").equals(userObjSharedPref.getId())) {
                        memberToLeave = new Member();
                        memberToLeave.setId((String) item.get("id"));
                        memberToLeave.setName((String) item.get("name"));
                        db.collection("trips").document(currentTrip.getId()).update("members", FieldValue.arrayRemove(memberToLeave)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                            }
                        });
                    }
                }
            }
        });
    }

    private void deleteTrip() {
        db.collection("trips").document(currentTrip.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                deleteChat();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void deleteTripFromUsers() {
        List<Member> members = currentTrip.getMembers();
        WriteBatch batch = db.batch();

        for (Member member : members) {
            DocumentReference userRef = db.collection("users").document(member.getId());
            batch.update(userRef, "", FieldValue.arrayRemove());
        }
    }

    private void deleteChat() {
        db.collection("chats").document(currentTrip.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void storeUserInsharedPreferences(User user) {

        SharedPreferences preferences = getSharedPreferences("mypref",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = preferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(user);
        prefsEditor.putString("userObj", json);
        prefsEditor.commit();

    }

    private void joinTrip() {
        JoinedTrip joinedTrip = new JoinedTrip();
        joinedTrip.setTripId(currentTrip.getId());
        joinedTrip.setJoinedDate(Timestamp.now());

        userObjSharedPref.addTrip(joinedTrip);
        storeUserInsharedPreferences(userObjSharedPref);

        db.collection("users").document(userObjSharedPref.getId()).update("trips", FieldValue.arrayUnion(joinedTrip)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                addUserToTrip();
            }
        });
    }

    private void addUserToTrip() {
        Member member = new Member();
        member.setId(userObjSharedPref.getId());
        member.setName(userObjSharedPref.getFirstName() + " " + userObjSharedPref.getLastName());

        db.collection("trips").document(currentTrip.getId()).update("members", FieldValue.arrayUnion(member)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        });
    }

    private void renderUI() {
        if (userState.equals(USER_STATE_ADMIN)) {
            bottomButton.setText("DELETE TRIP");
        } else if (userState.equals(USER_STATE_MEMBER)) {
            bottomButton.setText("LEAVE TRIP");
        } else {
            bottomButton.setText("JOIN TRIP");
        }
    }

    private void checkUserState() {
        if (currentTrip.getCreatedByEmail().equals(userObjSharedPref.getEmail())) {
            userState = USER_STATE_ADMIN;
        } else if (isMember()) {
            userState = USER_STATE_MEMBER;
        } else {
            userState = USER_STATE_DEFAULT;
        }
    }

    private boolean isMember() {
        for (Member member : currentTrip.getMembers()) {
            if (member.getId().equals(userObjSharedPref.getId())) {
                return true;
            }
        }
        return false;
    }
}
