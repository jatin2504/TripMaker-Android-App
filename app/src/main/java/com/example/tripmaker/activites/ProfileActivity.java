package com.example.tripmaker.activites;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tripmaker.R;
import com.example.tripmaker.models.Gender;
import com.example.tripmaker.models.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    boolean edit = false;
    private ImageView rightCornerIV;
    private ImageView leftCornerIV;
    private ImageView profileIV;
    private TextView updateProfPicTV;
    private TextView firstNameTV;
    private TextView lastNameTV;
    private TextView emailTV;
    private TextView GenderTV;
    private EditText firstNameET;
    private EditText lasrNameET;
    private RadioGroup genderRG;
    private RadioButton maleRB;
    private RadioButton femaleRB;
    private User currentUser;
    private FirebaseFirestore db;
    Bitmap bitmapUpload = null;
    private static final int CAPTURE_IMAGE_CAMERA_CODE = 1000;
    boolean imageChanged = false;
    String imageChangedURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences mPrefs = getSharedPreferences("mypref", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("userObj", "");
        currentUser = gson.fromJson(json, User.class);

        leftCornerIV = findViewById(R.id.profileBackIV);
        rightCornerIV = findViewById(R.id.profileEditIV);
        profileIV = findViewById(R.id.profileIV);
        updateProfPicTV = findViewById(R.id.updateProfImageTV);
        firstNameTV = findViewById(R.id.profFnVal);
        lastNameTV = findViewById(R.id.profLnVal);
        emailTV = findViewById(R.id.profEmailVal);
        GenderTV = findViewById(R.id.profGenderVal);
        firstNameET = findViewById(R.id.profFnET);
        lasrNameET = findViewById(R.id.profLnET);
        genderRG = findViewById(R.id.profGenderRG);
        maleRB = findViewById(R.id.profMaleRB);
        femaleRB = findViewById(R.id.profFemaleRB);

        leftCornerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit) {
                    hideEdit();
                    imageChanged = false;
                    if(TextUtils.isEmpty(currentUser.getImageUrl()))
                    {
                        profileIV.setImageDrawable(null);
                        profileIV.setImageResource(R.drawable.userprofile);
                    }
                    else
                        Picasso.get().load(currentUser.getImageUrl()).into(profileIV);
                    edit = false;
                } else {
                    edit = true;
                    finish();
                }

            }
        });

        rightCornerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit) {
                    edit = false;
                    if(imageChanged)
                        currentUser.setImageUrl(imageChangedURL);
                    if(genderRG.getCheckedRadioButtonId()==R.id.maleRB)
                        currentUser.setGender(Gender.MALE);
                    else
                        currentUser.setGender(Gender.FEMALE);
                    saveProfile();
                    storeUserInsharedPreferences(currentUser);
                    finish();
                } else {
                    edit = true;
                    showEdit();

                }
            }
        });

        if(currentUser!=null){
            if(!TextUtils.isEmpty(currentUser.getImageUrl()))
                Picasso.get().load(currentUser.getImageUrl()).into(profileIV);
            firstNameTV.setText(currentUser.getFirstName());
            lastNameTV.setText(currentUser.getLastName());
            emailTV.setText(currentUser.getEmail());
            GenderTV.setText(currentUser.getGender()!=null?currentUser.getGender().toString():"");
            firstNameET.setText(currentUser.getFirstName());
            lasrNameET.setText(currentUser.getLastName());
            if(currentUser.getGender().equals(Gender.MALE))
                maleRB.setChecked(true);
            else
                femaleRB.setChecked(true);
        }

        findViewById(R.id.updateProfImageTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, CAPTURE_IMAGE_CAMERA_CODE);
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

    private void saveProfile() {
        db = FirebaseFirestore.getInstance();
        CollectionReference reference = db.collection("users");
        reference.document(currentUser.getId()).update("firstName",firstNameET.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
        reference.document(currentUser.getId()).update("lastName",lasrNameET.getText().toString());
        reference.document(currentUser.getId()).update("imageUrl",currentUser.getImageUrl());
        reference.document(currentUser.getId()).update("gender",currentUser.getGender());

        currentUser.setFirstName(firstNameET.getText().toString());
        currentUser.setLastName(lasrNameET.getText().toString());
    }

    private void hideEdit() {
        rightCornerIV.setImageDrawable(getDrawable(R.drawable.edit));
        leftCornerIV.setImageDrawable(getDrawable(R.drawable.leftarrow));
        updateProfPicTV.setVisibility(View.INVISIBLE);
        firstNameET.setVisibility(View.INVISIBLE);
        lasrNameET.setVisibility(View.INVISIBLE);
        genderRG.setVisibility(View.INVISIBLE);

        firstNameTV.setVisibility(View.VISIBLE);
        lastNameTV.setVisibility(View.VISIBLE);
        GenderTV.setVisibility(View.VISIBLE);
    }

    private void showEdit() {
        rightCornerIV.setImageDrawable(getDrawable(R.drawable.save));
        leftCornerIV.setImageDrawable(getDrawable(R.drawable.close));
        updateProfPicTV.setVisibility(View.VISIBLE);
        firstNameET.setVisibility(View.VISIBLE);
        lasrNameET.setVisibility(View.VISIBLE);
        genderRG.setVisibility(View.VISIBLE);

        firstNameTV.setVisibility(View.INVISIBLE);
        lastNameTV.setVisibility(View.INVISIBLE);
        GenderTV.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAPTURE_IMAGE_CAMERA_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    profileIV.setImageBitmap(imageBitmap);
                    //uploadImage(imageBitmap);
                    bitmapUpload = imageBitmap;
                    uploadDP();
                    imageChanged = true;
                }
                break;
        }
    }

    private void uploadDP() {
        //progressBar.setVisibility(View.VISIBLE);
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
                    imageChangedURL = (task.getResult().toString());
                }
            }
        });
    }


}
