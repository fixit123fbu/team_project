package com.example.fixit.fragments.BottomNavFragments.HomeFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.fixit.Adapters.FragPagerAdapter;
import com.example.fixit.Models.Issue;
import com.example.fixit.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeManagerFragment extends Fragment {

    private final static String POST_ROUTE = "posts";
    private final static String GET_ISSUES = "getIssues";

    private ViewPager vpHomeManager;
    private TabLayout tlHomeManager;
    private List<Issue> mIssues;
    private FragPagerAdapter adapter;

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

        adapter = new FragPagerAdapter(getChildFragmentManager());

        getIssues();
    }

    public void getIssues() {
        mIssues = new ArrayList<>();
        Query recentPostsQuery = FirebaseDatabase.getInstance().getReference().child(POST_ROUTE).orderByKey();//.endAt("-Lk59IfKS_d2B1MJs8FZ").limitToLast(2);
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot issueSnapshot : dataSnapshot.getChildren()) {
                    mIssues.add(issueSnapshot.getValue(Issue.class));
                }
                adapter.addFragment(TimelineFragment.newInstance(mIssues));
                adapter.addFragment(MapFragment.newInstance(mIssues));
                vpHomeManager.setAdapter(adapter);
                tlHomeManager.setupWithViewPager(vpHomeManager);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(GET_ISSUES, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }
}
