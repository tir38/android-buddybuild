package com.buddybuild.core;

import org.threeten.bp.ZonedDateTime;

import java.util.Comparator;
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

    /**
     * TODO
     *
     * @return
     */
    public ZonedDateTime getMostRecentBuildEvent() {
        if (builds == null || builds.isEmpty()) {
            return null;
        }

        ZonedDateTime mostRecent = null;
        for (Build build : builds) {
            if (mostRecent == null) {
                mostRecent = build.getMostRecentBuildEvent();
                continue;
            }

            if (mostRecent.isBefore(build.getMostRecentBuildEvent())) {
                mostRecent = build.getMostRecentBuildEvent();
            }
        }

        return mostRecent;
    }

    /**
     * A Comparator that sorts Branches, with "master" first, then remaining by most recent event.
     * If most recent event is null then puts last.
     */
    public static class SortByMostRecentEvent implements Comparator<Branch> {

        @Override
        public int compare(Branch branch1, Branch branch2) {
            if (branch1.name.equals("master")) {
                return -1;
            }

            if (branch2.name.equals("master")) {
                return 1;
            }

            ZonedDateTime branch1MostRecentEvent = branch1.getMostRecentBuildEvent();
            ZonedDateTime branch2MostRecentEvent = branch2.getMostRecentBuildEvent();

            // if any are null
            if (branch1MostRecentEvent == null && branch2MostRecentEvent == null) {
                return 1;
            } else if (branch1MostRecentEvent == null) {
                return 1;
            } else if (branch2MostRecentEvent == null)
                return -1;

            if (branch1MostRecentEvent.isAfter(branch2MostRecentEvent)) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
