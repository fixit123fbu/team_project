package com.example.fixit.fragments.BottomNavFragments.HomeFragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fixit.Models.Issue;
import com.example.fixit.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final float DEFAULT_ZOOM = 15f;
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private List<Issue> mIssues;
    private View mView;

    public MapFragment() {}

    public static MapFragment newInstance(List<Issue> issues){
        MapFragment mapFragment = new MapFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("issues", (ArrayList<? extends Parcelable>) issues);
        mapFragment.setArguments(args);
        return mapFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mIssues = getArguments().getParcelableArrayList("issues");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        MapsInitializer.initialize(getContext());
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        for (Issue anIssue : mIssues) {
            if (anIssue.getLatitude() != null) {
                LatLng marker = new LatLng(anIssue.getLatitude(), anIssue.getLongitude());
                Marker myMark = googleMap.addMarker(new MarkerOptions().title(anIssue.getTitle()).position(marker));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, DEFAULT_ZOOM));
                myMark.showInfoWindow();
            }
        }
    }
}