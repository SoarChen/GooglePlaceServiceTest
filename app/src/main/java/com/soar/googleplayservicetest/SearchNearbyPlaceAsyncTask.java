package com.soar.googleplayservicetest;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by user on 2015/12/31.
 */
public class SearchNearbyPlaceAsyncTask extends AsyncTask<Void, Void, String> {

    private String mLocation = null;
    private String mRadius = null;
    private List<String> mTypes = null;
    private String mKeyword = null;
    private StringBuilder mUri = null;

    SearchNearbyPlaceAsyncTask(String location, String radius) {
        mUri = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=");
        mUri.append(location);
        mUri.append("&radius=");
        mUri.append(radius);
        // api key
        mUri.append("&key=AIzaSyA_XcexX-w6yAw5SVTWBenxNnePL_ac12Y");
    }

    public void addTypes(List<String> mTypes) {
        if (mUri != null) {
            mUri.append("&types=");
            int size = mTypes.size();
            for (int i = 0; i < size; ++i) {
                String type = mTypes.get(i);
                if (!TextUtils.isEmpty(type)) {
                    mUri.append(mTypes.get(i));
                    if (i < size - 1)
                        mUri.append("|");
                }
            }
        }
    }


    @Override
    protected String doInBackground(Void... urls) {
        //String email = emailText.getText().toString();
        // Do some validation here

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
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(String response) {
        //Log.d("Soar ", "response = " + response);
        List<GooglePlace> googlePlaces = GooglePlace.parseGooglePlaces(response);
        int size = googlePlaces.size();
        for (int i = 0; i < size; ++i) {
            Log.d("Soar ", "place name = " + googlePlaces.get(i).getName());
        }
    }

}
