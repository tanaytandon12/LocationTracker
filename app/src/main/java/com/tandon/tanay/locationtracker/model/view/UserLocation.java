package com.tandon.tanay.locationtracker.model.view;


import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.tandon.tanay.locationtracker.model.persistent.UserLocationEntity;

public class UserLocation implements Parcelable {

    private Long sessionId;

    private LatLng latLng;

    private Long timestamp;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public UserLocation(UserLocationEntity userLocationEntity) {
        this.latLng = new LatLng(userLocationEntity.getLatitude(), userLocationEntity.getLongitude());
        this.sessionId = userLocationEntity.getTrackingSessionId();
        this.timestamp = userLocationEntity.getTimestamp();
    }

    public UserLocation(Location location, Long sessionId) {
        this.latLng = new LatLng(location.getLatitude(), location.getLongitude());
        this.sessionId = sessionId;
        this.timestamp = location.getTime();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.sessionId);
        dest.writeParcelable(this.latLng, flags);
        dest.writeValue(this.timestamp);
    }

    protected UserLocation(Parcel in) {
        this.sessionId = (Long) in.readValue(Long.class.getClassLoader());
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
        this.timestamp = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Parcelable.Creator<UserLocation> CREATOR = new Parcelable.Creator<UserLocation>() {
        @Override
        public UserLocation createFromParcel(Parcel source) {
            return new UserLocation(source);
        }

        @Override
        public UserLocation[] newArray(int size) {
            return new UserLocation[size];
        }
    };
}
