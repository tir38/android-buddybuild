package com.buddybuild.core;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * A model of a branch on a repository
 */
public class Branch {

    @Getter
    private final String name;

    @Getter
    @Setter
    private List<Build> builds;

    public Branch(String name) {
        this.name = name;
    }
}
