package com.workhard.movieonline.database.history;

import com.workhard.movieonline.model.Movie;

import java.util.List;

/**
 * Created by TrungKD on 3/5/2017.
 */
public interface IHistoryDao {
    List<Movie> fetchAllHistories();
    List<Movie> fetchHistoryFromIndex(int index);
    // add history
    boolean addHistory(Movie movie);
    boolean addHistories(List<Movie> movieList);
    // delete history
    boolean deleteHistoryById(String alias);
    boolean deleteAllHistories();
}
