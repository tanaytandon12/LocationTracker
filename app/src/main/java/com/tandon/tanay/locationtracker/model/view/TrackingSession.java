package com.tandon.tanay.locationtracker.model.view;


import java.util.List;

public class TrackingSession {

    private Long id;

    private List<UserLocation> userLocationList;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<UserLocation> getUserLocationList() {
        return userLocationList;
    }

    public void setUserLocationList(List<UserLocation> userLocationList) {
        this.userLocationList = userLocationList;
    }
}
