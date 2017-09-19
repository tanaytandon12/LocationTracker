package com.tandon.tanay.locationtracker.data;

import com.tandon.tanay.locationtracker.model.persistent.DaoSession;
import com.tandon.tanay.locationtracker.model.persistent.TrackingSessionEntity;
import com.tandon.tanay.locationtracker.model.persistent.TrackingSessionEntityDao;
import com.tandon.tanay.locationtracker.model.persistent.UserLocationEntity;
import com.tandon.tanay.locationtracker.model.persistent.UserLocationEntityDao;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;


public class DatabaseManager {

    private DaoSession daoSession;

    public DatabaseManager(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    public Observable<List<TrackingSessionEntity>> getActiveTrackingSession() {
        return Observable.fromCallable(new Callable<List<TrackingSessionEntity>>() {
            @Override
            public List<TrackingSessionEntity> call() throws Exception {
                return daoSession.getTrackingSessionEntityDao().queryBuilder()
                        .where(TrackingSessionEntityDao.Properties.Active.eq(true)).list();
            }
        });
    }

    public Observable<List<UserLocationEntity>> getUserLocations(final Long sessionId) {
        return Observable.fromCallable(new Callable<List<UserLocationEntity>>() {
            @Override
            public List<UserLocationEntity> call() throws Exception {
                return daoSession.getUserLocationEntityDao().queryBuilder()
                        .where(UserLocationEntityDao.Properties.TrackingSessionId.eq(sessionId)).list();
            }
        });
    }

    public Observable<Long> addUserLocation(final UserLocationEntity userLocationEntity) {
        return Observable.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return daoSession.getUserLocationEntityDao().insert(userLocationEntity);
            }
        });
    }

    public Observable<Long> addTrackingSession(final TrackingSessionEntity trackingSessionEntity) {
        return Observable.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return daoSession.getTrackingSessionEntityDao().insert(trackingSessionEntity);
            }
        });
    }

    public Observable<Long> updateTrackingSession(final TrackingSessionEntity trackingSessionEntity) {
        return Observable.fromCallable(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                daoSession.getTrackingSessionEntityDao().update(trackingSessionEntity);
                return trackingSessionEntity.getId();
            }
        });
    }


}
