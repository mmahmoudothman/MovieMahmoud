package com.example.osos.moviemahmoud.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.osos.moviemahmoud.R;

import static com.example.osos.moviemahmoud.data.MoviesContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID;
import static com.example.osos.moviemahmoud.data.MoviesContract.FavouriteMoviesEntry.COLUMN_TITLE;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.GuestViewHolder>
{



    private Cursor mCursor;
    private Context mContext;



    public FavoriteAdapter(Context context, Cursor cursor)
    {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public GuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.favorite_item, parent, false);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuestViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
            return;

        String title = mCursor.getString(mCursor.getColumnIndex(COLUMN_TITLE));
        Long movie_id = mCursor.getLong(mCursor.getColumnIndex(COLUMN_MOVIE_ID));
        holder.title.setText(title);
        holder.movieId.setText(String.valueOf(movie_id));

    }

    @Override
    public int getItemCount() {

        return mCursor.getCount();
    }


    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            this.notifyDataSetChanged();
        }
    }


    class GuestViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView movieId;


        public GuestViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.favorite_title);
            movieId = (TextView) itemView.findViewById(R.id.favorite_id);
        }

    }
}