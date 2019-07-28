package com.example.fixit.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixit.Models.Issue;
import com.example.fixit.Adapters.IssuesAdapter;
import com.example.fixit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TimelineFragment extends Fragment {

    private final static String POST_ROUTE = "posts";

    private RecyclerView rvTimeline;
    private IssuesAdapter adapter;
    private List<Issue> mIssues;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvTimeline = view.findViewById(R.id.rvTimeline);

        // create the data source
        mIssues = new ArrayList<>();
        // create adapter
        adapter = new IssuesAdapter(getContext(), mIssues);
        // set adapter on the recycler view
        rvTimeline.setAdapter(adapter);
        // set layout manager on the recycler view
        rvTimeline.setLayoutManager(new LinearLayoutManager(getContext()));

        getIssues();
    }

    public void getIssues(){
        Query recentPostsQuery = FirebaseDatabase.getInstance().getReference().child(POST_ROUTE).orderByKey().limitToLast(5);//.endAt("-Lk59IfKS_d2B1MJs8FZ").limitToLast(2);
        recentPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot issueSnapshot: dataSnapshot.getChildren()) {
                    Issue temp = issueSnapshot.getValue(Issue.class);
                    mIssues.add(temp);
                    adapter.notifyDataSetChanged();
                    Log.d("getting", issueSnapshot.getValue(Issue.class).getDescription());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
