package com.example.arafatm.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.arafatm.flicks.models.Movie;
import com.example.arafatm.flicks.models.config;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {
    //base URL for API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //parameter name for API key
    public final static String API_KEY_PARAM = "api_key";
    //tag for logging from this activity
    public final static String TAG = "MovieListActivity";
    //instance field
    AsyncHttpClient client;

    //list of currently playing movies
    ArrayList<Movie> movies;
    //the recycler view
    RecyclerView rvMovies;
    //the adapter wired to recycler view
    MovieAdapter adapter;

    config Config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        //initialize the client
        client = new AsyncHttpClient();
        //initialize list of movies
        movies = new ArrayList<>();
        //initialize the adapter
        adapter = new MovieAdapter(movies);
        //resolve the recycler view and commit a layout manager and adapter
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);


        //get the configuration on app creation
        getConfiguration();
    }

    //gets the list of currently playing movies
    private void getNowPlaying() {
        //create url
        String url = API_BASE_URL + "/movie/now_playing";
        //sets the request param
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");
                    //loop through the set and create the movie objects
                    for (int i = 0; i < results.length(); i++) {
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);

                        //notify change
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failure passing now playing movies", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failure getting data from now laying movies", throwable, true);
            }
        });
    }

    //gets configuration from API
    private void getConfiguration() {
        //create url
        String url = API_BASE_URL + "/configuration";
        //sets the request param
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        //gets a JSON response from the GET

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {

                    Config = new config(response);
                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and posterSize %s", Config.getImageBaseUrl(), Config.getPosterSize()));

                    //pass config to adapter
                    adapter.setConfig(Config);
                    //get Now playing movies
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failure passing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failure getting configuration", throwable, true);
            }
        });
    }

    // handles errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser) {
        //always log the error
        Log.e(TAG, message, error);
        //alert the user of any error
        if (alertUser) {
            //shows a long toast with the error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }


}
