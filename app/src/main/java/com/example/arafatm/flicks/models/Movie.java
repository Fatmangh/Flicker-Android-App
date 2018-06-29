package com.example.arafatm.flicks.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;


@Parcel
public class Movie {

    //values from API
    public String title;
    public String overview;
    public String posterPath;
    public Double voteAverage;
    public Integer id;
    //add more later

    public Movie() {}

    //initialize JSON data
    public Movie (JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        voteAverage = object.getDouble("vote_average");
        id = object.getInt("id");
        //add more later
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Integer getId() {
        return id;
    }
}
