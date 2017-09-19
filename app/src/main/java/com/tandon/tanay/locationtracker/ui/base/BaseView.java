package com.tandon.tanay.locationtracker.ui.base;

import android.content.Context;
import android.view.View;


public interface BaseView {

    Context getViewContext();

    void showErrorMessage(View view, int messageResId);

    void showErrorMessage(View view, int messageResId, int actionResId, View.OnClickListener clickListener);

    void dismissSnackbar();
}
