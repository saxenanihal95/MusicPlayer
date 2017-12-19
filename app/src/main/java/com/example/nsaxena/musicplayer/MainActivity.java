package com.example.nsaxena.musicplayer;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;


public class MainActivity extends Activity implements GetJsonData.OnDataAvailable{

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            Log.d(TAG, "onDownloadComplete: data is "+data);
        }else
        {
            Log.e(TAG, "onDownloadComplete: failed with status "+status );
        }
    }
}
