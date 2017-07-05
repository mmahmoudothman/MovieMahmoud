
package com.example.osos.moviemahmoud.Response;

import com.example.osos.moviemahmoud.model.Trailer;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@SuppressWarnings("unused")
public class TrailerResponse {

    @SerializedName("id")
    private Long mId;
    @SerializedName("results")
    private List<Trailer> mResults;

    public Long getId() {
        return mId;
    }

    public List<Trailer> getResults() {
        return mResults;
    }

    public static class Builder {

        private Long mId;
        private List<Trailer> mResults;

        public TrailerResponse.Builder withId(Long id) {
            mId = id;
            return this;
        }

        public TrailerResponse.Builder withResults(List<Trailer> results) {
            mResults = results;
            return this;
        }

        public TrailerResponse build() {
            TrailerResponse TrailerResponse = new TrailerResponse();
            TrailerResponse.mId = mId;
            TrailerResponse.mResults = mResults;
            return TrailerResponse;
        }

    }

}
