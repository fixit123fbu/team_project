package com.example.fixit.fragments.BottomNavFragments.HomeFragment;

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

import com.example.fixit.Adapters.IssuesAdapter;
import com.example.fixit.Models.Issue;
import com.example.fixit.R;

import java.util.List;

public class TimelineFragment extends Fragment {

    private RecyclerView rvTimeline;
    private IssuesAdapter adapter;
    private List<Issue> mIssues;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TimelineFragment", "Issues received");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_timeline, container, false);
        rvTimeline = view.findViewById(R.id.rvTimeline);
        return view;
    }

    public void updateIssues(List<Issue> issues){
//        this.mIssues = getArguments().getParcelableArrayList("issues");
//        this.mIssues = ((HomeManagerFragment)getParentFragment()).getmIssues();
        this.mIssues = issues;
        // create adapter
        adapter = new IssuesAdapter(getContext(), mIssues);
        // set adapter on the recycler view
        rvTimeline.setAdapter(adapter);
        // set layout manager on the recycler view
        rvTimeline.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
