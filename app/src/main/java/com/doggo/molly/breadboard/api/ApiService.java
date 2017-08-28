package com.doggo.molly.breadboard.api;

import com.doggo.molly.breadboard.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author Abhishek Vadnerkar
 */

public interface ApiService {
    @GET("movies")
    Call<List<Movie>> getMovies();
}
