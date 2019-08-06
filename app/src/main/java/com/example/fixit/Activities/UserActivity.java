package com.example.fixit.Activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.fixit.Models.FixitUser;
import com.example.fixit.R;
import com.example.fixit.fragments.BottomNavFragments.HomeFragment.HomeManagerFragment;
import com.example.fixit.fragments.BottomNavFragments.ProfileFragment;
import com.example.fixit.fragments.PostingFragments.PostWizard;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserActivity extends FragmentActivity implements PostWizard.OnFinishedPostingListener {

    private static final String USER_TAG = "fixitUser";
    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;
    private FixitUser fixitUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        fixitUser = getIntent().getParcelableExtra(USER_TAG);

        fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_timeline:
                        fragment = new HomeManagerFragment();
                        break;
                    case R.id.action_profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.action_post:
                        fragment = new PostWizard();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flBottomNav, fragment).commit();
                return true;
            }
        });

        backToHome();
    }

    public FixitUser getFixitUser(){
        return fixitUser;
    }

    @Override
    public void backToHome() {
        fragmentManager.beginTransaction().replace(R.id.flBottomNav, new HomeManagerFragment()).commit();
    }
}