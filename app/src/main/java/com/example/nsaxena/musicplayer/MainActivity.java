package com.example.nsaxena.musicplayer;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;


import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends Activity implements GetJsonData.OnDataAvailable {

    private static final String TAG = "MainActivity";

    private RecyclerViewAdapter mRecyclerViewAdapter;

    private LinearLayout mLinearScroll;

    private ListView mListView;

    private ArrayList<Song> mArrayListSong;

    private ArrayList<Song> mArrayListSongTemp;
    // change row size according to your need, how many row you needed per page

    private MediaPlayer mMediaPlayer;

    private SeekBar mSeekBar;

    ProgressDialog mProgressDialog;

    int rowSize = 4;

    Handler mSeekBarHandler, mDownloadManagerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mSeekBarHandler = new Handler();

        mDownloadManagerHandler = new Handler();

        mLinearScroll = (LinearLayout) findViewById(R.id.linear_scroll);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);

        mSeekBar = (SeekBar) findViewById(R.id.seekbar);

        mArrayListSong = new ArrayList<Song>();

        mArrayListSongTemp = new ArrayList<Song>();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<Song>(), this);
        recyclerView.setAdapter(mRecyclerViewAdapter);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            boolean userTouch;

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                userTouch = false;
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
                userTouch = true;
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                if (mMediaPlayer != null) {
                    if (mMediaPlayer.isPlaying() && userTouch)
                        mMediaPlayer.seekTo(arg1);

                }
            }
        });

        mRecyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final Button button, View view, final Song songObject, int position) {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (button.getText().toString().equals("Stop")) {
                                button.setText("Play");
                                mMediaPlayer.stop();
                                mMediaPlayer.reset();
                                mMediaPlayer.release();
                                mMediaPlayer = null;
                            } else {
                                mMediaPlayer = new MediaPlayer();
                                mMediaPlayer.setDataSource(songObject.getSongUrl());
                                mMediaPlayer.prepareAsync();
                                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mediaPlayer) {
                                        mediaPlayer.start();
                                        mSeekBar.setProgress(0);
                                        mSeekBar.setMax(mediaPlayer.getDuration());
                                    }
                                });
                                button.setText("Stop");
                            }
                        } catch (IOException e) {

                        }
                    }
                };
                mSeekBarHandler.postDelayed(r, 100);

            }
        });

        Thread seekBarThread = new SeekBarThread();
        seekBarThread.run();
    }

    public class SeekBarThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                if (mMediaPlayer != null) {
                    mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.linear_scroll);
        ll.removeAllViews();
        super.onResume();
        GetJsonData getJsonData = new GetJsonData("http://starlord.hackerearth.com/studio", this);
        getJsonData.executeOnSameThread();
    }

    @Override
    public void onDataAvailable(List<Song> data, DownloadStatus status) {

        if (status == DownloadStatus.OK) {
            int rem = data.size() % rowSize;
            mArrayListSong = new ArrayList<Song>(data);

            /**
             * add arraylist item into list on page load
             */
            addItem(0);

            int size = mArrayListSong.size() / rowSize;
            Log.d(TAG, "onDataAvailable: data " + mArrayListSong.size());
            for (int j = 0; j < size; j++) {
                final int k;
                k = j;
                final Button btnPage = new Button(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(120,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(5, 2, 2, 2);
                btnPage.setTextColor(Color.WHITE);
                btnPage.setTextSize(26.0f);
                btnPage.setId(j);
                btnPage.setText(String.valueOf(j + 1));
                mLinearScroll.addView(btnPage, lp);

                btnPage.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        /**
                         * add arraylist item into list
                         */
                        addItem(k);

                    }
                });
            }
            //mRecyclerViewAdapter.loadNewData(data);
        } else {
            Log.e(TAG, "onDownloadComplete: failed with status " + status);
        }

        Log.d(TAG, "onDataAvailable: ends");
    }

    public void addItem(int count) {
        Log.d(TAG, "addItem: " + mArrayListSongTemp.toString());
        mArrayListSongTemp.clear();
        count = count * rowSize;
        /**
         * fill temp array list to set on page change
         */
        for (int j = 0; j < rowSize; j++) {
            mArrayListSongTemp.add(j, mArrayListSong.get(count));
            count = count + 1;
        }
        // set view

        mRecyclerViewAdapter.loadNewData(mArrayListSongTemp);
    }


}
