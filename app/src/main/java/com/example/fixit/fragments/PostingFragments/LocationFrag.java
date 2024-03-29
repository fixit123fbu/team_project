package com.example.fixit.fragments.PostingFragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.fixit.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class LocationFrag extends Fragment{

    private int REQUEST_LOCATION = 1;
    private float DEFAULT_ZOOM = 15f;
    private String places_api_key =  "AIzaSyBR_HirBjq-d46IBvG40f16aqHJ20LHoSw\n";
    private LatLng currentLatLng;
    private Geocoder auxGeo;
    private List<Address> auxAdr = new ArrayList<>();
    private com.example.fixit.Models.Location issueLocation;
    private GoogleMap map = null;
    private FusedLocationProviderClient fusedLocationClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        auxGeo = new Geocoder(getActivity(), Locale.getDefault());
        View v = inflater.inflate(R.layout.location_fragment, container, false);

        requestLocationPermissions();

        initializeMapWithCurrentLocation();

        initializeAutocomplete();

        return v;
    }

    private void requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
    }

    private void initializeMapWithCurrentLocation(){
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragMapLoc);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                MapsInitializer.initialize(getContext());
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            try {
                                currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                auxAdr = auxGeo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                issueLocation = new com.example.fixit.Models.Location(
                                        location.getLatitude(),
                                        location.getLongitude(),
                                        auxAdr.get(0).getAddressLine(0),
                                        auxAdr.get(0).getFeatureName());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM));
                            map.addMarker(new MarkerOptions().position(currentLatLng));
                            Toast.makeText(getContext(), "Current location loaded", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }


    private void initializeAutocomplete(){
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), places_api_key);
        }

        // Initialize Places.
        Places.initialize(getContext(), places_api_key);


        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(getContext());

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_loc);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                issueLocation = new com.example.fixit.Models.Location(
                        place.getLatLng().latitude,
                        place.getLatLng().longitude,
                        place.getAddress(),
                        place.getName());
                currentLatLng = place.getLatLng();
                if(map != null)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM));
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error
                Toast.makeText(getContext(), "Failed to calculate location", Toast.LENGTH_LONG).show();
            }
        });
    }

    public com.example.fixit.Models.Location getIssueLocation(){
        return issueLocation;
    }
}