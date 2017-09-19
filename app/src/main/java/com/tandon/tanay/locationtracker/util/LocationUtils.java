package com.tandon.tanay.locationtracker.util;

import android.content.Intent;
import android.location.Location;

import com.google.android.gms.location.LocationResult;

import java.util.List;

/**
 * Created by tanaytandon on 20/09/17.
 */

public enum LocationUtils {

    INSTANCE;

    public Location getLocationFromIntent(Intent intent) {
        LocationResult result = LocationResult.extractResult(intent);
        if (result != null && result.getLocations().size() > 0) {
            // get the first location
            return result.getLocations().get(0);
        }
        return null;
    }
}
