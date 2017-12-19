package com.example.nsaxena.musicplayer;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements GetJsonData.OnDataAvailable{

    private static final String TAG = "MainActivity";

    private RecyclerViewAdapter mRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            mRecyclerViewAdapter.loadNewData(data);
        }else
        {
            Log.e(TAG, "onDownloadComplete: failed with status "+status );
        }

        Log.d(TAG, "onDataAvailable: ends");
    }
}
