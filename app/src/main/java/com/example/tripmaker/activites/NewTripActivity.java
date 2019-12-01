package com.example.tripmaker.activites;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaker.R;
import com.example.tripmaker.adapters.MemberAdapter;
import com.example.tripmaker.models.ChatRoom;
import com.example.tripmaker.models.JoinedTrip;
import com.example.tripmaker.models.Location;
import com.example.tripmaker.models.Trip;
import com.example.tripmaker.models.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class NewTripActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_CAMERA_CODE = 1000;
    ImageView coverPhotoIV;
    TextView tripNameET;
    TextView placeNameET;
    TextView latET;
    TextView lngET;
    ProgressBar progressBar;
    Bitmap bitmapUpload = null;
    String coverPicUrl = null;
    String tripId = null;

    public static List<User> allUsers = null;
    public static List<User> selectedUsers = new ArrayList<>();
    private FirebaseFirestore db;

    public static RecyclerView recyclerView;
    public static RecyclerView.Adapter mAdapter;
    public static RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);
        db = FirebaseFirestore.getInstance();
        coverPhotoIV = findViewById(R.id.coverPicIV);
        progressBar = findViewById(R.id.profilePB);
        tripNameET = findViewById(R.id.tripNameET);
        placeNameET = findViewById(R.id.placeNameET);
        latET = findViewById(R.id.latET);
        lngET = findViewById(R.id.lngET);

        recyclerView = findViewById(R.id.memberTripsRV);
        recyclerView.setHasFixedSize(true);

        mAdapter = new MemberAdapter(selectedUsers);
        recyclerView.setAdapter(mAdapter);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        findViewById(R.id.uploadImageTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, CAPTURE_IMAGE_CAMERA_CODE);
            }
        });

        findViewById(R.id.addMembersTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allUsers == null) {
                    fetchUsers();
                } else {
                    showPicker();
                }
            }
        });

        findViewById(R.id.newTripCloseIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.newTripSaveIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmapUpload == null)
                    createTrip();
                else
                    uploadCoverPhoto();
            }
        });
    }

    private void uploadCoverPhoto() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        String path = "images/" + UUID.randomUUID() + ".png";
        final StorageReference imageRepo = storageReference.child(path);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapUpload.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRepo.putBytes(data);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return imageRepo.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Log.d("Photo Album", "Image Download URL" + task.getResult());
                    coverPicUrl = task.getResult().toString();
                    createTrip();
                }
            }
        });
    }

    private void createTrip() {
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences mPrefs = getSharedPreferences("mypref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("userObj", "");
        User userObjSharedPref = gson.fromJson(json, User.class);

        final String tripName = tripNameET.getText().toString().trim();
        String locationName = placeNameET.getText().toString().trim();
        Double latitude = Double.parseDouble(latET.getText().toString().trim());
        Double longitude = Double.parseDouble(lngET.getText().toString().trim());

        //TODO: Validations
        Location location = new Location(latitude, longitude);
        Trip trip = new Trip();
        trip.setLocation(location);
        trip.setTitle(tripName);
        trip.setDate(new Timestamp(new Date()));
        trip.setLocationName(locationName);
        trip.setCreatedByName(userObjSharedPref.getFirstName() + " " + userObjSharedPref.getLastName()); //TODO: Take from shared pref.
        trip.setCreatedByEmail(userObjSharedPref.getEmail()); //TODO: Take from shared pref.
        trip.setCoverPhotoUrl(coverPicUrl);
        List<String> members = new ArrayList<>();
        for (User user : selectedUsers) {
            members.add(user.getEmail());
        }
        trip.setMembers(members);

        db.collection("trips").add(trip).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                tripId = documentReference.getId();
                updateUserCollection();
                createChatGroup(tripName);
                progressBar.setVisibility(View.INVISIBLE);
                finish();

            }
        });

    }

    private void updateUserCollection() {
        WriteBatch batch = db.batch();

        for (User user : selectedUsers) {
            DocumentReference userRef = db.collection("users").document(user.getId());
            JoinedTrip joinedTrip = new JoinedTrip();
            joinedTrip.setTripId(tripId);
            joinedTrip.setJoinedDate(new Timestamp(new Date()));
            batch.update(userRef, "trips", FieldValue.arrayUnion(joinedTrip));
        }

        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }

    public void createChatGroup(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(name);
        chatRoom.setTripId(tripId);
        db.collection("chats").document(tripId).set(chatRoom);
    }

    private void showPicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewTripActivity.this);
        builder.setTitle("Add Member");

        final String[] userNames = new String[allUsers.size()];
        for (int i = 0; i < allUsers.size(); i++) {
            User user = allUsers.get(i);
            userNames[i] = user.getFirstName() + " " + user.getLastName() + "(" + user.getEmail() + ")";
        }

        builder.setItems(userNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User selectedUser = allUsers.remove(which);
                selectedUsers.add(selectedUser);
                mAdapter.notifyDataSetChanged();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void fetchUsers() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //progressBar.setVisibility(View.INVISIBLE);
                if (queryDocumentSnapshots.isEmpty()) {
                    Toast.makeText(NewTripActivity.this, "failure to fetch data", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<User> userList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshots : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> map = documentSnapshots.getData();

                        User u = new User(documentSnapshots.getId(), map.get("firstName").toString(), map.get("lastName").toString(), map.get("email").toString());
                        userList.add(u);
                    }
                    allUsers = userList;
                    progressBar.setVisibility(View.INVISIBLE);
                    showPicker();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(NewTripActivity.this, "failure to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAPTURE_IMAGE_CAMERA_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    coverPhotoIV.setImageBitmap(imageBitmap);
                    // uploadImage(imageBitmap);
                    bitmapUpload = imageBitmap;
                }
                break;
        }
    }
}
