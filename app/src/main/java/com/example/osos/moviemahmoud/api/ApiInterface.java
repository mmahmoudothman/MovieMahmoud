package com.example.osos.moviemahmoud.api;


import com.example.osos.moviemahmoud.Response.MoviesResponse;
import com.example.osos.moviemahmoud.Response.ReviewResponse;
import com.example.osos.moviemahmoud.Response.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    //define what comes from the query "MovieResponse"
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);
    @GET("movie/popular")
    Call<MoviesResponse> getMostPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{sort}")
    Call<MoviesResponse> getMovie(@Path("sort") String order, @Query("api_key") String key);
    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getMovieTrailers(@Path("movie_id") long id, @Query("api_key") String apiKey);
    @GET("movie/{movie_id}/reviews")
    Call<ReviewResponse> getMovieReviews(@Path("movie_id") long id, @Query("api_key") String apiKey);

}
