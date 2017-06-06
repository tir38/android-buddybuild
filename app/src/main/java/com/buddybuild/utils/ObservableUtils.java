package com.buddybuild.utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;


public final class ObservableUtils {

    private static final int MINIMUM_LOADING_TIME = 800;

    /**
     * Zip the input {@link Observable} with a timer.
     * This will return an observable that only emits items from the input observable
     * once BOTH an items is emitted from the observable AND the timer expires.
     */
    public static <T> Observable<T> zipWithTimer(Observable<T> observable) {
        return Observable.zip(observable,
                Observable.timer(MINIMUM_LOADING_TIME, TimeUnit.MILLISECONDS), (t, timerValue) -> t);
    }

    /**
     * Zip the input {@link Single} with a timer.
     * This will return  a Single that only emits from the input Single
     * once BOTH an items is emitted from the Single AND the timer expires.
     */
    public static <T> Single<T> zipWithTimer(Single<T> single) {
        return Single.zip(single,
                Single.timer(MINIMUM_LOADING_TIME, TimeUnit.MILLISECONDS), (t, timerValue) -> t);
    }
}
