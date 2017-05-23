package com.buddybuild.rest;

import com.buddybuild.core.Branch;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BranchResponseBodyTest {

    private List<BranchResponseBody> branchResponseBodies;

    @Before
    public void setUp() throws Exception {
        String json = JsonStringReader.readJsonSampleFromFile("branch/valid_branch_response");
        Gson gson = new Gson();
        BranchResponseBody[] array = gson.fromJson(json, BranchResponseBody[].class);
        branchResponseBodies = Arrays.asList(array);
    }

    @Test
    public void shouldMapToBranch() throws Exception {
        // act
        Branch branch1 = branchResponseBodies.get(0).toBranch();
        Branch branch2 = branchResponseBodies.get(1).toBranch();

        // assert
        assertThat(branch1.getName()).isEqualTo("master");
        assertThat(branch2.getName()).isEqualTo("release");
    }
}
