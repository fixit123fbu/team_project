package com.example.fixit.fragments.BottomNavFragments.HomeFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.fixit.Adapters.FragPagerAdapter;
import com.example.fixit.R;
import com.google.android.material.tabs.TabLayout;

public class HomeManagerFragment extends Fragment {

    private ViewPager vpHomeManager;
    private TabLayout tlHomeManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manager_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vpHomeManager = view.findViewById(R.id.vpHomeManager);
        tlHomeManager = view.findViewById(R.id.tlHomeFragment);

        FragPagerAdapter adapter = new FragPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new TimelineFragment());
        adapter.addFragment(new MapFragment());

        vpHomeManager.setAdapter(adapter);
        tlHomeManager.setupWithViewPager(vpHomeManager);
    }
}
