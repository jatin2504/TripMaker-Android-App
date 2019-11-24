package com.example.tripmaker.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.tripmaker.R;
import com.example.tripmaker.adapters.PageAdapter;
import com.example.tripmaker.fragments.AllTripsFragment;
import com.example.tripmaker.fragments.MyTripsFragment;
import com.example.tripmaker.fragments.UsersFragment;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class DashboardActivity extends AppCompatActivity implements AllTripsFragment.OnFragmentInteractionListener {

    ViewPager viewPager;
    TabLayout tabLayout;
    PageAdapter pageAdapter;
    TabItem allTripsTab;
    TabItem myTripsTab;
    TabItem usersTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        allTripsTab = findViewById(R.id.allTripsTab);
        myTripsTab = findViewById(R.id.myTripsTab);
        usersTab = findViewById(R.id.usersTab);

        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        pageAdapter.addFragment(new AllTripsFragment());
        pageAdapter.addFragment(new MyTripsFragment());
        pageAdapter.addFragment(new UsersFragment());
        viewPager.setAdapter(pageAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        findViewById(R.id.logoutIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.mAuth.signOut();
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        findViewById(R.id.profileIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActionButtonClicked() {
        Intent i = new Intent(this, NewTripActivity.class);
        startActivity(i);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Toast.makeText(this, "Activity Resumed", Toast.LENGTH_SHORT).show();
    }
}
