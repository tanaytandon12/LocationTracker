package com.tandon.tanay.locationtracker.model.persistent;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;


@Entity
public class TrackingSessionEntity {

    @Id
    private Long id;


    private Boolean active;



    @Generated(hash = 1937971771)
    public TrackingSessionEntity(Long id, Boolean active) {
        this.id = id;
        this.active = active;
    }

    @Generated(hash = 928465350)
    public TrackingSessionEntity() {
    }



    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
