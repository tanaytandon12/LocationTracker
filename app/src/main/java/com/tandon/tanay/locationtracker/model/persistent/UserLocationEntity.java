package com.tandon.tanay.locationtracker.model.persistent;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;


@Entity
public class UserLocationEntity {

    @Id
    private Long id;

    private Double latitude;

    private Double longitude;

    private Long timestamp;


    private Long trackingSessionId;

    @ToOne(joinProperty = "trackingSessionId")
    private TrackingSessionEntity trackingSessionEntity;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1006461803)
    private transient UserLocationEntityDao myDao;


    @Generated(hash = 291981308)
    public UserLocationEntity(Long id, Double latitude, Double longitude,
            Long timestamp, Long trackingSessionId) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.trackingSessionId = trackingSessionId;
    }

    @Generated(hash = 207885588)
    public UserLocationEntity() {
    }

    @Generated(hash = 301463004)
    private transient Long trackingSessionEntity__resolvedKey;


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTrackingSessionId() {
        return this.trackingSessionId;
    }

    public void setTrackingSessionId(Long trackingSessionId) {
        this.trackingSessionId = trackingSessionId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 59922034)
    public TrackingSessionEntity getTrackingSessionEntity() {
        Long __key = this.trackingSessionId;
        if (trackingSessionEntity__resolvedKey == null
                || !trackingSessionEntity__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TrackingSessionEntityDao targetDao = daoSession
                    .getTrackingSessionEntityDao();
            TrackingSessionEntity trackingSessionEntityNew = targetDao.load(__key);
            synchronized (this) {
                trackingSessionEntity = trackingSessionEntityNew;
                trackingSessionEntity__resolvedKey = __key;
            }
        }
        return trackingSessionEntity;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1958651934)
    public void setTrackingSessionEntity(
            TrackingSessionEntity trackingSessionEntity) {
        synchronized (this) {
            this.trackingSessionEntity = trackingSessionEntity;
            trackingSessionId = trackingSessionEntity == null ? null
                    : trackingSessionEntity.getId();
            trackingSessionEntity__resolvedKey = trackingSessionId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 438507979)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserLocationEntityDao() : null;
    }


}
