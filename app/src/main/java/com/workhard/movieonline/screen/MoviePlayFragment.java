package com.workhard.movieonline.screen;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.Util;
import com.noveogroup.android.log.Logger;
import com.noveogroup.android.log.LoggerManager;
import com.workhard.movieonline.MainApplication;
import com.workhard.movieonline.R;
import com.workhard.movieonline.adapter.EpisodeAdapter;
import com.workhard.movieonline.adapter.SubtitleAdapter;
import com.workhard.movieonline.base.BaseFragment;
import com.workhard.movieonline.database.Database;
import com.workhard.movieonline.handler.MovieControllerHandler;
import com.workhard.movieonline.model.Episode;
import com.workhard.movieonline.model.Movie;
import com.workhard.movieonline.model.Subtitle;
import com.workhard.movieonline.rest.ApiInitialization;
import com.workhard.movieonline.rest.model.ResponseSubtitleDetail;
import com.workhard.movieonline.rest.model.ResponseSubtitleList;
import com.workhard.movieonline.util.EventLogger;
import com.workhard.movieonline.widget.EpisodeDialog;
import com.workhard.movieonline.widget.LoadingDialog;
import com.workhard.movieonline.widget.SimpleExoPlayerView;
import com.workhard.movieonline.widget.SubtitleDialog;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by TrungKD on 2/14/2017
 */
public class MoviePlayFragment extends BaseFragment implements
        MovieControllerHandler, View.OnTouchListener,
        ExoPlayer.EventListener, View.OnClickListener {
    // Logger to show log
    private       Logger logger           = LoggerManager.getLogger();
    // Delay for next touch to control the show/hide media control layout
    private final long   TOUCH_DELAY_TIME = 250L;
    private boolean isDelay;
    // Listener next/prev button
    private View.OnClickListener nextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            logger.d("next button on click");
        }
    };
    private View.OnClickListener prevListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            logger.d("previous button on click");
        }
    };

    private Movie         movie;
    private List<Episode> episodeList;

    public static final String DRM_SCHEME_UUID_EXTRA      = "drm_scheme_uuid";
    public static final String DRM_LICENSE_URL            = "drm_license_url";
    public static final String DRM_KEY_REQUEST_PROPERTIES = "drm_key_request_properties";
    public static final String PREFER_EXTENSION_DECODERS  = "prefer_extension_decoders";

    public static final String ACTION_VIEW = "com.google.android.exoplayer.demo.action.VIEW";

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private static final CookieManager DEFAULT_COOKIE_MANAGER;

    static {
        DEFAULT_COOKIE_MANAGER = new CookieManager();
        DEFAULT_COOKIE_MANAGER.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
    }

    private Handler             mainHandler;
    private EventLogger         eventLogger;
    private SimpleExoPlayerView simpleExoPlayerView;
    // Used to show/hide video or audio track info button.
    private LinearLayout        showHideTrackInfo;
    private Button              retryButton;

    private DataSource.Factory   mediaDataSourceFactory;
    private SimpleExoPlayer      player;
    private DefaultTrackSelector trackSelector;
    private boolean              needRetrySource;

    private boolean shouldAutoPlay;
    private int     resumeWindow;
    private long    resumePosition;
    private int     episodePos;

    private String subtitleUrl = "";
    private String subtitlePath = "";

    private String subtitleLang = "en";

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setEpisodePos(int episodePos) {
        this.episodePos = episodePos;
    }

    public void setEpisodeList(List<Episode> episodeList) {
        this.episodeList = episodeList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity.hideBannerAd();
        String url = "http://api.mp3.zing.vn/api/mobile/source/video/LGJGTLGNQANQDLETNDGTDGLG";
        /*if (movie != null && movie.getNoplayUrl() != null && !movie.getNoplayUrl().isEmpty()) {
            url = movie.getNoplayUrl();
        }*/

        // Auto play in first time.
        shouldAutoPlay = true;
        // Clear video current time.
        clearResumePosition();
        mediaDataSourceFactory = buildDataSourceFactory(true);
        mainHandler = new Handler();
        if (CookieHandler.getDefault() != DEFAULT_COOKIE_MANAGER) {
            CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_movie_play;
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.setImmersiveMode(true);
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity.enableLandscapeMode();

        rootView = inflater.inflate(getLayoutID(), null);
        // Handle touch event
        rootView.setOnTouchListener(this);

        // initVideoView();

        // Save the movie into history database.
        Database.historyDao.addHistory(movie);

        // rootView.setOnClickListener(this);
        showHideTrackInfo = (LinearLayout) rootView.findViewById(R.id.controls_root);
        retryButton = (Button) rootView.findViewById(R.id.retry_button);
        retryButton.setOnClickListener(this);

        simpleExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.videoSurface);
        simpleExoPlayerView.requestFocus();
        simpleExoPlayerView.setMovieControllerHandler(this);

        return rootView;
    }

    @Override
    public void close() {
        // Remove current fragment
        finish();
    }

    SubtitleDialog.Callback subtitleCallback = new SubtitleDialog.Callback() {
        @Override
        public void onSubtitleClick(final Subtitle subtitle) {
            LoadingDialog.getInstance().show(activity, true);
            // Call api get subtitle detail.
            ApiInitialization apiInitialization = new ApiInitialization(activity);
            Call<ResponseSubtitleDetail> call = baseApi.getSubtitleDetail(
                    apiInitialization.getDeviceToken(),
                    apiInitialization.getDeviceId(),
                    apiInitialization.getOsType(),
                    apiInitialization.getLanguage(),
                    apiInitialization.getHardware(),

                    subtitle.getSubtitleId()
            );
            call.enqueue(new Callback<ResponseSubtitleDetail>() {
                @Override
                public void onResponse(Call<ResponseSubtitleDetail> call, Response<ResponseSubtitleDetail> response) {
                    if (response.body() != null && response.body().getStatus()) {
                        // Get subtitle url to download and showing
                        subtitleUrl = response.body().getSrt();
                        subtitleLang = subtitle.getLanguage();
                        updateResumePosition();
                        initializePlayer();
                    } else {
                        // TODO: notice to user
                    }
                    LoadingDialog.getInstance().hide();
                }

                @Override
                public void onFailure(Call<ResponseSubtitleDetail> call, Throwable t) {
                    // TODO: notice to user
                    LoadingDialog.getInstance().hide();
                }
            });
        }
    };

    EpisodeDialog.Callback episodeCallback = new EpisodeDialog.Callback() {
        @Override
        public void onEpisodeClick(Episode episode) {
            // TODO: chooser episode for the movie.
        }
    };

    @Override
    public void openSubtitleDialog() {
        LoadingDialog.getInstance().show(activity, true);
        // Call API getSubtitleList
        ApiInitialization apiInitialization = new ApiInitialization(activity);

        Call<ResponseSubtitleList> call = baseApi.getSubtitles(
                apiInitialization.getDeviceToken(),
                apiInitialization.getDeviceId(),
                apiInitialization.getOsType(),
                apiInitialization.getLanguage(),
                apiInitialization.getHardware(),

                movie.getAlias(),
                String.valueOf(episodePos)
        );

        call.enqueue(new Callback<ResponseSubtitleList>() {
            @Override
            public void onResponse(Call<ResponseSubtitleList> call, Response<ResponseSubtitleList> response) {
                if (response.body().getStatus()) {
                    // Get subtitle data.
                    List<Subtitle>  subtitleList = response.body().getData();
                    SubtitleAdapter adapter      = new SubtitleAdapter(activity, subtitleList);
                    // Show dialog choose subtitle
                    SubtitleDialog immersiveDiaLog = new SubtitleDialog(activity, subtitleCallback, adapter);
                    immersiveDiaLog.show();
                } else {
                    // TODO: show noti
                }
                LoadingDialog.getInstance().hide();
            }

            @Override
            public void onFailure(Call<ResponseSubtitleList> call, Throwable t) {
                LoadingDialog.getInstance().hide();
            }
        });
    }

    @Override
    public void openEpisodeDialog() {
        // Dummy episode data.
        EpisodeAdapter adapter = new EpisodeAdapter(activity, episodeList);

        // Show dialog choose episode.
        EpisodeDialog immersiveDiaLog = new EpisodeDialog(activity, episodeCallback, adapter);
        immersiveDiaLog.show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isDelay) {
            return false;
        }

        isDelay = true;

        delayForNextTouch();

        return false;
    }

    private void delayForNextTouch() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isDelay = false;
            }
        }, TOUCH_DELAY_TIME);
    }

    @Override
    public boolean onBackClick() {
        finish();
        return true;
    }

    private void finish() {
        releaseMediaPlayer();
        activity.setImmersiveMode(false);
        activity.enablePortraitMode();
        activity.popBackStack();
        activity.showBannerAd();
        showActionBar();
    }

    private void releaseMediaPlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void setupActionBar() {

    }

    /*@Override
    public void onNewIntent(Intent intent) {
        releasePlayer();
        shouldAutoPlay = true;
        clearResumePosition();
        setIntent(intent);
    }*/

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializePlayer();
        } else {
            showToast(R.string.storage_permission_denied);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == retryButton) {
            initializePlayer();
        }
    }

    // Internal methods

    private void initializePlayer() {
        Bundle  intent        = getArguments();
        boolean needNewPlayer = player == null;
        if (needNewPlayer) {
            boolean preferExtensionDecoders = intent.getBoolean(PREFER_EXTENSION_DECODERS, false);
            UUID drmSchemeUuid = intent.containsKey(DRM_SCHEME_UUID_EXTRA)
                    ? UUID.fromString(intent.getString(DRM_SCHEME_UUID_EXTRA)) : null;
            DrmSessionManager<FrameworkMediaCrypto> drmSessionManager = null;
            if (drmSchemeUuid != null) {
                String   drmLicenseUrl             = intent.getString(DRM_LICENSE_URL);
                String[] keyRequestPropertiesArray = intent.getStringArray(DRM_KEY_REQUEST_PROPERTIES);
                try {
                    drmSessionManager = buildDrmSessionManager(drmSchemeUuid, drmLicenseUrl,
                            keyRequestPropertiesArray);
                } catch (UnsupportedDrmException e) {
                    int errorStringId = Util.SDK_INT < 18 ? R.string.error_drm_not_supported
                            : (e.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
                            ? R.string.error_drm_unsupported_scheme : R.string.error_drm_unknown);
                    showToast(errorStringId);
                    return;
                }
            }

            @SimpleExoPlayer.ExtensionRendererMode int extensionRendererMode =
                    ((MainApplication) activity.getApplication()).useExtensionRenderers()
                            ? (preferExtensionDecoders ? SimpleExoPlayer.EXTENSION_RENDERER_MODE_PREFER
                            : SimpleExoPlayer.EXTENSION_RENDERER_MODE_ON)
                            : SimpleExoPlayer.EXTENSION_RENDERER_MODE_OFF;
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            player = ExoPlayerFactory.newSimpleInstance(activity, trackSelector, new DefaultLoadControl(),
                    drmSessionManager, extensionRendererMode);
            player.addListener(this);

            eventLogger = new EventLogger(trackSelector);
            player.addListener(eventLogger);
            player.setAudioDebugListener(eventLogger);
            player.setVideoDebugListener(eventLogger);
            player.setMetadataOutput(eventLogger);

            simpleExoPlayerView.setPlayer(player);
            player.setPlayWhenReady(shouldAutoPlay);
        }

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = buildDataSourceFactory(true);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(intent.getString("movie_url")),
                dataSourceFactory, extractorsFactory, null, null);
        // Prepare the player with the source.

        // String subtitlePath = "/storage/emulated/0/Download/The Fate of the Furious Trailer 1 (2017).srt";
        // Uri    subtitleUri  = Uri.fromFile(new File(subtitlePath));

        // String subtitleString = "http://www.storiesinflight.com/js_videosub/jellies.srt";
        if (subtitleUrl != null && !subtitleUrl.isEmpty()) {
            Uri subtitleUri = Uri.parse(subtitleUrl);

            MediaSource subtitleSource = new SingleSampleMediaSource(subtitleUri, dataSourceFactory,
                    Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP, null,
                            Format.NO_VALUE, C.SELECTION_FLAG_DEFAULT, subtitleLang, null, 0),
                    player.getDuration());

            // Plays the video with the sideloaded subtitle.
            MergingMediaSource mergedSource = new MergingMediaSource(videoSource, subtitleSource);

            boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
            if (haveResumePosition) {
                player.seekTo(resumeWindow, resumePosition);
            }

            player.prepare(mergedSource, !haveResumePosition, false);
        } else {
            boolean haveResumePosition = resumeWindow != C.INDEX_UNSET;
            if (haveResumePosition) {
                player.seekTo(resumeWindow, resumePosition);
            }

            player.prepare(videoSource, !haveResumePosition, false);
        }

    }

    private MediaSource buildMediaSource(Uri uri, String overrideExtension) {
        int type = TextUtils.isEmpty(overrideExtension) ? Util.inferContentType(uri)
                : Util.inferContentType("." + overrideExtension);
        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
            case C.TYPE_DASH:
                return new DashMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mainHandler, eventLogger);
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler, eventLogger);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource(uri, mediaDataSourceFactory, new DefaultExtractorsFactory(),
                        mainHandler, eventLogger);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }

    private DrmSessionManager<FrameworkMediaCrypto> buildDrmSessionManager(
            UUID uuid, String licenseUrl, String[] keyRequestPropertiesArray) throws UnsupportedDrmException {
        if (Util.SDK_INT < 18) {
            return null;
        }
        HttpMediaDrmCallback drmCallback = new HttpMediaDrmCallback(licenseUrl,
                buildHttpDataSourceFactory(false));
        if (keyRequestPropertiesArray != null) {
            for (int i = 0; i < keyRequestPropertiesArray.length - 1; i += 2) {
                drmCallback.setKeyRequestProperty(keyRequestPropertiesArray[i],
                        keyRequestPropertiesArray[i + 1]);
            }
        }
        return new DefaultDrmSessionManager<>(
                uuid, FrameworkMediaDrm.newInstance(uuid), drmCallback, null, mainHandler, eventLogger);
    }

    private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            updateResumePosition();
            player.release();
            player = null;
            trackSelector = null;
            eventLogger = null;
        }
    }

    private void updateResumePosition() {
        resumeWindow = player.getCurrentWindowIndex();
        resumePosition = player.isCurrentWindowSeekable() ? Math.max(0, player.getCurrentPosition())
                : C.TIME_UNSET;
    }

    private void clearResumePosition() {
        resumeWindow = C.INDEX_UNSET;
        resumePosition = C.TIME_UNSET;
    }

    /**
     * Returns a new DataSource factory.
     *
     * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
     *                          DataSource factory.
     * @return A new DataSource factory.
     */
    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ((MainApplication) activity.getApplication())
                .buildDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    /**
     * Returns a new HttpDataSource factory.
     *
     * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
     *                          DataSource factory.
     * @return A new HttpDataSource factory.
     */
    private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
        return ((MainApplication) activity.getApplication())
                .buildHttpDataSourceFactory(useBandwidthMeter ? BANDWIDTH_METER : null);
    }

    // ExoPlayer.EventListener implementation

    @Override
    public void onLoadingChanged(boolean isLoading) {
        // Do nothing.
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_ENDED) {
            showControls();
        }
        updateButtonVisibilities();
    }

    @Override
    public void onPositionDiscontinuity() {
        if (needRetrySource) {
            // This will only occur if the user has performed a seek whilst in the error state. Update the
            // resume position so that if the user then retries, playback will resume from the position to
            // which they seeked.
            updateResumePosition();
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        // Do nothing.
    }

    @Override
    public void onPlayerError(ExoPlaybackException e) {
        String errorString = null;
        if (e.type == ExoPlaybackException.TYPE_RENDERER) {
            Exception cause = e.getRendererException();
            if (cause instanceof MediaCodecRenderer.DecoderInitializationException) {
                // Special case for decoder initialization failures.
                MediaCodecRenderer.DecoderInitializationException decoderInitializationException =
                        (MediaCodecRenderer.DecoderInitializationException) cause;
                if (decoderInitializationException.decoderName == null) {
                    if (decoderInitializationException.getCause() instanceof MediaCodecUtil.DecoderQueryException) {
                        errorString = getString(R.string.error_querying_decoders);
                    } else if (decoderInitializationException.secureDecoderRequired) {
                        errorString = getString(R.string.error_no_secure_decoder,
                                decoderInitializationException.mimeType);
                    } else {
                        errorString = getString(R.string.error_no_decoder,
                                decoderInitializationException.mimeType);
                    }
                } else {
                    errorString = getString(R.string.error_instantiating_decoder,
                            decoderInitializationException.decoderName);
                }
            }
        }
        if (errorString != null) {
            showToast(errorString);
        }
        needRetrySource = true;
        if (isBehindLiveWindow(e)) {
            clearResumePosition();
            initializePlayer();
        } else {
            updateResumePosition();
            updateButtonVisibilities();
            showControls();
        }
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        updateButtonVisibilities();
        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo != null) {
            if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_VIDEO)
                    == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                showToast(R.string.error_unsupported_video);
            }
            if (mappedTrackInfo.getTrackTypeRendererSupport(C.TRACK_TYPE_AUDIO)
                    == MappingTrackSelector.MappedTrackInfo.RENDERER_SUPPORT_UNSUPPORTED_TRACKS) {
                showToast(R.string.error_unsupported_audio);
            }
        }
    }

    // User controls

    private void updateButtonVisibilities() {
        showHideTrackInfo.removeAllViews();

        retryButton.setVisibility(needRetrySource ? View.VISIBLE : View.GONE);
        showHideTrackInfo.addView(retryButton);
    }

    private void showControls() {
        showHideTrackInfo.setVisibility(View.VISIBLE);
    }

    private void showToast(int messageId) {
        showToast(getString(messageId));
    }

    private void showToast(String message) {
        Toast.makeText(activity.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private static boolean isBehindLiveWindow(ExoPlaybackException e) {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false;
        }
        Throwable cause = e.getSourceException();
        while (cause != null) {
            if (cause instanceof BehindLiveWindowException) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }
}