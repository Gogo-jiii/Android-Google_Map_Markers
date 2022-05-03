package com.example.mapmarkers;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapmarkers.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerDragListener, GoogleMap.OnInfoWindowCloseListener {

    private Button btnOpenMap;
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private Bitmap ic_baseline_location_city;
    private PermissionManager permissionManager;
    private LocationManager locationManager;
    private String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        btnOpenMap = findViewById(R.id.btnOpenMap);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        ic_baseline_location_city = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_baseline_location_city);

        permissionManager = PermissionManager.getInstance(this);
        locationManager = LocationManager.getInstance(this);

        btnOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!permissionManager.checkPermissions(permissions)) {
                    permissionManager.askPermissions(MapsActivity.this, permissions, 100);
                } else {
                    if (locationManager.isLocationEnabled()) {
                        mapFragment.getMapAsync(MapsActivity.this);
                    } else {
                        locationManager.createLocationRequest();
                    }
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng perth = new LatLng(-31.952854, 115.857342);

        mMap.addMarker(new MarkerOptions().
                position(sydney).
                title("Marker in Sydney")
                .snippet("Population: 4,627,300")
                .draggable(true)
                .infoWindowAnchor(0.5f, 0.5f));//.icon(BitmapDescriptorFactory.fromBitmap(ic_baseline_location_city))

        mMap.addMarker(new MarkerOptions().
                position(perth).
                title("Marker in Perth")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .draggable(true)
                .anchor(0.5f, 0.5f)
                .rotation(90.0f)
                .infoWindowAnchor(0.5f, 0.5f));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowCloseListener(this);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Toast.makeText(this, "Info window clicked.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowClose(@NonNull Marker marker) {
        Toast.makeText(this, "Info window closed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Toast.makeText(this, "Marker clicked.", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {
        //Toast.makeText(this, "onMarkerDrag", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        Toast.makeText(this, "Marker drag end.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {
        Toast.makeText(this, "Marker drag start.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && permissionManager.handlePermissionResult(MapsActivity.this, 100,
                permissions,
                grantResults)) {

            if (locationManager.isLocationEnabled()) {
                mapFragment.getMapAsync(MapsActivity.this);
            } else {
                locationManager.createLocationRequest();
            }
        }
    }
}