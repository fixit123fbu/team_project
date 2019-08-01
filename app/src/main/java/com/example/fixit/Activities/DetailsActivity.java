package com.example.fixit.Activities;

import android.os.Bundle;
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

public class DetailsActivity extends FragmentActivity implements OnMapReadyCallback {

    float DEFAULT_ZOOM = 15f;
    private GoogleMap mMap;
    String INTENT_ISSUE_EXTRA = DetailsActivity.class.getSimpleName();
    String INTENT_DATE_EXTRA = "date";

    SeekBar sbUrgency;
    SeekBar sbDanger;
    SeekBar sbUtility;
    Issue issue;
    TextView tvTitleDetails;
    TextView tvTimestampDetails;
    TextView tvDescriptionDetails;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        viewPager = findViewById(R.id.vpImageDetails);
        sbUrgency =findViewById(R.id.sbUrgency);
        sbDanger = findViewById(R.id.sbDanger);
        sbUtility = findViewById(R.id.sbUtility);
        tvTitleDetails = findViewById(R.id.tvTitleDetails);
        tvTimestampDetails = findViewById(R.id.tvTimestampDetails);
        tvDescriptionDetails = findViewById(R.id.tvDescriptionDetails);

        issue = getIntent().getParcelableExtra(INTENT_ISSUE_EXTRA);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        CommentFragment commentFragment = CommentFragment.newInstance(issue);
        ft.replace(R.id.flComments, commentFragment);
        ft.commit();

        tvTitleDetails.setText(issue.getTitle());
        tvTimestampDetails.setText(getIntent().getStringExtra(INTENT_DATE_EXTRA));
        tvDescriptionDetails.setText(issue.getDescription());

        pagerAdapter = new PagerAdapter(DetailsActivity.this, issue);
        viewPager.setAdapter(pagerAdapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragMap);
        mapFragment.getMapAsync(this);

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng marker = new LatLng(issue.getLatitude(), issue.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, DEFAULT_ZOOM));
    }
}
