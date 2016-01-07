package com.soar.googleplayservicetest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 2016/1/6.
 */
public class FindPlaceDetailAsyncTask extends AsyncTask<Void, Void, String> {

    private StringBuilder mUri = null;
    private WeakReference<Context> mContext = null;

    public FindPlaceDetailAsyncTask(String placeId) {
        mUri = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?key=AIzaSyA_XcexX-w6yAw5SVTWBenxNnePL_ac12Y&language=zh-TW&placeid=");
        mUri.append(placeId);
    }

    public void setContext(Context context) {
        mContext = new WeakReference<Context>(context);
    }


    @Override
    protected String doInBackground(Void... params) {
        try {
            URL url = new URL(mUri.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        PlaceWrapper place = PlaceBuilder.getPlaceWrapper(s);
        Intent intent = new Intent();
        intent.putExtra(PlaceDetailActivity.KEY_PLACE, place);
        if (mContext != null) {
            Context context = mContext.get();
            if (context != null) {
                intent.setClass(context, PlaceDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                context.startActivity(intent);
            }
        }

    }
}
