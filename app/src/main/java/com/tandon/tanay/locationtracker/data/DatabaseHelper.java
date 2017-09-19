package com.tandon.tanay.locationtracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tandon.tanay.locationtracker.constants.DbConfig;
import com.tandon.tanay.locationtracker.model.persistent.DaoMaster;

/**
 * Created by tanaytandon on 19/09/17.
 */

public class DatabaseHelper extends DaoMaster.OpenHelper {

    public DatabaseHelper(Context context, String name) {
        super(context, DbConfig.NAME);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }
}
