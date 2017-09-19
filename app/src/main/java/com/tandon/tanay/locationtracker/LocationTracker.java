package com.tandon.tanay.locationtracker;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.tandon.tanay.locationtracker.constants.DbConfig;
import com.tandon.tanay.locationtracker.data.DatabaseHelper;
import com.tandon.tanay.locationtracker.model.persistent.DaoMaster;
import com.tandon.tanay.locationtracker.model.persistent.DaoSession;


public class LocationTracker extends Application {


    private DaoSession daoSession;

    private Long activeSessionId;

    @Override
    public void onCreate() {
        super.onCreate();

        DatabaseHelper helper = new DatabaseHelper(this, DbConfig.NAME);
        SQLiteDatabase database = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }


    public DaoSession getDaoSession() {
        return daoSession;
    }

    public Long getActiveSessionId() {
        return activeSessionId;
    }

    public void setActiveSessionId(Long sessionId) {
        this.activeSessionId = sessionId;
    }
}
