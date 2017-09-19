package com.tandon.tanay.locationtracker.ui.base;

/**
 * Created by tanaytandon on 13/09/17.
 */

public abstract class BasePresenter<V extends BaseView> {

    public abstract void init(V baseView);
}
