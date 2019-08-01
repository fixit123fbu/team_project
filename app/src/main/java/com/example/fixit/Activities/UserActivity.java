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

public class UserActivity extends FragmentActivity implements PostWizard.OnFinishedPostingListener {

    private final int TIME_POS = 0;
    private final int MAP_POS = 1;
    private final int PRO_POS = 2;
    private final int POST_POS = 3;

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
                switch (item.getItemId()) {
                    case R.id.action_timeline:
                        vpFragSlide.setCurrentItem(TIME_POS);
                        break;
                    case R.id.action_profile:
                        vpFragSlide.setCurrentItem(PRO_POS);
                        break;
                    case R.id.action_post:
                        vpFragSlide.setCurrentItem(POST_POS);
                        break;
                }
//                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        // Sets default selection
        vpFragSlide.setCurrentItem(POST_POS);
    }

    @Override
    public void backToHome() {
        vpFragSlide.setCurrentItem(TIME_POS);
    }
}