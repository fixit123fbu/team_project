package com.example.fixit.fragments.BottomNavFragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fixit.Models.Issue;
import com.example.fixit.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private final static String POST_ROUTE = "posts";
    private static final float DEFAULT_ZOOM = 15f;
    private static final int REQUEST_LOCATION = 4;
    FusedLocationProviderClient fusedLocationClient;
    GoogleMap mGoogleMap;
    MapView mMapView;
    List<Issue> mIssues;
    View mView;
    LatLng currentLatLng;

    public MapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
//        getIssues();

        mMapView = mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    public void getIssues() {
        mIssues = new ArrayList<>();
        Query recentPostsQuery = FirebaseDatabase.getInstance().getReference().child(POST_ROUTE).orderByKey();//.endAt("-Lk59IfKS_d2B1MJs8FZ").limitToLast(2);
        recentPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot issueSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    mIssues.add(issueSnapshot.getValue(Issue.class));
                    Log.d("getting", issueSnapshot.getValue(Issue.class).getDescription());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        MapsInitializer.initialize(getContext());
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        for (Issue anIssue : mIssues) {
//            if (anIssue.getLatitude() != null) {
//                LatLng marker = new LatLng(anIssue.getLatitude(), anIssue.getLongitude());
//                Marker myMark = googleMap.addMarker(new MarkerOptions().title(anIssue.getTitle()).position(marker));
//                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, DEFAULT_ZOOM));
//                myMark.showInfoWindow();
//            }
//        }
    }
}