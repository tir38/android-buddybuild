package com.buddybuild;

import com.buddybuild.rest.AppResponse;
import com.buddybuild.rest.WebService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class LiveCoordinator implements Coordinator {

    private WebService webService;

    public LiveCoordinator(WebService webService) {
        this.webService = webService;
    }

    @Override
    public Observable<List<App>> getApps() {
        return webService.getApps().map(response -> {
            List<App> apps = new ArrayList<>();
            if (response.isSuccessful()) {
                List<AppResponse> appResponses = response.body();
                for (AppResponse appResponse : appResponses) {
                    App app = appResponse.toApp();
                    if (app != null) {
                        apps.add(appResponse.toApp());
                    }
                }
                return apps;
            } else {
                return apps; // TODO handle nonsuccess
            }
        });
    }
}
