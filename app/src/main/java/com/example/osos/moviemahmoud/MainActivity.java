package com.example.osos.moviemahmoud;


import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.osos.moviemahmoud.Response.MoviesResponse;
import com.example.osos.moviemahmoud.adapter.FavoriteAdapter;
import com.example.osos.moviemahmoud.adapter.MoviesAdapter;
import com.example.osos.moviemahmoud.api.ApiClient;
import com.example.osos.moviemahmoud.api.ApiInterface;
import com.example.osos.moviemahmoud.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.osos.moviemahmoud.data.MoviesContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ProgressDialog prograss;
    RecyclerView.LayoutManager manager;
    private final static String API_KEY = BuildConfig.API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        init();
    }

    private void init() {
        prograss = new ProgressDialog(this);
        prograss.setMessage("Loading ...");
        prograss.setCancelable(false);
        prograss.show();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            manager =new GridLayoutManager(this,2);
            recyclerView.setLayoutManager(manager);

        } else {
            manager=new GridLayoutManager(this,4);
            recyclerView.setLayoutManager(manager);
        }

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No API KEY ", Toast.LENGTH_LONG).show();
            return;
        }
       Popular();

    }

    private void Popular() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call = apiService.getMovie("popular",API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> movies = response.body().getResults();

                MoviesAdapter moviesAdapter=new MoviesAdapter(getApplicationContext(),movies);
                recyclerView.setAdapter(moviesAdapter);
                prograss.dismiss();
            }


            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("Error", t.toString());
                Toast.makeText(MainActivity.this, "Error No Data", Toast.LENGTH_SHORT).show();
            }

        });


    }

    private void TopRated() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call = apiService.getMovie("top_rated",API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> movies = response.body().getResults();
                MoviesAdapter moviesAdapter=new MoviesAdapter(getApplicationContext(),movies);
                recyclerView.setAdapter(moviesAdapter);

                prograss.dismiss();
            }


            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error No Data", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void loadFavoriteMovies() {

//        ArrayList<Movie>array=new ArrayList<>();
//        array= getData();
        Cursor cursor=getContentResolver().query(CONTENT_URI,
                null,
                null,
                null,
                null);
       recyclerView.setAdapter(new FavoriteAdapter(getApplicationContext(), cursor));
        prograss.dismiss();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId()== R.id.action_popular)
        {
            Popular();
            Toast.makeText(this, "most popular", Toast.LENGTH_SHORT).show();

            return true;
        }else if (item.getItemId()==R.id.action_rate)
        {
            TopRated();
            Toast.makeText(this, "highest rating", Toast.LENGTH_SHORT).show();

            return true;
        }else if (item.getItemId()== R.id.action_favoirate)
        {

            loadFavoriteMovies();
            Toast.makeText(this, "Favourite ", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }







//    public  ArrayList<Movie> getData()
//    {
//        ArrayList<Movie>array=new ArrayList<>();
//        String [] coulms={COLUMN_MOVIE_ID,
////                COLUMN_DATE,
//                COLUMN_POSTER_PATH,COLUMN_OVERVIEW,COLUMN_RATING,COLUMN_TITLE};
//
//        Cursor cursor=getContentResolver().query(CONTENT_URI,
//                null,
//                null,
//                null,
//                null);
//
//        for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
//        {
//            Movie modelMovies=new Movie();
//            modelMovies.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
//            modelMovies.setPosterPath( cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)));
//            modelMovies.setOverview( cursor.getString(cursor.getColumnIndex(COLUMN_OVERVIEW)));
//            modelMovies.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(COLUMN_RATING)));
//            modelMovies.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_MOVIE_ID)));;
//            array.add(modelMovies);
//        }
//        return array;
//    }



}


