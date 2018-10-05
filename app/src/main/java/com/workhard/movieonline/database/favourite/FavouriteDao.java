package com.workhard.movieonline.database.favourite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.workhard.movieonline.database.DbContentProvider;
import com.workhard.movieonline.model.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TrungKD on 3/6/2017.
 */
public class FavouriteDao extends DbContentProvider
        implements IFavouriteSchema, IFavouriteDao {
    private Logger logger = LoggerManager.getLogger(FavouriteDao.class);

    private Cursor        cursor;
    private ContentValues initialValues;

    public FavouriteDao(SQLiteDatabase db) {
        super(db);
    }

    @Override
    protected Movie cursorToEntity(Cursor cursor) {
        Movie movie = new Movie();

        int alias;
        int thumb;
        int name;
        int is_bookmark;
        int episode_count;

        if (cursor != null) {
            if (cursor.getColumnIndex(COLUMN_ALIAS) != -1) {
                alias = cursor.getColumnIndexOrThrow(COLUMN_ALIAS);
                movie.setAlias(cursor.getString(alias));
            }

            if (cursor.getColumnIndex(COLUMN_THUMBNAIL) != -1) {
                thumb = cursor.getColumnIndexOrThrow(COLUMN_THUMBNAIL);
                movie.setCover(cursor.getString(thumb));
            }

            if (cursor.getColumnIndex(COLUMN_NAME) != -1) {
                name = cursor.getColumnIndexOrThrow(COLUMN_NAME);
                movie.setName(cursor.getString(name));
            }

            if (cursor.getColumnIndex(COLUMN_IS_BOOKMARK) != -1) {
                is_bookmark = cursor.getColumnIndexOrThrow(COLUMN_IS_BOOKMARK);
                movie.setBookmark(cursor.getInt(is_bookmark) == 1);
            }

            if (cursor.getColumnIndex(COLUMN_EPISODE_COUNT) != -1) {
                episode_count = cursor.getColumnIndexOrThrow(COLUMN_EPISODE_COUNT);
                movie.setEpisodesCount(cursor.getInt(episode_count));
            }
        }

        return movie;
    }

    private void setContentValue(Movie movie) {
        initialValues = new ContentValues();
        initialValues.put(COLUMN_ALIAS, movie.getAlias());
        initialValues.put(COLUMN_THUMBNAIL, movie.getCover());
        initialValues.put(COLUMN_NAME, movie.getName());
        initialValues.put(COLUMN_IS_BOOKMARK, movie.isBookmark());
        initialValues.put(COLUMN_EPISODE_COUNT, movie.getEpisodesCount());
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

    @Override
    public List<Movie> fetchAllFavourites() {
        List<Movie> movieList = new ArrayList<>();

        cursor = super.query(TABLE_NAME, FAVOURITE_COLUMNS, null, null, COLUMN_ALIAS);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Movie movie = cursorToEntity(cursor);
                movieList.add(movie);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return movieList;
    }

    @Override
    public List<Movie> fetchFavouriteFromIndex(int index) {
        List<Movie> movieList = new ArrayList<>();

        cursor = super.query(TABLE_NAME, FAVOURITE_COLUMNS, COLUMN_ID + " > " + index, null, COLUMN_ID + " ASC", "10");

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Movie movie = cursorToEntity(cursor);
                movieList.add(movie);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return movieList;
    }

    @Override
    public Movie fetchFavouriteByAlias(String alias) {
        Movie movie = new Movie();

        cursor = super.query(TABLE_NAME, FAVOURITE_COLUMNS, COLUMN_ALIAS + " = \"" + alias + "\"", null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                movie = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return movie;
    }

    @Override
    public boolean addFavourite(Movie movie) {
        if (movie == null) {
            return false;
        }

        setContentValue(movie);
        try {
            return super.insert(TABLE_NAME, getContentValue()) > 0;
        } catch (SQLiteConstraintException e) {
            logger.d("SQLiteConstraintException: " + e);
            return false;
        }
    }

    @Override
    public boolean addFavourites(List<Movie> movieList) {
        return false;
    }

    @Override
    public boolean deleteFavouriteById(String alias) {
        try {
            return super.delete(TABLE_NAME, COLUMN_ALIAS + " = \"" + alias + "\"", null) > 0;
        } catch (SQLiteConstraintException e) {
            logger.d("SQLiteConstraintException: " + e);
            return false;
        }
    }

    @Override
    public boolean deleteAllFavourites() {
        try {
            return super.delete(TABLE_NAME, null, null) > 0;
        } catch (SQLiteConstraintException e) {
            logger.d("SQLiteConstraintException: " + e);
            return false;
        }
    }
}
