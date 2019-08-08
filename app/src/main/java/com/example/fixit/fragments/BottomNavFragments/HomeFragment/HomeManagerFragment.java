package com.example.fixit.fragments.BottomNavFragments.HomeFragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.Stack;

public class HomeManagerFragment extends Fragment {

    private final static String POST_ROUTE = "posts";
    private final static String GET_ISSUES = "getIssues";
    private final static int TL_POS = 0;
    private final static int MAP_POS = 1;

    private View view;
    private List<Issue> mIssues;
    private Stack<Issue> sIssues;
    private FragPagerAdapter adapter;
    private EditText etSearch;
    private ImageButton btnSearch;
    private TabLayout tlHomeManager;
    private ViewPager vpHomeManager;
    private Button btnLogOut;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.manager_home, container, false);
        etSearch = view.findViewById(R.id.tvSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        vpHomeManager = view.findViewById(R.id.vpHomeManager);
        tlHomeManager = view.findViewById(R.id.tlHomeFragment);
        toolbar = view.findViewById(R.id.toolbar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        adapter = new FragPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new TimelineFragment());
        adapter.addFragment(new MapFragment());
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterIssuesByTitle();
                hideKeyboardFrom(getContext(), view);
            }
        });
        getIssues(false);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnLogOut.setVisibility(View.GONE);
    }

    public void filterIssuesByTitle(){
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
                updateWithFilteredIssues();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(GET_ISSUES, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    public void filterIssuesByLocation(final String loc){
        mIssues = new ArrayList<>();
        Query recentPostsQuery =  FirebaseDatabase.getInstance().getReference().child(POST_ROUTE);
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot issueSnapshot : dataSnapshot.getChildren()) {
                    String title = issueSnapshot.child("location").child("address").getValue(String.class);;
                    if(title.contains(loc)) {
                        mIssues.add(issueSnapshot.getValue(Issue.class));
                    }
                }
                updateWithFilteredIssues();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(GET_ISSUES, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }


    public void getIssues(boolean byLikes) {
        mIssues = new ArrayList<>();
        Query recentPostsQuery;
        recentPostsQuery = FirebaseDatabase.getInstance().getReference().child(POST_ROUTE);//.endAt("-Lk59IfKS_d2B1MJs8FZ").limitToLast(2);
        if(!byLikes){
            recentPostsQuery.orderByKey();
        }else{
             recentPostsQuery.orderByChild("fixvotes");
        }
        recentPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sIssues = new Stack<Issue>();
                for (DataSnapshot issueSnapshot : dataSnapshot.getChildren()) {
                    sIssues.push(issueSnapshot.getValue(Issue.class));
                }
                while(!sIssues.isEmpty()){
                    mIssues.add(sIssues.pop());
                }
                updateWithFilteredIssues();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(GET_ISSUES, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void updateWithFilteredIssues(){
        TimelineFragment tf = ((TimelineFragment)adapter.getItem(TL_POS));
        MapFragment mf = ((MapFragment)adapter.getItem(MAP_POS));
        mf.updateIssues(mIssues);
        vpHomeManager.setAdapter(adapter);
        tlHomeManager.setupWithViewPager(vpHomeManager);
        tf.updateIssues(mIssues);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Sunnyvale:
                filterIssuesByLocation("Sunnyvale");
                return  true;
            case R.id.Berkeley:
                filterIssuesByLocation("Berkeley");
                return true;
            case R.id.Trending:
                getIssues(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
