package com.tandon.tanay.locationtracker.ui.base;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.View;

import com.tandon.tanay.locationtracker.LocationTracker;

public abstract class BaseFragment extends Fragment implements BaseView {

    private BaseActivity baseActivity;
    private LocationTracker locationTracker;
    private Snackbar snackbar;


    @Override
    public void showErrorMessage(View view, int messageResId) {
        showErrorMessage(view, messageResId, -1, null);
    }

    @Override
    public void showErrorMessage(View view, int messageResId, int actionResId, View.OnClickListener clickListener) {
        dismissSnackbar();
        if (view != null) {
            snackbar = Snackbar.make(view, messageResId, Snackbar.LENGTH_INDEFINITE);
            if (actionResId != -1 && clickListener != null) {
                snackbar.setAction(actionResId, clickListener);
            }
            snackbar.show();
        }
    }

    @Override
    public void dismissSnackbar() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    public BaseActivity getBaseActivity() {
        if (baseActivity == null) {
            baseActivity = (BaseActivity) getActivity();
        }
        return baseActivity;
    }

    public LocationTracker getApp() {
        if (locationTracker == null) {
            locationTracker = (LocationTracker) getActivity().getApplicationContext();
        }
        return locationTracker;
    }

}
