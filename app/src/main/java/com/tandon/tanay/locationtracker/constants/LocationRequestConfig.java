package com.tandon.tanay.locationtracker.constants;

import com.google.android.gms.location.LocationRequest;

/**
 * Created by tanaytandon on 18/09/17.
 */

public interface LocationRequestConfig {

    int UPDATE_INTERVAL = 1 * 6000;
    int FASTEST_INTERVAL = 1 * 6000;
    int MAX_INTERVAL = 1 * 6000;
    int PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;
}
