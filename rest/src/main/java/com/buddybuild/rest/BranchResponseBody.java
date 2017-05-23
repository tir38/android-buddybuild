package com.buddybuild.rest;

import com.buddybuild.core.Branch;
import com.google.gson.annotations.SerializedName;

class BranchResponseBody {

    @SerializedName("name")
    private String name;

    Branch toBranch() {
        return new Branch(name);
    }
}
