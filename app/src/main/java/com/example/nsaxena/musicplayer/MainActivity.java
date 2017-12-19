package com.example.nsaxena.musicplayer;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends Activity implements GetRawData.OnDownloadComplete{

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetRawData getRawData = new GetRawData(this);
        getRawData.execute("http://starlord.hackerearth.com/studio");
    }

    @Override
    public void onDownloadComplete(String data,DownloadStatus status)
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
