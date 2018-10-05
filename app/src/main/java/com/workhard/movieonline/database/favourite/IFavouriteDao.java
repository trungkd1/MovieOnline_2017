package com.workhard.movieonline.database.favourite;

import com.workhard.movieonline.model.Movie;

import java.util.List;

/**
 * Created by TrungKD on 3/6/2017.
 */
public interface IFavouriteDao {
    List<Movie> fetchAllFavourites();
    List<Movie> fetchFavouriteFromIndex(int index);
    Movie fetchFavouriteByAlias(String alias);
    // add favourite
    boolean addFavourite(Movie movie);
    boolean addFavourites(List<Movie> movieList);
    // delete favourite
    boolean deleteFavouriteById(String alias);
    boolean deleteAllFavourites();
}
