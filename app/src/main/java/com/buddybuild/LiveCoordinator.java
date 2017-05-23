package com.buddybuild;

import com.buddybuild.core.App;
import com.buddybuild.core.Branch;
import com.buddybuild.rest.RestCoordinator;

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
    public Single<List<Branch>> getBranches(final String appId) {
        return restCoordinator.getBranches(appId)
                .flattenAsObservable(branches -> branches) // emit each branch in list one at a time
                .flatMap(branch -> restCoordinator.getBuildsForBranch(appId, branch.getName()).toObservable(),
                        (branch, builds) -> {
                            branch.setBuilds(builds); // add builds to branch
                            return branch;
                        })
                .toList();
    }
}
