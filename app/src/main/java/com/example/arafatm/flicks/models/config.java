package com.example.arafatm.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class config {
    String imageBaseUrl;
    //poster size
    String posterSize;

    public config(JSONObject object) throws JSONException {
        //   Log.i(TAG, response.toString()); for debugging
        JSONObject images = object.getJSONObject("images");
        //get image base url
        imageBaseUrl = images.getString("secure_base_url");
        //get posterSize
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        posterSize = posterSizeOptions.optString(3, "w342");
    }

    public String getImageUrl (String size, String path) {
        return String.format("%s%s%s", imageBaseUrl, size, path);
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }
}
