package com.tandon.tanay.locationtracker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.tandon.tanay.locationtracker.LocationTracker;
import com.tandon.tanay.locationtracker.Router;
import com.tandon.tanay.locationtracker.constants.Actions;
import com.tandon.tanay.locationtracker.data.Repository;
import com.tandon.tanay.locationtracker.model.persistent.TrackingSessionEntity;
import com.tandon.tanay.locationtracker.model.view.UserLocation;
import com.tandon.tanay.locationtracker.util.LocationUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class LocationReceiver extends BroadcastReceiver {
    public static final String TAG = LocationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive: arre bhai");

        if (intent != null) {

            switch (intent.getAction()) {

                case Actions.LOCATION_RECEIVER_ACTION: {

                    Log.d(TAG, "onReceive: hey there");

                    // get the first location
                    final Location location = LocationUtils.INSTANCE.getLocationFromIntent(intent);

                    if (location != null) {
                        // app instance
                        final LocationTracker locationTracker = (LocationTracker) context.getApplicationContext();

                        // save to database if no active session then get the active session and then save
                        if (locationTracker.getActiveSessionId() == null) {
                            Repository.getInstance(locationTracker)
                                    .getActiveTrackingSessions().subscribeOn(Schedulers.computation())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Consumer<List<TrackingSessionEntity>>() {
                                        @Override
                                        public void accept(List<TrackingSessionEntity> trackingSessionEntities) throws Exception {
                                            if (trackingSessionEntities != null && trackingSessionEntities.size() > 0) {
                                                locationTracker.setActiveSessionId(trackingSessionEntities.get(0).getId());
                                                saveLocation(locationTracker, location);
                                            }
                                        }
                                    });
                        } else {
                            saveLocation(locationTracker, location);
                        }

                    }

                    break;
                }

                default:
                    break;

            }

        }

    }


    private void saveLocation(final LocationTracker locationTracker, final Location location) {
        Repository.getInstance(locationTracker).saveLocation(location, locationTracker.getActiveSessionId())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "accept: location is " + location.getLongitude());
                        // broadcast the location
                        Router.INSTANCE
                                .sendLocationBroadCast(locationTracker.getBaseContext(),
                                        new UserLocation(location, locationTracker.getActiveSessionId()));
                    }
                });
    }
}
