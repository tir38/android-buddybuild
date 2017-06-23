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
    private static final String PREF_EMAIL = "PREF_EMAIL";
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
                        saveLoginToken(loginResult.getToken());
                        saveEmail(email);
                    }
                });
    }

    @Override
    public Single<Boolean> logout() {
        restCoordinator.setLoginToken(null);
        deleteLoginToken();
        deleteEmail();
        branches = null;
        return Single.just(true);
    }

    @Override
    public boolean isLoggedIn() {
        return getLoginToken() != null;
    }

    @Override
    public String getLoggedInUserEmail() {
        if (isLoggedIn()) {
            return getEmail();
        }

        return null;
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

    private void saveLoginToken(String token) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LOGIN_TOKEN, token)
                .apply();
    }

    private void deleteLoginToken() {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(PREF_LOGIN_TOKEN)
                .apply();
    }

    private String getEmail() {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_EMAIL, null);
    }

    private void saveEmail(String token) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_EMAIL, token)
                .apply();
    }

    private void deleteEmail() {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .remove(PREF_EMAIL)
                .apply();
    }
}
