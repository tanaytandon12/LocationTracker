package com.tandon.tanay.locationtracker.ui.home;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.tandon.tanay.locationtracker.model.view.UserLocation;
import com.tandon.tanay.locationtracker.ui.base.BaseActivity;
import com.tandon.tanay.locationtracker.ui.base.BaseView;

import java.util.List;


interface HomeView extends BaseView {

    void sessionIsActive();

    void noSessionIsActive();

    void sessionStarted();

    void sessionStopped();

    void userLocationsLoaded(List<UserLocation> userLocations);

    void userLocationAdded(UserLocation userLocation, UserLocation lastUserLocation);
}
