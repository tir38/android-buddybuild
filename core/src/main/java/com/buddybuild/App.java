package com.buddybuild;

import lombok.Getter;

/**
 * A model representation of an app
 */
public class App {

    @Getter
    private String id;
    @Getter
    private String name;
    @Getter
    private Platform platform;

    public App(String id, String name, Platform platform) {
        this.id = id;
        this.name = name;
        this.platform = platform;
    }

    public enum Platform {
        ANDROID,
        IOS
    }
}
