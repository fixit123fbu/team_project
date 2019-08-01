package com.example.fixit.fragments.PostingFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fixit.R;

public class InformationFrag extends Fragment {

    private EditText etTitleInfo;
    private EditText etDescriptionInfo;
    private ImageView ivHeaderInfo;

    InformationFrag(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.information_fragment, container, false);
        etTitleInfo = view.findViewById(R.id.etTitleInfo);
        etDescriptionInfo = view.findViewById(R.id.etDescriptionInfo);
        ivHeaderInfo = view.findViewById(R.id.ivHeaderInfo);
        return view;
    }

    public String getTitle(){
        String s = etTitleInfo.getText().toString();
        return s;
    }

    public String getDescription(){
        return etDescriptionInfo.getText().toString();
    }
}
