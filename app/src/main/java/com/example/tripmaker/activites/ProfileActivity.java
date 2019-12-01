package com.example.tripmaker.activites;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tripmaker.R;
import com.example.tripmaker.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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
    private User currentUser;
    private FirebaseFirestore db;


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

        leftCornerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit) {
                    hideEdit();
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
            Picasso.get().load(currentUser.getImageUrl()).into(profileIV);
            firstNameTV.setText(currentUser.getFirstName());
            lastNameTV.setText(currentUser.getLastName());
            emailTV.setText(currentUser.getEmail());
            GenderTV.setText(currentUser.getGender()!=null?currentUser.getGender().toString():"");
        }

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
        //reference.document(currentUser.getId()).update("imageUrl",lasrNameET.getText());
        //
        //
        //
        //                  TODO: ImageURL update
        //
        //
        //
        //

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


}
