package com.workhard.movieonline.util;

/**
 * Created by TrungKD on 4/24/2017.
 */
public final class Constant {
    public final class Params {
        // For all API
        public static final String DEVICE_TOKEN = "deviceToken"; // For notification pushing.
        public static final String DEVICE_ID    = "deviceId"; // Device distinguish
        public static final String OS_TYPE      = "osType"; // iOS/Android
        public static final String LANGUAGE     = "language"; // "en" is Default
        public static final String HARDWARE     = "hardware"; // device type (ex: iPhone 6)

        /**
         * Parameters for API {@link Constant.WebService#POST_APP_INFO}
         */
        public static final String DEVICE_NAME = "deviceName";
        public static final String APP_VERSION = "appVersion";
        public static final String APP_NAME    = "appName";
        public static final String OS_NAME     = "osName";
        public static final String OS_VERSION  = "osVersion";
        public static final String IP           = "ip";
        public static final String COUNTRY_NAME = "countryName";
        public static final String COUNTRY_CODE = "countryCode";
        public static final String REGION_CODE  = "regionCode";
        public static final String ISP          = "isp";
        public static final String CITY         = "city";
        public static final String TOKEN        = "token"; // MD5 of time
        public static final String TIME         = "time"; // current time

        /**
         * Parameters for API
         * {@link Constant.WebService#GET_POPULAR}
         * {@link Constant.WebService#GET_TRENDING}
         * {@link Constant.WebService#GET_LAST_UPDATE}
         * {@link Constant.WebService#GET_GENRE_DETAIL}
         */
        public static final String PAGE = "page";

        /**
         * Parameters for API {@link Constant.WebService#GET_GENRE_DETAIL}
         */
        public static final String CATEGORY = "cat";

        /**
         * Parameters for API {@link Constant.WebService#GET_MOVIE_DETAIL}
         */
        public static final String ALIAS = "alias";

        /**
         * Parameters for API {@link Constant.WebService#GET_EPISODES}
         */
        public static final String SERIAL_ALIAS = "serialAlias";
        public static final String SERVER_ID    = "serverId";

        /**
         * Parameters for API {@link Constant.WebService#GET_SUBTITLE_LIST}
         */
        public static final String EPISODE_POS = "episode";

        /**
         * Parameters for API {@link Constant.WebService#GET_SUBTITLE_DETAIL}
         */
        public static final String SUBTITLE_ID = "subtitleId";
    }

    public final class WebService {
        public static final String GET_LOCATION_URL = "http://ip-api.com/json/";
        public static final String BASE_URL         = "http://ytapp.xyz/mkiop/";

        public static final String POST_APP_INFO = "info2";

        public static final String GET_POPULAR     = "getPopular";
        public static final String GET_TRENDING    = "getTrending";
        public static final String GET_LAST_UPDATE = "getLastUpdate";

        public static final String GET_GENRES       = "getCategory";
        public static final String GET_GENRE_DETAIL = "search";

        public static final String GET_MOVIE_DETAIL = "getDetail";
        public static final String GET_EPISODES     = "getMovieBySource2";

        public static final String GET_MORE_LIKE = "search";

        public static final String GET_SUBTITLE_LIST   = "getSubtitle";
        public static final String GET_SUBTITLE_DETAIL = "subtitle2";
    }
}