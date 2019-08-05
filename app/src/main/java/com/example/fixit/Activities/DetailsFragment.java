package com.example.fixit.Activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.fixit.Adapters.PagerAdapter;
import com.example.fixit.Models.Comment;
import com.example.fixit.Models.Issue;
import com.example.fixit.R;
import com.example.fixit.fragments.DetailsFragment.CommentFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailsFragment extends Fragment implements OnMapReadyCallback {

    private float DEFAULT_ZOOM = 15f;

    private SeekBar sbUrgency;
    private SeekBar sbDanger;
    private SeekBar sbUtility;
    private Issue issue;
    private TextView tvTitleDetails;
    private TextView tvTimestampDetails;
    private TextView tvDescriptionDetails;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private EditText etAddComment;
    private Button btnAddComment;

    public static DetailsFragment newInstance(Issue issue){
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable("issueDetails", issue);
        detailsFragment.setArguments(args);
        return detailsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        findViews(view);
        setListeners();
        getIssue();
        configureViewPager();
        initMapFragment();
        startCommentsFragments();
    }

    private void setListeners() {
        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etAddComment.getText() != null){
                    issue.addComment(new Comment(etAddComment.getText().toString(), ((UserActivity)getActivity()).getFixitUser()));
                    etAddComment.setText("");
                    hideKeyboardFrom(getContext(), getView());
                }
            }
        });
    }

    private void initMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragMapDetails);
        mapFragment.getMapAsync(this);
    }

    private void configureViewPager() {
        pagerAdapter = new PagerAdapter(getContext(), issue);
        viewPager.setAdapter(pagerAdapter);
    }

    private void startCommentsFragments() {
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        CommentFragment commentFragment = CommentFragment.newInstance(issue.getIssueID());
        ft.replace(R.id.flComments, commentFragment);
        ft.commit();
    }

    private void getIssue() {
        issue = getArguments().getParcelable("issueDetails");
        tvTitleDetails.setText(issue.getTitle());
//        tvTimestampDetails.setText(getIntent().getStringExtra(INTENT_DATE_EXTRA));
        tvDescriptionDetails.setText(issue.getDescription());
    }

    private void findViews(View view) {
        viewPager = view.findViewById(R.id.vpImageDetails);
        sbUrgency = view.findViewById(R.id.sbUtility);
        sbDanger = view.findViewById(R.id.sbDanger);
        sbUtility = view.findViewById(R.id.sbUrgency);
        tvTitleDetails = view.findViewById(R.id.tvTitleDetails);
        tvDescriptionDetails = view.findViewById(R.id.tvDescriptionDetails);
        etAddComment = view.findViewById(R.id.etAddComment);
        btnAddComment = view.findViewById(R.id.btnAddComment);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng marker = new LatLng(issue.getLatitude(), issue.getLongitude());
        googleMap.addMarker(new MarkerOptions().title(issue.getTitle()).position(marker));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, DEFAULT_ZOOM));
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
