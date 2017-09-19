package com.tandon.tanay.locationtracker.ui.home;

import android.util.Log;

import com.tandon.tanay.locationtracker.LocationTracker;
import com.tandon.tanay.locationtracker.data.Repository;
import com.tandon.tanay.locationtracker.model.persistent.TrackingSessionEntity;
import com.tandon.tanay.locationtracker.model.persistent.UserLocationEntity;
import com.tandon.tanay.locationtracker.model.view.UserLocation;
import com.tandon.tanay.locationtracker.ui.base.BasePresenter;
import com.tandon.tanay.locationtracker.util.EntityMapper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter extends BasePresenter<HomeView> {

    private HomeView homeView;
    private LocationTracker locationTracker;

    private boolean trackingStatusKnown, isSessionActive;
    private Repository repository;
    private List<UserLocation> userLocations;

    public static final String TAG = HomePresenter.class.getSimpleName();

    @Override
    public void init(HomeView homeView) {
        this.homeView = homeView;
        this.locationTracker = (LocationTracker) homeView.getViewContext().getApplicationContext();
        this.repository = Repository.getInstance(locationTracker);
        this.userLocations = new ArrayList<>();
    }

    public void checkSessionStatus() {
        repository.getActiveTrackingSessions().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TrackingSessionEntity>>() {
                    @Override
                    public void accept(List<TrackingSessionEntity> trackingSessionEntities) throws Exception {
                        trackingStatusKnown = true;
                        if (trackingSessionEntities == null || trackingSessionEntities.size() == 0) {
                            noActiveSession();
                        } else {
                            locationTracker.setActiveSessionId(trackingSessionEntities.get(0).getId());
                            activateSession();
                            loadLocationsForCurrentSession();
                        }
                    }
                });
    }

    public void loadLocationsForCurrentSession() {
        repository.getAllLocationsForSession(locationTracker.getActiveSessionId())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<UserLocationEntity>>() {
                    @Override
                    public void accept(List<UserLocationEntity> userLocationEntities) throws Exception {
                        userLocations.addAll
                                (EntityMapper.INSTANCE.getUserLocationListFromEntityList(userLocationEntities));
                        homeView.userLocationsLoaded(userLocations);
                    }
                });
    }

    public void newLocationAdded(UserLocation userLocation) {
        UserLocation lastUserLocation = userLocations.size() > 0 ? userLocations.get(userLocations.size() - 1) : null;
        userLocations.add(userLocation);
        homeView.userLocationAdded(userLocation, lastUserLocation);
    }

    public void trackingButtonClicked() {
        if (isSessionActive) {
            stopActiveSession();
        } else {
            createActiveSession();
        }
    }

    public void noActiveSession() {
        homeView.noSessionIsActive();
        isSessionActive = false;
    }

    public void activateSession() {
        homeView.sessionIsActive();
        isSessionActive = true;
    }

    public void createActiveSession() {
        repository.createTrackingSession().subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        locationTracker.setActiveSessionId(aLong);
                        sessionCreated();
                    }
                });
    }

    public void sessionCreated() {
        isSessionActive = true;
        homeView.sessionIsActive();
        homeView.sessionStarted();
    }

    public void stopActiveSession() {
        if (locationTracker.getActiveSessionId() != null) {
            repository.stopTrackingSession().subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            locationTracker.setActiveSessionId(null);
                            sessionStopped();
                        }
                    });
        } else {
            sessionStopped();
        }
    }

    public void sessionStopped() {
        isSessionActive = false;
        homeView.sessionStopped();
        homeView.noSessionIsActive();
    }


}
