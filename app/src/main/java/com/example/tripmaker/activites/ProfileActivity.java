package com.example.tripmaker.activites;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tripmaker.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
                } else {
                    edit = true;
                    showEdit();
                }
            }
        });
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
