package com.example.arafatm.flicks;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.arafatm.flicks.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;

import org.parceler.Parcels;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    public final static String TAG = "MovieTrailerActivity";
    //base URL for API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    //parameter name for API key
    public final static String API_KEY_PARAM = "api_key";
    //instance field
    AsyncHttpClient client;
    Movie movie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));

        // temporary test video id -- TODO replace with movie trailer video id
        final Integer videoId = movie.getId();



        // resolve the player view from the layout
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

        // initialize with API key stored in secrets.xml
        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                YouTubePlayer youTubePlayer, boolean b) {
                // do any work here to cue video, play video, etc.
                youTubePlayer.cueVideo(String.valueOf(videoId));
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                YouTubeInitializationResult youTubeInitializationResult) {
                // log the error
                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
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
