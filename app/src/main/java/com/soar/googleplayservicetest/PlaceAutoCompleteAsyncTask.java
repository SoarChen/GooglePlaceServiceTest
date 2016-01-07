package com.soar.googleplayservicetest;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by user on 2016/1/6.
 */
public class PlaceAutoCompleteAsyncTask extends AsyncTask<Void, Void, String> {
    private StringBuilder mUri = null;
    private String mInput = null;
    private String mLocation = null;
    private String mRadius = null;
    private GooglePlaceWebAPI.AutocompleteListener mAutocompleteListener = null;

    public PlaceAutoCompleteAsyncTask(String input) {
        mUri = new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyA_XcexX-w6yAw5SVTWBenxNnePL_ac12Y&input=");
        try {
            mUri.append(URLEncoder.encode(input, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mLocation = "25.04,121.55";
        if (!TextUtils.isEmpty(mLocation)) {
            mUri.append("&location=");
            mUri.append(mLocation);
        }
        mRadius = "500";
        if (!TextUtils.isEmpty(mRadius)) {
            mUri.append("&radius=");
            mUri.append(mRadius);
        }
    }


    public void setAutocompleteListener(GooglePlaceWebAPI.AutocompleteListener autocompleteListener) {
        mAutocompleteListener = autocompleteListener;
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
        super.onPostExecute(s);
        List<GoogleAutocomplete> googleAutocompleteList = GoogleAutocomplete.parseGoogleAutocomplete(s);
        if (mAutocompleteListener != null) {
            mAutocompleteListener.onResponse(googleAutocompleteList);
        }
    }



}
