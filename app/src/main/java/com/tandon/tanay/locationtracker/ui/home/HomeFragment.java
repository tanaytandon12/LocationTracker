package com.tandon.tanay.locationtracker.ui.home;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tandon.tanay.locationtracker.R;
import com.tandon.tanay.locationtracker.Router;
import com.tandon.tanay.locationtracker.constants.Actions;
import com.tandon.tanay.locationtracker.constants.IntentFilters;
import com.tandon.tanay.locationtracker.constants.Keys;
import com.tandon.tanay.locationtracker.constants.LocationRequestConfig;
import com.tandon.tanay.locationtracker.constants.RequestCodes;
import com.tandon.tanay.locationtracker.model.view.UserLocation;
import com.tandon.tanay.locationtracker.receiver.LocationReceiver;
import com.tandon.tanay.locationtracker.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends BaseFragment implements HomeView, View.OnClickListener,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap googleMap;

    @BindView(R.id.btnTracking)
    protected Button btnTracking;

    @BindView(R.id.rootView)
    protected View rootView;

    private boolean isGoogleMapReady, isGoogleApiClientConnected, shouldRequestForLocationUpdates;

    private HomePresenter homePresenter;

    private GoogleApiClient googleApiClient;

    private LocationRequest locationRequest;

    private List<Marker> markers;
    private List<Polyline> polylines;

    private BroadcastReceiver locationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(Keys.LOCATION)) {
                UserLocation userLocation = intent.getParcelableExtra(Keys.LOCATION);
                homePresenter.newLocationAdded(userLocation);
            }
        }
    };

    private static final int[] COLORS = {Color.BLUE, Color.BLACK, Color.GREEN, Color.RED};

    public static final String TAG = HomeFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        markers = new ArrayList<>();
        polylines = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        homePresenter = new HomePresenter();
        homePresenter.init(this);

        init();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(locationBroadcastReceiver,
                new IntentFilter(IntentFilters.LOCATION_FOUND));
        checkLocationPermission();
        homePresenter.checkSessionStatus();
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(locationBroadcastReceiver);
        disConnectGoogleApiClient();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestCodes.REQUEST_LOCATION_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    connectGoogleApiClient();
                } else {
                    showMessageToOpenSettings();
                }
                break;
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.isGoogleMapReady = true;
        zoomGoogleMap();
    }


    private void zoomGoogleMap() {
        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(20f));
        }
    }

    private void drawMarkerOnMap(UserLocation userLocation) {
        if (googleMap != null) {
            markers.add(googleMap.addMarker(new MarkerOptions().position(userLocation.getLatLng())));
        }
    }

    private void drawPolyline(UserLocation userLocation1, UserLocation userLocation2) {
        if (googleMap != null) {
            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(userLocation1.getLatLng(), userLocation2.getLatLng())
                    .width(10f)
                    .color(COLORS[(int) (Math.random() * COLORS.length)]);
            polylines.add(googleMap.addPolyline(polylineOptions));
        }
    }

    private void moveCamera(UserLocation userLocation) {
        if (googleMap != null) {
            googleMap.moveCamera(CameraUpdateFactory
                    .newLatLng(userLocation.getLatLng()));
        }
    }

    public void setUpMap() {

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            getFragmentManager().beginTransaction().add(R.id.rootView, supportMapFragment).commit();
        }

        supportMapFragment.getMapAsync(this);
    }

    public void setUpTrackingButton() {
        btnTracking.setOnClickListener(this);
    }

    public PendingIntent getPendingIntent() {
        Intent intent = new Intent(getContext(), LocationReceiver.class);
        intent.setAction(Actions.LOCATION_RECEIVER_ACTION);
        return PendingIntent.getBroadcast(getContext(), 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void setUpGoogleApiClient() {
        if (googleApiClient != null)
            return;

        googleApiClient = new GoogleApiClient.Builder(getContext()).addApi(LocationServices.API)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
    }


    public void requestForLocationPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                RequestCodes.REQUEST_LOCATION_PERMISSION);
    }

    public void setUpLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(LocationRequestConfig.UPDATE_INTERVAL);
        locationRequest.setFastestInterval(LocationRequestConfig.FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequestConfig.PRIORITY);
        locationRequest.setMaxWaitTime(LocationRequestConfig.MAX_INTERVAL);
    }

    public boolean requireLocationPermission() {
        return !(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }


    private void checkLocationPermission() {
        if (requireLocationPermission()) {

            boolean shouldProvideRationale =
                    shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION);
            if (shouldProvideRationale) {
                // the user has earlier refused permission to give location
                // so we must ask him to give permission by opening settings
                showMessageRequestingLocationPermission();
            } else {
                // we must ask for location permission
                requestForLocationPermission();
            }
        } else {
            connectGoogleApiClient();
        }
    }


    public void init() {

        setUpMap();
        setUpTrackingButton();
        setUpGoogleApiClient();
        setUpLocationRequest();

    }

    public void connectGoogleApiClient() {
        if (googleApiClient != null && !googleApiClient.isConnected() && !googleApiClient.isConnecting()) {
            googleApiClient.connect();
        }
    }

    public void disConnectGoogleApiClient() {
        if (googleApiClient != null && (googleApiClient.isConnected() || googleApiClient.isConnecting())) {
            googleApiClient.disconnect();
        }
    }

    public void requestForLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.
                    requestLocationUpdates(googleApiClient, locationRequest, getPendingIntent());
            shouldRequestForLocationUpdates = false;
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    public void stopRequestingForLocationUpdates() {
        if (googleApiClient != null && (googleApiClient.isConnected())) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, getPendingIntent());
        }
    }

    public void showMessageRequestingLocationPermission() {
        showErrorMessage(rootView, R.string.locationPermissionRequired, R.string.ok,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestForLocationPermission();
                    }
                });
    }

    public void showMessageToOpenSettings() {
        showErrorMessage(rootView, R.string.locationPermissionDenied, R.string.openSettings,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Router.INSTANCE.startSettingsActivity(getBaseActivity());
                    }
                });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btnTracking: {
                homePresenter.trackingButtonClicked();
                break;
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        isGoogleApiClientConnected = true;
        if (shouldRequestForLocationUpdates) {
            requestForLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        isGoogleApiClientConnected = false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: connection to google api client failed ");
        showErrorMessage(rootView, R.string.errorConnectingPlayServices);
    }

    @Override
    public void sessionIsActive() {
        btnTracking.setEnabled(true);
        btnTracking.setText(R.string.stopTracking);
    }

    @Override
    public void noSessionIsActive() {
        btnTracking.setEnabled(true);
        btnTracking.setText(R.string.startTracking);
    }

    @Override
    public void sessionStarted() {
        if (googleMap != null) {
            googleMap.clear();
            for (Marker marker : markers) {
                marker.remove();
            }
            for (Polyline polyline : polylines) {
                polyline.remove();
            }
        }
        if (googleApiClient != null && googleApiClient.isConnected()) {
            requestForLocationUpdates();
        } else {
            shouldRequestForLocationUpdates = true;
            connectGoogleApiClient();
        }
    }

    @Override
    public void sessionStopped() {
        shouldRequestForLocationUpdates = false;
        stopRequestingForLocationUpdates();
    }

    @Override
    public void userLocationsLoaded(List<UserLocation> userLocations) {
        for (UserLocation userLocation : userLocations) {
            drawMarkerOnMap(userLocation);
        }
        if (userLocations.size() > 0) {
            moveCamera(userLocations.get(userLocations.size() - 1));
        }
        for (int index = 0; index < userLocations.size() - 1; index++) {
            drawPolyline(userLocations.get(index), userLocations.get(index + 1));
        }
        zoomGoogleMap();
    }

    @Override
    public void userLocationAdded(UserLocation userLocation, UserLocation lastUserLocation) {
        drawMarkerOnMap(userLocation);
        moveCamera(userLocation);
        if (lastUserLocation != null) {
            drawPolyline(lastUserLocation, userLocation);
        }
    }
}
