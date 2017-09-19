package com.tandon.tanay.locationtracker.util;

import com.tandon.tanay.locationtracker.model.persistent.UserLocationEntity;
import com.tandon.tanay.locationtracker.model.view.UserLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanaytandon on 20/09/17.
 */

public enum EntityMapper {

    INSTANCE;

    public List<UserLocation> getUserLocationListFromEntityList(List<UserLocationEntity> userLocationEntities) {
        List<UserLocation> userLocations = new ArrayList<>();
        if (userLocationEntities != null) {
            for (UserLocationEntity userLocationEntity : userLocationEntities) {
                userLocations.add(new UserLocation(userLocationEntity));
            }
        }
        return userLocations;
    }
}
