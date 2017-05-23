package com.buddybuild;


import com.buddybuild.core.App;
import com.buddybuild.core.Branch;

import java.util.List;

import io.reactivex.Single;

public interface Coordinator {

    /**
     * login a user with email and password
     *
     * @param email    user's email address
     * @param password user's password
     * @return a single of boolean which emits true if successful, false if failed
     */
    Single<Boolean> login(String email, String password);

    /**
     * Get all {@link App}s for a signed-in user
     *
     * @return Single of List<App>
     */
    Single<List<App>> getApps();

    /**
     * Get all {@link Branch}es for a given {@link App}
     *
     * @param appId ID of the {@link App}
     * @return single of List<Branch>
     */
    Single<List<Branch>> getBranches(String appId);
}
