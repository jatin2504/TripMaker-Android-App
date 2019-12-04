package com.example.tripmaker.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.tripmaker.R;
import com.example.tripmaker.models.User;
import com.example.tripmaker.utils.StringValidation;

public class LoginFragment extends Fragment {

    public static final String FRAGMENT_STATE = "Login";

    private OnFragmentInteractionListener mListener;

    private EditText email;
    private EditText password;

    public LoginFragment() {
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
        return inflater.inflate(R.layout.fragment_login, container, false);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        email = getView().findViewById(R.id.loginEmailET);
        password = getView().findViewById(R.id.loginPasswordET);

        getView().findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean validEntries = true;
                if(!StringValidation.validateEmailAddress(email.getText().toString()))
                {
                    email.setError("Enter valid email address");
                    validEntries = false;
                }
                if(!validEntries)
                    return;

                User user = new User(email.getText().toString(), password.getText().toString());
                mListener.loginUser(user);
            }
        });

        getView().findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.signInWithGoogle();
            }
        });

        getActivity().findViewById(R.id.registerTV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentChange(FRAGMENT_STATE);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentChange(String state);

        void loginUser(User user);

        void signInWithGoogle();
    }


}
