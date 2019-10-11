package com.example.ex.bakingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ex.bakingapp.models.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StepDetailsFragment extends Fragment {
    View view;
    final String EXO_PLAYER_CURRENT_POSITION_KEY = "exactPosition";
    final String EXO_PLAYER_PLAYING_KEY = "isPlaying";
    final String EXO_PLAYER_WINDOW_INDEX_KEY = "windowIndex";
    static int stepId;
    Step step;
    String videoURL;
    String thumbnailURL;
    Unbinder unbinder;
    private SimpleExoPlayer mExoPlayer;

    boolean isExoPlayerPlaying = true;
    long exoPlayerCurrentPosition = 0;
    int exoPlayerWindowIndex = 0;


    public StepDetailsFragment() {
    }

    @SuppressLint("ValidFragment")
    public StepDetailsFragment(int stepId) {
    this.stepId = stepId;

    }
    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.description_tv)
    TextView descriptionTextView;
    @BindView(R.id.navigateToPrevious_tv)
    TextView navigateToPreviousTextView;
    @BindView(R.id.navigateToNext_tv)
    TextView navigateToNextTextView;
    @BindView(R.id.tv_container)
    LinearLayout linearLayout;
    @BindView(R.id.main_media_frame)
    FrameLayout frameLayout;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recipe_step_details_fragment,container,false);
        unbinder = ButterKnife.bind(this,view);
        if (savedInstanceState != null && savedInstanceState.containsKey("exactPosition")){
            exoPlayerCurrentPosition = savedInstanceState.getLong(EXO_PLAYER_CURRENT_POSITION_KEY);
            exoPlayerWindowIndex = savedInstanceState.getInt(EXO_PLAYER_WINDOW_INDEX_KEY);
            isExoPlayerPlaying = savedInstanceState.getBoolean(EXO_PLAYER_PLAYING_KEY);
        }
        populateUI();
        if (((videoURL.length()>0)||(thumbnailURL.length()>0))&& getActivity().findViewById(R.id.tabletStepsView)==null) {
            int orientation = getResources().getConfiguration().orientation;

            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                descriptionTextView.setVisibility(View.GONE);
                linearLayout.setVisibility(View.GONE);
                navigateToNextTextView.setVisibility(View.GONE);
                navigateToPreviousTextView.setVisibility(View.GONE);
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            } else {
                descriptionTextView.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);
                navigateToNextTextView.setVisibility(View.VISIBLE);
                navigateToPreviousTextView.setVisibility(View.VISIBLE);
                ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            }
        }
        navigateToPreviousTextView.setText("<< Previous step");
        navigateToNextTextView.setText("Next Step >>");
        navigateToPreviousTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stepId!=0){
                    stepId--;
                    step = RecipeDetailsActivity.steps.get(stepId);
                    if ((step.getThumbnailURL().length()>0)||step.getVideoURL().length()>0)
                    releasePlayer();
                    populateUI();
                }else Toast.makeText(getContext(), "This is the first step", Toast.LENGTH_SHORT).show();
            }
        });
        navigateToNextTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stepId!=RecipeDetailsActivity.steps.size()-1){
                    stepId++;
                    step = RecipeDetailsActivity.steps.get(stepId);
                    if ((step.getThumbnailURL().length()>0)||step.getVideoURL().length()>0)
                        releasePlayer();
                    populateUI();
                }else Toast.makeText(getContext(), "This is the last step", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    void populateUI(){
        step = RecipeDetailsActivity.steps.get(stepId);
        thumbnailURL = step.getThumbnailURL();
        videoURL = step.getVideoURL();
        if (videoURL.length()>0){
            mPlayerView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(videoURL));

        }else if (thumbnailURL.length()>0) {
            mPlayerView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.VISIBLE);
            initializePlayer(Uri.parse(thumbnailURL));
        }else {
            mPlayerView.setVisibility(View.GONE);
            frameLayout.setVisibility(View.GONE);
        }

        descriptionTextView.setText(step.getDescription());
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mExoPlayer.addListener(new PlayerEventListener());
            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            boolean haveStartPosition = exoPlayerWindowIndex != C.INDEX_UNSET;
            if (haveStartPosition) {
                mExoPlayer.seekTo(exoPlayerWindowIndex, exoPlayerCurrentPosition);
            }
            mExoPlayer.prepare(mediaSource,!haveStartPosition,false);
            mExoPlayer.setPlayWhenReady(isExoPlayerPlaying);

        }
    }
    private void releasePlayer() {
        if (mExoPlayer != null) {
            getExoPlayerStates();
            isExoPlayerPlaying = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            populateUI();
        }
    }
    private void getExoPlayerStates() {
        exoPlayerCurrentPosition = mExoPlayer.getCurrentPosition();
        exoPlayerWindowIndex = mExoPlayer.getCurrentWindowIndex();
        isExoPlayerPlaying = mExoPlayer.getPlayWhenReady();
    }
    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            populateUI();
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class PlayerEventListener implements ExoPlayer.EventListener {

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case SimpleExoPlayer.STATE_IDLE:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SimpleExoPlayer.STATE_BUFFERING:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case SimpleExoPlayer.STATE_READY:
                    progressBar.setVisibility(View.GONE);
                    break;
                case SimpleExoPlayer.STATE_ENDED:
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity() {

        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        getExoPlayerStates();
        outState.putLong(EXO_PLAYER_CURRENT_POSITION_KEY,exoPlayerCurrentPosition);
        outState.putInt(EXO_PLAYER_WINDOW_INDEX_KEY,exoPlayerWindowIndex);
        outState.putBoolean(EXO_PLAYER_PLAYING_KEY,isExoPlayerPlaying);
        super.onSaveInstanceState(outState);
    }
}
