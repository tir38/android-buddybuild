package com.buddybuild.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.buddybuild.BR;
import com.buddybuild.core.Build;

public class BuildViewModel extends BaseObservable {

    private BuildStatusViewModel buildStatusViewModel;

    public BuildViewModel() {
        buildStatusViewModel = new BuildStatusViewModel();
    }

    public void setBuild(Build build) {
        buildStatusViewModel.setBuild(build);
        notifyPropertyChanged(BR.buildStatusViewModel);
    }

    @Bindable
    public BuildStatusViewModel getBuildStatusViewModel() {
        return buildStatusViewModel;
    }
}
