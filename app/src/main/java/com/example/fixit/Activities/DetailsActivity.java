package com.example.fixit.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.fixit.Adapters.PagerAdapter;
import com.example.fixit.Models.Issue;
import com.example.fixit.R;
import com.example.fixit.fragments.DetailsFragment.CommentFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    private float DEFAULT_ZOOM = 15f;
    private GoogleMap mMap;
    private String INTENT_ISSUE_EXTRA = DetailsActivity.class.getSimpleName();
    private String INTENT_DATE_EXTRA = "date";

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        findViews();
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
                    issue.addComment(etAddComment.getText().toString());
                }
            }
        });
    }

    private void initMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragMapDetails);
        mapFragment.getMapAsync(this);
    }

    private void configureViewPager() {
        pagerAdapter = new PagerAdapter(DetailsActivity.this, issue);
        viewPager.setAdapter(pagerAdapter);
    }

    private void startCommentsFragments() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        CommentFragment commentFragment = CommentFragment.newInstance(issue);
        ft.replace(R.id.flComments, commentFragment);
        ft.commit();
    }

    private void getIssue() {
        issue = getIntent().getParcelableExtra(INTENT_ISSUE_EXTRA);
        tvTitleDetails.setText(issue.getTitle());
//        tvTimestampDetails.setText(getIntent().getStringExtra(INTENT_DATE_EXTRA));
        tvDescriptionDetails.setText(issue.getDescription());
    }

    private void findViews() {
        viewPager = findViewById(R.id.vpImageDetails);
        sbUrgency =findViewById(R.id.sbUtility);
        sbDanger = findViewById(R.id.sbDanger);
        sbUtility = findViewById(R.id.sbUrgency);
        tvTitleDetails = findViewById(R.id.tvTitleDetails);
        tvDescriptionDetails = findViewById(R.id.tvDescriptionDetails);
        etAddComment = findViewById(R.id.etAddComment);
        btnAddComment = findViewById(R.id.btnAddComment);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng marker = new LatLng(issue.getLatitude(), issue.getLongitude());
        Marker myMark = googleMap.addMarker(new MarkerOptions().title(issue.getTitle()).position(marker));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, DEFAULT_ZOOM));
    }
}
