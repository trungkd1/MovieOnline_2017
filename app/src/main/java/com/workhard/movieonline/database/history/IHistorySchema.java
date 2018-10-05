package com.workhard.movieonline.database.history;

/**
 * Created by TrungKD on 3/5/2017.
 */
public interface IHistorySchema {
    String TABLE_NAME = "history";

    // record id
    String COLUMN_ID            = "id";
    // Movie id.
    String COLUMN_ALIAS         = "alias";
    // Movie thumbnail.
    String COLUMN_THUMBNAIL     = "thumb";
    // Movie name.
    String COLUMN_NAME          = "name";
    // bookmark flag that is used to know a movie which had been bookmark or not.
    String COLUMN_IS_BOOKMARK   = "is_bookmark";
    // The number of movie episode.
    String COLUMN_EPISODE_COUNT = "episode_count";

    String HISTORY_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ALIAS + " TEXT NOT NULL UNIQUE, "
            + COLUMN_THUMBNAIL + " TEXT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_IS_BOOKMARK + " INTEGER, "
            + COLUMN_EPISODE_COUNT + " INTEGER"
            + ")";

    String[] HISTORY_COLUMNS = new String[]{
            COLUMN_ID,
            COLUMN_ALIAS,
            COLUMN_THUMBNAIL,
            COLUMN_NAME,
            COLUMN_IS_BOOKMARK,
            COLUMN_EPISODE_COUNT
    };
}
