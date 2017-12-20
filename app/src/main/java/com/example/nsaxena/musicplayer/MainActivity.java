package com.example.nsaxena.musicplayer;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements GetJsonData.OnDataAvailable{

    private static final String TAG = "MainActivity";

    private RecyclerViewAdapter mRecyclerViewAdapter;

    private LinearLayout mLinearScroll;
    private ListView mListView;
    private ArrayList<Song> mArrayListSong;
    private ArrayList<Song> mArrayListSongTemp;
    // change row size according to your need, how many row you needed per page
    int rowSize = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mArrayListSong = new ArrayList<Song>();
        mArrayListSongTemp = new ArrayList<Song>();
        mLinearScroll = (LinearLayout) findViewById(R.id.linear_scroll);

        RecyclerView recyclerView =(RecyclerView)findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<Song>(),this);
        recyclerView.setAdapter(mRecyclerViewAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetJsonData getJsonData = new GetJsonData("http://starlord.hackerearth.com/studio",this);
        getJsonData.executeOnSameThread();
    }

    @Override
    public void onDataAvailable(List<Song> data, DownloadStatus status)
    {

        if(status==DownloadStatus.OK)
        {
            int rem = data.size() % rowSize;
            mArrayListSong = new ArrayList<Song>(data);

            /**
             * add arraylist item into list on page load
             */
            addItem(0);

            int size = data.size() / rowSize;
            Log.d(TAG, "onDataAvailable: data "+mArrayListSong.size());
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
        }else
        {
            Log.e(TAG, "onDownloadComplete: failed with status "+status );
        }

        Log.d(TAG, "onDataAvailable: ends");
    }
    public void addItem(int count) {
        Log.d(TAG, "addItem: "+mArrayListSongTemp.toString());
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
