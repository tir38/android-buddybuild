package com.buddybuild.rest;

import com.buddybuild.core.Branch;
import com.google.gson.annotations.SerializedName;

import timber.log.Timber;

/**
 * Internal
 */
class BranchResponseBody {

    @SerializedName("branch_name")
    private String name;

    Branch toBranch() {
        if (name == null) { /// TODO add test, try to deserialize null name
            Timber.e("name is null");
            return null;
        }

        return new Branch(name);
    }
}
