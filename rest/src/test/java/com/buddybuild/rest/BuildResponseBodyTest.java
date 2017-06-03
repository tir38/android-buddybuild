package com.buddybuild.rest;

import com.buddybuild.core.Build;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
public class BuildResponseBodyTest {

    private List<BuildResponseBody> buildResponseBody;

    @Before
    public void setUp() throws Exception {
        String json = JsonStringReader.readJsonSampleFromFile("build/valid_build_response");
        Gson gson = new Gson();
        BuildResponseBody[] array = gson.fromJson(json, BuildResponseBody[].class);
        buildResponseBody = Arrays.asList(array);
    }

    @Test
    public void shouldMapToBuild() throws Exception {
        // act
        Build build = buildResponseBody.get(0).toBuild();

        // assert
        assertThat(build.getId()).isEqualTo("58b899061baced0100616172");
        assertThat(build.getCommitMessage()).isEqualTo("Add new crash type");
        assertThat(build.getBuildNumber()).isEqualTo(2);
        assertThat(build.getAuthor()).isEqualTo("David Pie");
        assertThat(build.getBranchName()).isEqualTo("master");
        assertThat(build.getBuildStatus()).isEqualTo(Build.Status.FAILED);

        // TODO add tests for build status and create, start, finish, times;
    }
}
