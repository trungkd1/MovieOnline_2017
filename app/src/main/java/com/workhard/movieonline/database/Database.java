package com.workhard.movieonline.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.workhard.movieonline.database.favourite.FavouriteDao;
import com.workhard.movieonline.database.favourite.IFavouriteSchema;
import com.workhard.movieonline.database.history.HistoryDao;
import com.workhard.movieonline.database.history.IHistorySchema;

/**
 * Created by TrungKD on 3/5/2017.
 * <p/>
 * Reference urls:
 * http://wale.oyediran.me/2015/04/02/android-sqlite-dao-design/
 * http://www.androiddesignpatterns.com/2012/05/correctly-managing-your-sqlite-database.html
 */
public class Database {
    private static Logger logger = LoggerManager.getLogger(Database.class);

    private static final String DATABASE_NAME = "movie_online.db";
    private DatabaseHelper mDbHelper;
    private static final int DATABASE_VERSION = 1;
    public static HistoryDao   historyDao;
    public static FavouriteDao favouriteDao;

    private final Context context;

    public Database(Context context) {
        this.context = context;
    }

    public Database open() throws SQLException {
        mDbHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase mDb = mDbHelper.getWritableDatabase();
        historyDao = new HistoryDao(mDb);
        favouriteDao = new FavouriteDao(mDb);
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static DatabaseHelper instance;

        /**
         * Constructor should be private to prevent direct instantiation.
         * make call to static method "getInstance()" instead.
         */
        private DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public static synchronized DatabaseHelper getInstance(Context context) {
            // Use the application context, which will ensure that you
            // don't accidentally leak an Activity's context.
            // See this article for more information: http://bit.ly/6LRzfx
            if (instance == null) {
                instance = new DatabaseHelper(context.getApplicationContext());
            }
            return instance;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(IHistorySchema.HISTORY_TABLE_CREATE);
            db.execSQL(IFavouriteSchema.FAVOURITE_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            logger.w("Upgrading database from version "
                    + oldVersion + " to "
                    + newVersion + " which destroys all old data");

            db.execSQL("DROP TABLE IF EXISTS "
                    + IHistorySchema.TABLE_NAME);

            db.execSQL("DROP TABLE IF EXISTS "
                    + IFavouriteSchema.TABLE_NAME);
            onCreate(db);
        }
    }
}
