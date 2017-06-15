package com.buddybuild.rest;

import com.buddybuild.core.App;
import com.buddybuild.core.Branch;
import com.buddybuild.core.Build;
import com.buddybuild.core.LogItem;

import java.util.List;

import io.reactivex.Single;

/**
 * Coordinates all requests to the BuddyBuild REST API
 */
public interface RestCoordinator {
    /**
     * @return a {@link Single} which emits a list of {@link App}s
     */
    Single<List<App>> getApps();

    /**
     * Get all open {@link Branch}es for a given {@link App}
     *
     * @param appId ID of the {@link App}
     * @return Single of List<Branch>
     */
    Single<List<Branch>> getBranches(String appId);


    /**
     * Get up to five most recent {@link Build}s for a given {@link Branch} name
     *
     * @param appId      ID of the {@link App}
     * @param branchName name of  the {@link Branch}
     * @return Single of List<Build>
     */
    Single<List<Build>> getBuildsForBranch(String appId, String branchName);

    /**
     * Login to BuddyBuild API
     *
     * @param email    user's email
     * @param password user's password
     * @return a {@link Single} that emits a {@link LoginResult}
     */
    Single<LoginResult> login(String email, String password);

    /**
     * Get Logs for a single {@link Build}
     *
     * @param buildId ID of the build
     * @return a {@link Single} that emits list of logs
     */
    Single<List<LogItem>> getLog(String buildId);

}
