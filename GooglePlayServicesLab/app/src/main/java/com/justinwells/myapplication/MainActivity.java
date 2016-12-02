package com.justinwells.myapplication;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.justinwells.myapplication.R.id.map;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    LocationRequest mLocationRequest;
    public static final String KEY = "AIzaSyAzKiVxSpALnAUI_b4IEVYS9T6ivYlZpfU";
    GoogleApiClient mGoogleApiClient;
    Location mLocation;
    MapFragment mMapFragment;
    double mLatitude, mLongitude;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 138;
    public static final String[] REQUEST_CODE_LOCATION = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION};
    PendingResult<LocationSettingsResult> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);


    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(mLatitude, mLongitude))
                .title("Marker"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLatitude, mLongitude), 15));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLocation != null) {
                mLatitude = mLocation.getLatitude();
                mLongitude = mLocation.getLongitude();
                mMapFragment = (MapFragment) getFragmentManager()
                        .findFragmentById(map);
                mMapFragment.getMapAsync(this);
            }
        } else {
            verifyLocationPermissions(this);
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public static void verifyLocationPermissions(Activity activity) {

        int permission = ActivityCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    activity,
                    REQUEST_CODE_LOCATION,
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                if (mLocation != null) {
                    mLatitude = mLocation.getLatitude();
                    mLongitude = mLocation.getLongitude();
                    mMapFragment = (MapFragment) getFragmentManager()
                            .findFragmentById(map);
                    mMapFragment.getMapAsync(this);
                    }
                 break;
                }
            }
        }


}
