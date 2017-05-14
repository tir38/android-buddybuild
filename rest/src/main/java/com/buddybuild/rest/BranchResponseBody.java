package com.buddybuild.rest;

import com.buddybuild.core.Branch;
import com.google.gson.annotations.SerializedName;

public class BranchResponseBody {

    @SerializedName("name")
    private String name;

    // TODO add tests
    public Branch toBranch() {
        return new Branch(name);
    }
}
