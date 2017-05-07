package com.buddybuild;


import java.util.List;

import io.reactivex.Observable;

public interface Coordinator {

    /**
     * Get all apps a signed-in user
     *
     * @return
     */
    Observable<List<App>> getApps();
}
