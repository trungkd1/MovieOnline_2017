package com.workhard.movieonline;

import android.app.Application;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.workhard.movieonline.database.Database;

/**
 * Created by TrungKD on 3/5/2017.
 */
public class MainApplication extends Application {
    public static Database database;
    protected     String   userAgent;
    private final int TIME_OUT = 30 * 1000;

    @Override
    public void onCreate() {
        super.onCreate();

        userAgent = Util.getUserAgent(this, getString(R.string.app_name));

        database = new Database(this);
        database.open();
    }

    @Override
    public void onTerminate() {
        database.close();
        super.onTerminate();
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(this, bandwidthMeter, buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter, TIME_OUT, TIME_OUT, true);
    }

    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals("withExtensions");
    }
}
