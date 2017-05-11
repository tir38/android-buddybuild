package com.buddybuild;

import com.buddybuild.rest.RestCoordinator;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class LiveCoordinator implements Coordinator {

    private RestCoordinator restCoordinator;

    public LiveCoordinator(RestCoordinator restCoordinator) {
        this.restCoordinator = restCoordinator;
    }

    @Override
    public Single<Boolean> login(String email, String password) {
        return restCoordinator.login(email, password);
    }

    @Override
    public Single<List<App>> getApps() {
        return restCoordinator.getApps();
    }
}
