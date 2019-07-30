package com.example.fixit.Activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.fixit.Adapters.FragPagerAdapter;
import com.example.fixit.R;
import com.example.fixit.fragments.BottomNavFragments.MapFragment;
import com.example.fixit.fragments.PostingFragments.PostWizard;
import com.example.fixit.fragments.BottomNavFragments.ProfileFragment;
import com.example.fixit.fragments.BottomNavFragments.TimelineFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserActivity extends FragmentActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPager vpFragSlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        vpFragSlide = findViewById(R.id.vpFragSlide);

        FragPagerAdapter adapter = new FragPagerAdapter(fragmentManager);
        adapter.addFragment(new TimelineFragment());
        adapter.addFragment(new MapFragment());
        adapter.addFragment(new ProfileFragment());
        adapter.addFragment(new PostWizard());
        vpFragSlide.setAdapter(adapter);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_timeline:
                        vpFragSlide.setCurrentItem(0);
                        break;
                    case R.id.action_profile:
                        vpFragSlide.setCurrentItem(2);
                        break;
                    case R.id.action_post:
                        vpFragSlide.setCurrentItem(3);
                        break;
                }
//                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        // Sets default selection
        vpFragSlide.setCurrentItem(0);
//        bottomNavigationView.setSelectedItemId(R.id.action_timeline);
    }
}