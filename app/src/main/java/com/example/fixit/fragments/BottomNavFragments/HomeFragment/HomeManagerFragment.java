package com.example.fixit.fragments.BottomNavFragments.HomeFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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

    private View view;
    private List<Issue> mIssues;
//    private FragPagerAdapter adapter;
    private EditText etSearch;
    private ImageButton btnSearch;
    private Button btnLogOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.manager_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etSearch = view.findViewById(R.id.tvSearch);
        btnSearch = view.findViewById(R.id.btnSearch);

        getIssues();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterIssues();
            }
        });

        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnLogOut.setVisibility(View.GONE);
//
//        btnLogOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getContext(), SignInActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    public void filterIssues(){
        mIssues = new ArrayList<>();
        final String search = etSearch.getText().toString();
        Query recentPostsQuery =  FirebaseDatabase.getInstance().getReference().child(POST_ROUTE);
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot issueSnapshot : dataSnapshot.getChildren()) {
                    String title = issueSnapshot.child("title").getValue(String.class);;
                    if(title.contains(search)) {
                        mIssues.add(issueSnapshot.getValue(Issue.class));
                    }
                }
                setViewPager();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(GET_ISSUES, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public void getIssues() {
        mIssues = new ArrayList<>();
        Query recentPostsQuery;
        recentPostsQuery = FirebaseDatabase.getInstance().getReference().child(POST_ROUTE).orderByKey();//.endAt("-Lk59IfKS_d2B1MJs8FZ").limitToLast(2);
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot issueSnapshot : dataSnapshot.getChildren()) {
                    mIssues.add(issueSnapshot.getValue(Issue.class));
                }
                setViewPager();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(GET_ISSUES, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void setViewPager(){
        FragPagerAdapter adapter = new FragPagerAdapter(getChildFragmentManager());
        TimelineFragment timelineFragment = TimelineFragment.newInstance(mIssues);
        adapter.addFragment(timelineFragment);
        adapter.addFragment(MapFragment.newInstance(mIssues));
        ViewPager vpHomeManager = view.findViewById(R.id.vpHomeManager);
        TabLayout tlHomeManager = view.findViewById(R.id.tlHomeFragment);
        vpHomeManager.setAdapter(adapter);
        tlHomeManager.setupWithViewPager(vpHomeManager);
    }
}
