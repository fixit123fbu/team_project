package com.example.fixit.fragments.DetailsFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fixit.Adapters.CommentsAdapter;
import com.example.fixit.R;

public class CommentFragment extends Fragment {

    private RecyclerView rvComments;
    private String issueKey;
    private CommentsAdapter adapter;

    public static CommentFragment newInstance(String issueKey){
        CommentFragment commentFragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putString("issueKey", issueKey);
        commentFragment.setArguments(args);
        return commentFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.issueKey = getArguments().getString("issueKey", "");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.comments_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvComments = view.findViewById(R.id.rvComments);
        adapter = new CommentsAdapter(getContext(), issueKey);
        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
