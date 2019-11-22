package com.example.tripmaker.activites;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripmaker.R;
import com.example.tripmaker.adapters.MemberAdapter;
import com.example.tripmaker.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewTripActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_CAMERA_CODE = 1000;
    ImageView coverPhotoIV;
    ProgressBar progressBar;
    Bitmap bitmapUpload = null;

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

            }
        });
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
