package com.example.nsaxena.musicplayer;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 19/12/17.
 */

class GetJsonData implements GetRawData.OnDownloadComplete{

    private static final String TAG = "GetJsonData";

    private List<Song> mSongList = null;

    private String mURL;

    private final OnDataAvailable mCallBack;


    interface OnDataAvailable
    {
        void onDataAvailable(List<Song> data,DownloadStatus status);
    }

    public GetJsonData(String URL, OnDataAvailable callBack) {
        mURL = URL;
        mCallBack = callBack;
    }

    void executeOnSameThread()
    {
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(mURL);
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {

        if(status==DownloadStatus.OK)
        {
            mSongList=new ArrayList<>();
            try{
                JSONArray jsonArray=new JSONArray(data);
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String songTitle=jsonObject.getString("song");
                    String songUrl=jsonObject.getString("url");
                    String songArtists=jsonObject.getString("artists");
                    String songImage=jsonObject.getString("cover_image");

                    Song songObject=new Song(songTitle,songUrl,songArtists,songImage);
                    mSongList.add(songObject);

                    Log.d(TAG, "onDownloadComplete: "+songObject.toString());
                }

            }catch (JSONException jsone){
                jsone.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error Processing json data "+jsone.getMessage() );
                status=DownloadStatus.FAILED_OR_EMPTY;
            }
        }

        if(mCallBack!=null)
        {
            mCallBack.onDataAvailable(mSongList,status);
        }
        Log.d(TAG, "onDownloadComplete: ends");
    }

}
