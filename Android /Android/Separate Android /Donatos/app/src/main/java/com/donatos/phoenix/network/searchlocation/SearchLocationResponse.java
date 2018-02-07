package com.donatos.phoenix.network.searchlocation;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class SearchLocationResponse {
    @JsonField(name = {"results"})
    private SearchLocationResult[] results = null;
    @JsonField(name = {"status"})
    private String status = null;

    public SearchLocationResult getResults(Integer num) {
        return (this.results == null || this.results.length <= num.intValue()) ? null : this.results[num.intValue()];
    }

    public SearchLocationResult[] getResults() {
        return this.results;
    }

    public String getStatus() {
        return this.status;
    }

    public SearchLocationResponse results(SearchLocationResult[] searchLocationResultArr) {
        this.results = searchLocationResultArr;
        return this;
    }

    public void setResults(SearchLocationResult[] searchLocationResultArr) {
        this.results = searchLocationResultArr;
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public SearchLocationResponse status(String str) {
        this.status = str;
        return this;
    }
}
