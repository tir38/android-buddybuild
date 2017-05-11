package com.buddybuild;


import java.util.List;

import io.reactivex.Observable;

public interface Coordinator {

    /**
     * TODO
     * @param email
     * @param password
     * @return
     */
    Observable<Boolean> login(String email, String password);

    /**
     * Get all apps a signed-in user
     *
     * @return
     */
    Observable<List<App>> getApps();
}
