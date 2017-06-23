package com.buddybuild;


import com.buddybuild.core.App;
import com.buddybuild.core.Branch;
import com.buddybuild.core.Build;
import com.buddybuild.core.LogItem;
import com.buddybuild.rest.LoginResult;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

public interface Coordinator {

    /**
     * login a user with email and password
     *
     * @param email    user's email address
     * @param password user's password
     * @return a single which emits a {@link LoginResult}
     */
    Single<LoginResult> login(String email, String password);

    /**
     * logout current user
     *
     * @return a single which emits a Boolean: true for success, false for failure
     */
    Single<Boolean> logout();

    /**
     * @return true if a user is currently logged in, false if not
     */
    boolean isLoggedIn();


    /**
     * @return If a user is logged in, returns their email
     */
    String getLoggedInUserEmail();

    /**
     * Get all {@link App}s for a signed-in user
     *
     * @return a single of List<App>
     */
    Single<List<App>> getApps();

    /**
     * Get all {@link Branch}es for a given {@link App}
     *
     * @param appId ID of the {@link App}
     * @return a single of List<Branch>
     */
    Single<List<Branch>> getBranches(String appId);

    /**
     * @param buildId ID of the {@link Build}
     * @return a {@link Maybe} of {@link Build}
     */
    Maybe<Build> getBuild(String buildId);

    /**
     * Get logs for a single build
     *
     * @param buildId ID of the build
     * @return single of list of log items
     */
    Single<List<LogItem>> getLogs(String buildId);


}
