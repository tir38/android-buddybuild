package com.buddybuild;

import com.buddybuild.core.App;
import com.buddybuild.core.Branch;
import com.buddybuild.core.Build;
import com.buddybuild.rest.RestCoordinator;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public Single<List<Branch>> getBranches(String appId) {
        return Single.zip(restCoordinator.getBranches(appId),
                restCoordinator.getBuilds(appId),
                (branches, allBuilds) -> {
                    for (Branch branch : branches) {
                        List<Build> builds = new ArrayList<>();
                        for (Build build : allBuilds) {
                            if (build.getBranch().equals(branch.getName())) {
                                builds.add(build);
                            }
                        }
                        branch.setBuilds(builds);
                    }
                    return branches;
                }
        );
    }
}
