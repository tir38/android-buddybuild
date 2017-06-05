package com.buddybuild.core;

import org.junit.Test;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
public class BranchTest {

    @Test
    public void shouldHaveName() throws Exception {
        // arrange
        Branch branch = new Branch("name");

        // assert
        assertThat(branch.getName()).isEqualTo("name");
    }

    @Test
    public void shouldReturnMostRecentEvent() throws Exception {
        // arrange
        Branch branch = new Branch("name");

        ZonedDateTime createTime = ZonedDateTime.now().minus(3, ChronoUnit.HOURS);
        ZonedDateTime startTime = ZonedDateTime.now().minus(2, ChronoUnit.HOURS);
        ZonedDateTime build1FinishTime = ZonedDateTime.now().minus(1, ChronoUnit.HOURS);

        Build build1 = new Build.Builder()
                .id("1")
                .createTime(createTime)
                .startTime(startTime)
                .finishTime(build1FinishTime)
                .build();

        ZonedDateTime build2FinishTime = build1FinishTime.plus(10, ChronoUnit.MINUTES);
        Build build2 = new Build.Builder()
                .id("2")
                .createTime(createTime)
                .startTime(startTime)
                .finishTime(build2FinishTime)
                .build();

        branch.setBuilds(Arrays.asList(build1, build2));

        // act
        ZonedDateTime mostRecentBuildEvent = branch.getMostRecentBuildEvent();

        // assert
        assertThat(mostRecentBuildEvent).isEqualTo(build2FinishTime);
    }

    @Test
    public void shouldSortBranchesWithMasterFirst() throws Exception {
        // arrange
        Branch master = new Branch("master");
        Branch featureBranch = new Branch("some feature");

        List<Branch> branches = Arrays.asList(featureBranch, master);

        // act
        Collections.sort(branches, new Branch.SortByMostRecentEvent());

        // assert
        assertThat(branches.get(0)).isEqualTo(master);
    }

    @Test
    public void shouldSortBranchesIfBothNullMostRecentEvent() throws Exception {
        // arrange
        Branch bugBranch = new Branch("a bug fix");
        Branch featureBranch = new Branch("some feature");

        List<Branch> branches = Arrays.asList(featureBranch, bugBranch);

        // act
        Collections.sort(branches, new Branch.SortByMostRecentEvent());

        // assert
        assertThat(branches.get(0)).isEqualTo(featureBranch);
    }

    @Test
    public void shouldSortBranchesIfOneHasNullMostRecentEvent() throws Exception {
        // arrange
        Branch bugBranch = new Branch("a bug fix");

        List<Build> builds = Arrays.asList(new Build.Builder().finishTime(ZonedDateTime.now()).build());
        Branch featureBranch = new Branch("some feature");
        featureBranch.setBuilds(builds);

        List<Branch> branches = Arrays.asList(bugBranch, featureBranch);

        // act
        Collections.sort(branches, new Branch.SortByMostRecentEvent());

        // assert
        assertThat(branches.get(0)).isEqualTo(featureBranch);
    }

    @Test
    public void shouldSortBranchesBasedOnMostRecentEvent() throws Exception {
        // arrange
        List<Build> bugBuilds = Arrays.asList(new Build.Builder()
                .finishTime(ZonedDateTime.now().minus(1, ChronoUnit.HOURS)).build());
        Branch bugBranch = new Branch("a bug fix");
        bugBranch.setBuilds(bugBuilds);

        List<Build> featureBuilds = Arrays.asList(new Build.Builder().finishTime(ZonedDateTime.now()).build());
        Branch featureBranch = new Branch("some feature");
        featureBranch.setBuilds(featureBuilds);

        List<Branch> branches = Arrays.asList(bugBranch, featureBranch);

        // act
        Collections.sort(branches, new Branch.SortByMostRecentEvent());

        // assert
        assertThat(branches.get(0)).isEqualTo(featureBranch);
    }

}
