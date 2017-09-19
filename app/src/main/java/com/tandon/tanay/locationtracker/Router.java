package com.tandon.tanay.locationtracker;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;

import com.tandon.tanay.locationtracker.constants.IntentFilters;
import com.tandon.tanay.locationtracker.constants.Keys;
import com.tandon.tanay.locationtracker.model.view.UserLocation;
import com.tandon.tanay.locationtracker.ui.base.BaseActivity;

/**
 * Created by tanaytandon on 18/09/17.
 */

public enum Router {
    INSTANCE;

    public void startSettingsActivity(BaseActivity baseActivity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        baseActivity.startActivity(intent);
    }

    public void sendLocationBroadCast(Context context, UserLocation location) {
        Intent intent = new Intent(IntentFilters.LOCATION_FOUND);
        intent.putExtra(Keys.LOCATION, location);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
