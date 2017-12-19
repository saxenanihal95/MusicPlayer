package com.example.nsaxena.musicplayer;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by nsaxena on 19/12/17.
 */

enum DownloadStatus{
    IDLE, // Not Processing the data
    PROCESSING, // Processing the data
    NOT_INITIALIZED, //Not a valid URL
    FAILED_OR_EMPTY, //Failed to Download or the data came back empty
    OK //valid data and Download was successful
}

class GetRawData extends AsyncTask<String,Void,String> {

    private static final String TAG = "GetRawData";

    private final OnDownloadComplete mCallback;

    private DownloadStatus mDownloadStatus;

    interface OnDownloadComplete
    {
        void onDownloadComplete(String data,DownloadStatus status);
    }

    public GetRawData(OnDownloadComplete callback) {
        this.mDownloadStatus = mDownloadStatus.IDLE;
        mCallback=callback;
    }

    @Override
    protected void onPostExecute(String s) {
        //Log.d(TAG, "onPostExecute: parameter = "+s);
        if(mCallback!=null)
        {
            mCallback.onDownloadComplete(s,mDownloadStatus);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection connection =null;
        BufferedReader reader=null;

        if(strings==null)
        {
            mDownloadStatus=DownloadStatus.NOT_INITIALIZED;
            return null;
        }
        try {
            mDownloadStatus=DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response=connection.getResponseCode();
            Log.d(TAG, "doInBackground: The response code was "+response);
            StringBuilder result = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while (null!=(line=reader.readLine()))
            {
                result.append(line).append("\n");
            }
            mDownloadStatus=DownloadStatus.OK;
            return result.toString();
        }catch (MalformedURLException e){
            Log.e(TAG, "doInBackground: Invalid URL "+ e.getMessage());
        }catch (IOException e){
            Log.e(TAG, "doInBackground: IO Exception reading data"+e.getMessage() );
        }catch (SecurityException e){
            Log.e(TAG, "doInBackground: Security Exception. Needs permsission? "+e.getMessage() );
        }finally {
            if(connection!=null){
                connection.disconnect();
            }if(reader!=null){
                try {
                    reader.close();
                }catch (IOException e){
                    Log.e(TAG, "doInBackground: Error closing stream "+e.getMessage() );
                }

            }
        }

        mDownloadStatus=DownloadStatus.FAILED_OR_EMPTY;
        return null;
    }
}
