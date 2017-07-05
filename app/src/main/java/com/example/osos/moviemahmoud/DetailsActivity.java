package com.example.osos.moviemahmoud;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.osos.moviemahmoud.Response.ReviewResponse;
import com.example.osos.moviemahmoud.Response.TrailerResponse;
import com.example.osos.moviemahmoud.adapter.ReviewAdapter;
import com.example.osos.moviemahmoud.adapter.TrailerAdapter;
import com.example.osos.moviemahmoud.api.ApiClient;
import com.example.osos.moviemahmoud.api.ApiInterface;
import com.example.osos.moviemahmoud.model.Movie;
import com.example.osos.moviemahmoud.model.Review;
import com.example.osos.moviemahmoud.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.osos.moviemahmoud.data.MoviesContract.CONTENT_URI;
import static com.example.osos.moviemahmoud.data.MoviesContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID;
import static com.example.osos.moviemahmoud.data.MoviesContract.FavouriteMoviesEntry.COLUMN_OVERVIEW;
import static com.example.osos.moviemahmoud.data.MoviesContract.FavouriteMoviesEntry.COLUMN_POSTER_PATH;
import static com.example.osos.moviemahmoud.data.MoviesContract.FavouriteMoviesEntry.COLUMN_RATING;
import static com.example.osos.moviemahmoud.data.MoviesContract.FavouriteMoviesEntry.COLUMN_TITLE;

public class DetailsActivity extends AppCompatActivity {
    Movie movie;
    TextView title;
    TextView overView;
    TextView releaseDate;
    ImageView imageView;
    ImageView favoutiteImage;
    RatingBar mRateView;
    RecyclerView recyclerView;
    RecyclerView reviewsRecyclers;

    ArrayList<Movie> movies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Movie Detail");
        ab.setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.details_image);
        title = (TextView) findViewById(R.id.details_title);
        overView = (TextView) findViewById(R.id.details_overview);
        mRateView = (RatingBar) findViewById(R.id.ratingBar);
        favoutiteImage= (ImageView) findViewById(R.id.image);
        releaseDate = (TextView) findViewById(R.id.details_relase_date);
        recyclerView = (RecyclerView) findViewById(R.id.details_trailer);
        reviewsRecyclers = (RecyclerView) findViewById(R.id.details_review);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        reviewsRecyclers.setLayoutManager(manager);
        Intent intent = getIntent();
        movie = intent.getParcelableExtra(Intent.EXTRA_TEXT);

        if (IsFavourite()){
            favoutiteImage.setImageResource(R.drawable.photo2);
            favoutiteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoutiteImage.setImageResource(R.drawable.photo1);
                    getContentResolver().delete(CONTENT_URI,
                            COLUMN_MOVIE_ID + "=?",
                            new String[]{String.valueOf(movie.getId())});

                }
            });
        }else {     //not favoutite
            favoutiteImage.setImageResource(R.drawable.photo1);
            favoutiteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoutiteImage.setImageResource(R.drawable.photo2);

                    ContentValues values = new ContentValues();
                    values.put(COLUMN_TITLE, movie.getOriginalTitle());
                    values.put(COLUMN_OVERVIEW, movie.getOverview());
                    values.put(COLUMN_RATING, movie.getVoteAverage());
                    values.put(COLUMN_POSTER_PATH, movie.getPosterPath());
                    values.put(COLUMN_MOVIE_ID, movie.getId());
                    getContentResolver().insert(CONTENT_URI, values);

                }
            });
        }





        if (movie != null) {

            title.setText(movie.getOriginalTitle());
            overView.setText(movie.getOverview());
            releaseDate.setText(movie.getReleaseDate());
            mRateView.setRating((Float.parseFloat(String.valueOf(movie.getVoteAverage())) / 2));
            Picasso.with(this)
                    .load(movie.getPosterPath())
                    .into(imageView);

        } else {

            Toast.makeText(this, "No Data", Toast.LENGTH_SHORT).show();
        }
        long movie_id = movie.getId();
        LoadTrailer(movie_id);
        LoadReview(movie_id);


    }

    private boolean IsFavourite() {
        Cursor cursor = getContentResolver().query(CONTENT_URI,
                new String[]{COLUMN_MOVIE_ID},
                COLUMN_MOVIE_ID + "=?",
                new String[]{String.valueOf(movie.getId())},
                null);

        if ((cursor != null) && (cursor.getCount() > 0)) {
            Toast.makeText(this, "cursor=" + cursor.toString(), Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }

    }


    private void LoadTrailer(long id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<TrailerResponse> call = apiService.getMovieTrailers(id, BuildConfig.API_KEY);
        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                int statusCode = response.code();
                List<Trailer> trailers = response.body().getResults();
                recyclerView.setAdapter(new TrailerAdapter(getApplicationContext(), trailers));
                recyclerView.smoothScrollToPosition(0);

            }


            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                //    Log error here since request failed
                Log.e("Error", t.toString());
                Toast.makeText(DetailsActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show();
            }

        });


    }

    private void LoadReview(long id) {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ReviewResponse> call = apiService.getMovieReviews(id, BuildConfig.API_KEY);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                int statusCode = response.code();
                List<Review> trailers = response.body().getResults();
                reviewsRecyclers.setAdapter(new ReviewAdapter(getApplicationContext(), trailers));
                reviewsRecyclers.smoothScrollToPosition(0);

            }


            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                Toast.makeText(DetailsActivity.this, "Error ", Toast.LENGTH_SHORT).show();
            }

        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
