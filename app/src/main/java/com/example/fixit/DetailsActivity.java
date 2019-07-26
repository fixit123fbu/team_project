package com.example.fixit;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.fixit.Adapters.PagerAdapter;
import com.example.fixit.Models.Issue;
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
    int images[] = {R.drawable.camaro, R.drawable.datsun, R.drawable.corvette, R.drawable.mustang};
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

        pagerAdapter = new PagerAdapter(DetailsActivity.this, images);
        viewPager.setAdapter(pagerAdapter);

        issue = getIntent().getParcelableExtra(INTENT_ISSUE_EXTRA);
        tvTitleDetails.setText(issue.getTitle());
        tvTimestampDetails.setText(getIntent().getStringExtra(INTENT_DATE_EXTRA));
        tvDescriptionDetails.setText(issue.getDescription());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragMap);
        mapFragment.getMapAsync(this);

//        try {
//            issue.downloadFile(ivIssueDetails);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng marker = new LatLng(issue.getLatitude(), issue.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, DEFAULT_ZOOM));
    }
}
