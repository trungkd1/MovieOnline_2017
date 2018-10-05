package com.workhard.movieonline.rest;

import com.workhard.movieonline.rest.model.BaseListResponse;
import com.workhard.movieonline.rest.model.ResponseEpisodeList;
import com.workhard.movieonline.rest.model.ResponseGenreDetail;
import com.workhard.movieonline.rest.model.ResponseGenreList;
import com.workhard.movieonline.rest.model.ResponseMovieDetail;
import com.workhard.movieonline.rest.model.ResponseMovieList;
import com.workhard.movieonline.rest.model.ResponseSubtitleDetail;
import com.workhard.movieonline.rest.model.ResponseSubtitleList;
import com.workhard.movieonline.util.Constant;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by TrungKD on 4/24/2017.
 */
public interface IBaseApi {
    @POST(Constant.WebService.POST_APP_INFO)
    Call<ResponseBody> postAppInfo(
            // All API have the parameters.
            @Header(Constant.Params.DEVICE_TOKEN) String deviceToken,
            @Header(Constant.Params.DEVICE_ID) String deviceId,
            @Header(Constant.Params.OS_TYPE) String osType,
            @Header(Constant.Params.LANGUAGE) String language,
            @Header(Constant.Params.HARDWARE) String hardware,

            @Query(Constant.Params.DEVICE_NAME) String deviceName,
            @Query(Constant.Params.APP_VERSION) String appVersion,
            @Query(Constant.Params.APP_NAME) String appName,
            @Query(Constant.Params.OS_NAME) String osName,
            @Query(Constant.Params.OS_VERSION) String osVersion,
            @Query(Constant.Params.IP) String ip,
            @Query(Constant.Params.COUNTRY_NAME) String countryName,
            @Query(Constant.Params.COUNTRY_CODE) String countryCode,
            @Query(Constant.Params.REGION_CODE) String regionCode,
            @Query(Constant.Params.ISP) String isp,
            @Query(Constant.Params.CITY) String city,
            @Query(Constant.Params.TOKEN) String token,
            @Query(Constant.Params.TIME) String time);

    @GET(Constant.WebService.GET_POPULAR)
    Call<ResponseMovieList> getPopular(
            // All API have the parameters.
            @Header(Constant.Params.DEVICE_TOKEN) String deviceToken,
            @Header(Constant.Params.DEVICE_ID) String deviceId,
            @Header(Constant.Params.OS_TYPE) String osType,
            @Header(Constant.Params.LANGUAGE) String language,
            @Header(Constant.Params.HARDWARE) String hardware,

            @Query(Constant.Params.PAGE) String page);

    @GET(Constant.WebService.GET_TRENDING)
    Call<ResponseMovieList> getTrending(
            // All API have the parameters.
            @Header(Constant.Params.DEVICE_TOKEN) String deviceToken,
            @Header(Constant.Params.DEVICE_ID) String deviceId,
            @Header(Constant.Params.OS_TYPE) String osType,
            @Header(Constant.Params.LANGUAGE) String language,
            @Header(Constant.Params.HARDWARE) String hardware,

            @Query(Constant.Params.PAGE) String page);

    @GET(Constant.WebService.GET_LAST_UPDATE)
    Call<ResponseMovieList> getLastUpdate(
            // All API have the parameters.
            @Header(Constant.Params.DEVICE_TOKEN) String deviceToken,
            @Header(Constant.Params.DEVICE_ID) String deviceId,
            @Header(Constant.Params.OS_TYPE) String osType,
            @Header(Constant.Params.LANGUAGE) String language,
            @Header(Constant.Params.HARDWARE) String hardware,

            @Query(Constant.Params.PAGE) String page);

    @GET(Constant.WebService.GET_GENRES)
    Call<ResponseGenreList> getGenres(
            // All API have the parameters.
            @Header(Constant.Params.DEVICE_TOKEN) String deviceToken,
            @Header(Constant.Params.DEVICE_ID) String deviceId,
            @Header(Constant.Params.OS_TYPE) String osType,
            @Header(Constant.Params.LANGUAGE) String language,
            @Header(Constant.Params.HARDWARE) String hardware);


    @GET(Constant.WebService.GET_GENRE_DETAIL)
    Call<ResponseGenreDetail> getGenreDetail(
            // All API have the parameters.
            @Header(Constant.Params.DEVICE_TOKEN) String deviceToken,
            @Header(Constant.Params.DEVICE_ID) String deviceId,
            @Header(Constant.Params.OS_TYPE) String osType,
            @Header(Constant.Params.LANGUAGE) String language,
            @Header(Constant.Params.HARDWARE) String hardware,

            @Query(Constant.Params.CATEGORY) String category,
            @Query(Constant.Params.PAGE) String page
    );

    @GET(Constant.WebService.GET_MOVIE_DETAIL)
    Call<ResponseMovieDetail> getMovieDetail(
            // All API have the parameters.
            @Header(Constant.Params.DEVICE_TOKEN) String deviceToken,
            @Header(Constant.Params.DEVICE_ID) String deviceId,
            @Header(Constant.Params.OS_TYPE) String osType,
            @Header(Constant.Params.LANGUAGE) String language,
            @Header(Constant.Params.HARDWARE) String hardware,

            @Query(Constant.Params.ALIAS) String movieAlias
    );

    @GET(Constant.WebService.GET_EPISODES)
    Call<ResponseEpisodeList> getEpisodes(
            // All API have the parameters.
            @Header(Constant.Params.DEVICE_TOKEN) String deviceToken,
            @Header(Constant.Params.DEVICE_ID) String deviceId,
            @Header(Constant.Params.OS_TYPE) String osType,
            @Header(Constant.Params.LANGUAGE) String language,
            @Header(Constant.Params.HARDWARE) String hardware,

            @Query(Constant.Params.SERIAL_ALIAS) String movieAlias,
            @Query(Constant.Params.SERVER_ID) String serverId
    );

    @GET(Constant.WebService.GET_MORE_LIKE)
    Call<ResponseMovieList> getMoreLike(
            // All API have the parameters.
            @Header(Constant.Params.DEVICE_TOKEN) String deviceToken,
            @Header(Constant.Params.DEVICE_ID) String deviceId,
            @Header(Constant.Params.OS_TYPE) String osType,
            @Header(Constant.Params.LANGUAGE) String language,
            @Header(Constant.Params.HARDWARE) String hardware,

            @Query(Constant.Params.SERIAL_ALIAS) String movieAlias
    );

    @GET(Constant.WebService.GET_SUBTITLE_LIST)
    Call<ResponseSubtitleList> getSubtitles(
            // All API have the parameters.
            @Header(Constant.Params.DEVICE_TOKEN) String deviceToken,
            @Header(Constant.Params.DEVICE_ID) String deviceId,
            @Header(Constant.Params.OS_TYPE) String osType,
            @Header(Constant.Params.LANGUAGE) String language,
            @Header(Constant.Params.HARDWARE) String hardware,

            @Query(Constant.Params.SERIAL_ALIAS) String movieAlias,
            @Query(Constant.Params.EPISODE_POS) String episodePos
    );

    @GET(Constant.WebService.GET_SUBTITLE_DETAIL)
    Call<ResponseSubtitleDetail> getSubtitleDetail(
            // All API have the parameters.
            @Header(Constant.Params.DEVICE_TOKEN) String deviceToken,
            @Header(Constant.Params.DEVICE_ID) String deviceId,
            @Header(Constant.Params.OS_TYPE) String osType,
            @Header(Constant.Params.LANGUAGE) String language,
            @Header(Constant.Params.HARDWARE) String hardware,

            @Query(Constant.Params.SUBTITLE_ID) String subtitleId
    );
}