package com.soar.googleplayservicetest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
    private WeakReference<GoogleApiClient> mGoogleApiClient = null;
    private WeakReference<Context> mContext = null;

    SearchNearbyPlaceAsyncTask(String location, String radius) {
        mUri = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=");
        mUri.append(location);
        mUri.append("&radius=");
        mUri.append(radius);
        // api key
        mUri.append("&key=AIzaSyA_XcexX-w6yAw5SVTWBenxNnePL_ac12Y");
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        mGoogleApiClient = new WeakReference(googleApiClient);
    }

    public void setContext(Context context) {
        mContext = new WeakReference<Context>(context);
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
        String[] placeId = new String[size];
        for (int i = 0; i < size; ++i) {
            Log.d("Soar ", "place name = " + googlePlaces.get(i).getName());
            placeId[i] = googlePlaces.get(i).getPlaceId();
        }

        if (mGoogleApiClient != null) {
            GoogleApiClient googleApiClient = mGoogleApiClient.get();
            PendingResult<PlaceBuffer> pendResult = Places.GeoDataApi.getPlaceById(googleApiClient, placeId);
            if (pendResult != null) {
                pendResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        ArrayList<PlaceWrapper> placeWrappers = new ArrayList<PlaceWrapper>();
                        for (Place place : places) {
                            if (place != null) {
                                PlaceWrapper placeWrapper = PlaceBuilder.getPlaceWrapper(place);
                                placeWrappers.add(placeWrapper);
                            }
                        }

                        Intent intent = new Intent();
                        intent.putParcelableArrayListExtra(PlaceListActivity.KEY_PLACE_LIST, placeWrappers);
                        if (mContext != null) {
                            Context context = mContext.get();
                            if (context != null) {
                                intent.setClass(context, PlaceListActivity.class);
                                context.startActivity(intent);
                            }
                        }
                    }
                });
            }
        }

    }

}
