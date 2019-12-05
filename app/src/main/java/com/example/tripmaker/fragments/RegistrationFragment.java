package com.example.tripmaker.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.tripmaker.R;
import com.example.tripmaker.models.Gender;
import com.example.tripmaker.models.User;
import com.example.tripmaker.utils.StringValidation;

public class RegistrationFragment extends Fragment {

    public static final String FRAGMENT_STATE = "Registration";
    private OnFragmentInteractionListener mListener;

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText passWord;
    private RadioGroup genderRg;
    private Gender gender;

    public RegistrationFragment() {
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
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        firstName = getView().findViewById(R.id.fnET);
        lastName = getView().findViewById(R.id.lnET);
        email = getView().findViewById(R.id.emailET);
        passWord = getView().findViewById(R.id.passwordET);
        genderRg = getView().findViewById(R.id.genderRG);

        genderRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.maleRB) {
                    gender = Gender.MALE;
                } else if (i == R.id.femaleRB) {
                    gender = Gender.FEMALE;
                }
            }
        });

        getView().findViewById(R.id.loginTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentChange(FRAGMENT_STATE);
            }
        });

        getView().findViewById(R.id.registerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateForm())
                    return;
                User user = new User(firstName.getText().toString().trim(), lastName.getText().toString().trim(), email.getText().toString().trim(),
                        passWord.getText().toString().trim(), gender);
                mListener.onClickRegister(user);
            }
        });
    }

    private boolean validateForm() {
        boolean validEntries = true;

        if(!StringValidation.validarteName(firstName.getText().toString().trim()))
        {
            firstName.setError("Enter Valid name");
            validEntries = false;
        }

        if(!StringValidation.validarteName(lastName.getText().toString().trim()))
        {
            lastName.setError("Enter Valid name");
            validEntries = false;
        }

        if(!StringValidation.validateEmailAddress(email.getText().toString().trim())){
            email.setError("Enter Valid Email Address");
            validEntries = false;
        }

        if(TextUtils.isEmpty(passWord.getText().toString().trim())||passWord.getText().toString().trim().length()<6){
            if(TextUtils.isEmpty(passWord.getText().toString()))
                passWord.setError("Please enter a password");
            else
                passWord.setError("Password must be atleast 6 charachers long");

            validEntries = false;
        }


        return validEntries;
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

    public interface OnFragmentInteractionListener {
        void onFragmentChange(String state);

        void onClickRegister(User user);
    }
}
