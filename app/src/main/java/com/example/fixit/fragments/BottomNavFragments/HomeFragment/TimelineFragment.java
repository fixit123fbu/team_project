package com.example.fixit.fragments.BottomNavFragments.HomeFragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixit.Adapters.IssuesAdapter;
import com.example.fixit.Models.Issue;
import com.example.fixit.R;

import java.util.ArrayList;
import java.util.List;

public class TimelineFragment extends Fragment {

    private RecyclerView rvTimeline;
    private IssuesAdapter adapter;
    private List<Issue> mIssues;

    public static TimelineFragment newInstance(List<Issue> issues){
        TimelineFragment timelineFragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("issues", (ArrayList<? extends Parcelable>) issues);
        timelineFragment.setArguments(args);
        return timelineFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mIssues = getArguments().getParcelableArrayList("issues");
        Log.d("TimelineFragment", "Issues received");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvTimeline = view.findViewById(R.id.rvTimeline);
        // create adapter
        adapter = new IssuesAdapter(getContext(), mIssues);
        // set adapter on the recycler view
        rvTimeline.setAdapter(adapter);
        // set layout manager on the recycler view
        rvTimeline.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void initializeRecyclerView(List<Issue> arrIssues){
        this.mIssues = arrIssues;
    }
}
