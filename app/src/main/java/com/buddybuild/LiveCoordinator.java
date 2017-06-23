package com.buddybuild;

import android.content.Context;
import android.preference.PreferenceManager;

import com.buddybuild.core.App;
import com.buddybuild.core.Branch;
import com.buddybuild.core.Build;
import com.buddybuild.core.LogItem;
import com.buddybuild.rest.LoginResult;
import com.buddybuild.rest.RestCoordinator;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class LiveCoordinator implements Coordinator {

    private static final String PREF_LOGIN_TOKEN = "PREF_LOGIN_TOKEN";
    private RestCoordinator restCoordinator;
    private Context context;
    private List<Branch> branches;

    public LiveCoordinator(RestCoordinator restCoordinator, Context context) {
        this.restCoordinator = restCoordinator;
        this.context = context;

        if (isLoggedIn()) {
            restCoordinator.setLoginToken(getLoginToken());
        }
    }

    @Override
    public Single<LoginResult> login(String email, String password) {
        return restCoordinator.login(email, password)
                .doOnSuccess(loginResult -> {
                    if (loginResult.getToken() != null) {
                        // save token to shared pref
                        PreferenceManager.getDefaultSharedPreferences(context)
                                .edit()
                                .putString(PREF_LOGIN_TOKEN, loginResult.getToken())
                                .apply();
                    }
                });
    }

    @Override
    public boolean isLoggedIn() {
        return getLoginToken() != null;
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
                .toList()
                .doOnSuccess(branches -> LiveCoordinator.this.branches = branches); // cache
    }

    @Override
    public Maybe<Build> getBuild(String buildId) {
        for (Branch branch : branches) {
            for (Build build : branch.getBuilds()) {
                if (build.getId().equals(buildId)) {
                    return Maybe.just(build);
                }
            }
        }

        return Maybe.empty();
    }

    @Override
    public Single<List<LogItem>> getLogs(String buildId) {
        return restCoordinator.getLog(buildId);
    }

    private String getLoginToken() {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_LOGIN_TOKEN, null);
    }
}
