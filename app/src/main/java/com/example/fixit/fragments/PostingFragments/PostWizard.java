package com.example.fixit.fragments.PostingFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.fixit.Models.Issue;
import com.example.fixit.Models.Location;
import com.example.fixit.R;

import java.util.ArrayList;
import java.util.List;

public class PostWizard extends Fragment{

    private List<Fragment> steps;
    private int position;
    private Button btnBack;
    private Button btnNext;
    public Issue issue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wizard_fragment, container, false);
        btnBack = view.findViewById(R.id.btnBack);
        btnNext = view.findViewById(R.id.btnNext);
        issue = new Issue();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        steps = new ArrayList<>();
        steps.add(new InformationFrag());
        steps.add(new LocationFrag());
        steps.add(new PicturesFrag());
        position = 0;
        changeChildFrag();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    position = position - 1;
                    changeChildFrag();
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < steps.size() - 1){
                    updateIssue();
                    position = position + 1;
                    changeChildFrag();
                }
            }
        });
    }

    private void changeChildFrag(){
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.flChildFragCont, steps.get(position)).commit();
    }



    private void updateIssue(){
        switch (position){
            case 0:
                issue.setTitle(((InformationFrag)steps.get(position)).getTitle());
                issue.setDescription(((InformationFrag)steps.get(position)).getDescription());
                break;
            case 1:
                Location temp = ((LocationFrag)steps.get(position)).getIssueLocation();
                issue.setLocation(temp);
                break;
        }

    }
}
