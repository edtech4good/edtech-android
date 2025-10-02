package com.fortyk.studentapp.activities;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.fortyk.studentapp.R;
import com.fortyk.studentapp.utils.Helper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.fortyk.studentapp.utils.Constants.fromBrickFilePath;

public class VideoPlayerActivity extends BaseActivity {

    @BindView(R.id.main_layout)
    RelativeLayout mainLayout;
    @BindView(R.id.video_view)
    VideoView videoView;
    @BindView(R.id.pause)
    ImageView pause;
    @BindView(R.id.play)
    ImageView play;
    @BindView(R.id.back_button)
    ImageView back;
    @BindView(R.id.progress_seekbar)
    SeekBar progressSeekbar;

    private int stopPosition = 0;
    private static final int delayTime = 3000;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        ButterKnife.bind(this);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String resourcePath = extras.getString("path");

            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            playResource(resourcePath);
        }

        progressSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (b) {
                    videoView.seekTo(progress);
                }
            }
        });
    }

    @OnClick({R.id.main_layout, R.id.play, R.id.pause, R.id.back_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_layout:
                play.setVisibility(View.GONE);
                pause.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        pause.setVisibility(View.GONE);
                    }
                }, delayTime);
                break;
            case R.id.play:
                //onStart();
                play();
                play.setVisibility(View.GONE);
                pause.setVisibility(View.GONE);
                break;
            case R.id.pause:
                onPause();
                play.setVisibility(View.VISIBLE);
                pause.setVisibility(View.GONE);
                break;
            case R.id.back_button:
                finish();
                break;
        }
    }

    public void playResource(String videoFilePath) {
        pause.setVisibility(View.GONE);
        play.setVisibility(View.GONE);
        if (videoFilePath != null) {
            videoView.setVideoPath(videoFilePath);
            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoView.start();
                    progressDialog.dismiss();
                    progressSeekbar.setMax(videoView.getDuration());
                    progressSeekbar.postDelayed(progress, 0);
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    onStop();
                   // progressSeekbar.setProgress(videoView.getCurrentPosition());
                }
            });

            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.d("anupam", what + " " + extra);
                    progressDialog.dismiss();
                    return false;
                }
            });
        }
    }

    private Runnable progress = new Runnable() {
        @Override
        public void run() {
            if (progressSeekbar != null) {
                progressSeekbar.setProgress(videoView.getCurrentPosition());
            }
            if (videoView.isPlaying()) {
                progressSeekbar.postDelayed(progress, 0);
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        stopPosition = videoView.getCurrentPosition();
        if (videoView.isPlaying())
            videoView.pause();
    }

    private void play(){
        if (videoView != null) {
            stopPosition = videoView.getCurrentPosition();
            if (!videoView.isPlaying()) {
                if (stopPosition > 0) {
                    videoView.seekTo(stopPosition);
                } else {
                    videoView.seekTo(0);
                }
                progressSeekbar.postDelayed(progress, 0);
                videoView.start();
            }
        }
    }

  /*  @Override
    public void onStart() {
        super.onStart();
        if (videoView != null) {

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoView.start();
                    progressSeekbar.setMax(videoView.getDuration());
                    progressSeekbar.postDelayed(progress, 10);
                }
            });
        }
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        play.setVisibility(View.VISIBLE);
        pause.setVisibility(View.GONE);
        videoView.seekTo(0);
        progressSeekbar.setMax(videoView.getDuration());
        progressSeekbar.postDelayed(progress, 0);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        play.setVisibility(View.GONE);
        videoView.seekTo(stopPosition);
    }
}
