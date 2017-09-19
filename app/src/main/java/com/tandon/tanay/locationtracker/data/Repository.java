package com.tandon.tanay.locationtracker.data;

import android.location.Location;
import android.util.Log;

import com.tandon.tanay.locationtracker.LocationTracker;
import com.tandon.tanay.locationtracker.model.persistent.TrackingSessionEntity;
import com.tandon.tanay.locationtracker.model.persistent.UserLocationEntity;

import java.util.List;

import io.reactivex.Observable;

import static android.content.ContentValues.TAG;

public class Repository {

    private LocationTracker locationTracker;
    private DatabaseManager databaseManager;

    private static Repository repository;

    public static Repository getInstance(LocationTracker locationTracker) {
        if (repository == null) {
            repository = new Repository(locationTracker);
        }
        return repository;
    }

    private Repository(LocationTracker locationTracker) {
        this.locationTracker = locationTracker;
        this.databaseManager = new DatabaseManager(locationTracker.getDaoSession());
    }

    public Observable<Long> saveLocation(Location location, Long sessionId) {
        UserLocationEntity userLocationEntity = new UserLocationEntity();
        userLocationEntity.setLatitude(location.getLatitude());
        userLocationEntity.setLongitude(location.getLongitude());
        userLocationEntity.setTimestamp(location.getTime());
        userLocationEntity.setTrackingSessionId(sessionId);
        return databaseManager.addUserLocation(userLocationEntity);
    }

    public Observable<Long> createTrackingSession() {
        TrackingSessionEntity trackingSessionEntity = new TrackingSessionEntity();
        trackingSessionEntity.setActive(true);
        return databaseManager.addTrackingSession(trackingSessionEntity);
    }

    public Observable<Long> stopTrackingSession() {
        TrackingSessionEntity trackingSessionEntity = new TrackingSessionEntity();
        trackingSessionEntity.setId(locationTracker.getActiveSessionId());
        trackingSessionEntity.setActive(false);
        return databaseManager.updateTrackingSession(trackingSessionEntity);
    }

    public Observable<List<TrackingSessionEntity>> getActiveTrackingSessions() {
        return databaseManager.getActiveTrackingSession();
    }


    public Observable<List<UserLocationEntity>> getAllLocationsForSession(Long sessionId) {
        Log.d(TAG, "getAllLocationsForSession: sessionId is " + sessionId);
        return databaseManager.getUserLocations(sessionId);
    }
}
