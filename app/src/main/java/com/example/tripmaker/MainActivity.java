package com.example.tripmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends AppCompatActivity implements RegistrationFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(R.id.mainActivityContainer, new RegistrationFragment(), "f_registration").commit();
    }


    @Override
    public void onFragmentChange(String state) {
        if (state == RegistrationFragment.FRAGMENT_STATE) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityContainer, new LoginFragment(), "f_login").commit();
        } else if (state == LoginFragment.FRAGMENT_STATE) {
            getSupportFragmentManager().beginTransaction().replace(R.id.mainActivityContainer, new RegistrationFragment(), "f_registration").commit();
        }
    }
}
