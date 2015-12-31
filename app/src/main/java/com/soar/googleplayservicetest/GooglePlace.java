package com.soar.googleplayservicetest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2015/12/31.
 */
public class GooglePlace {
    private String mName = null;
    private List<String> mTypes = null;
    private String mPlaceId = null;

    public void setName(String name) {
        mName = name;
    }

    public void addType(String type) {

    }

    public void setPlaceId(String placeId) {
        mPlaceId = placeId;
    }


    public String getName() {
        return mName;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public static List<GooglePlace> parseGooglePlaces(String response) {
        ArrayList temp = new ArrayList();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("results")) {
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                final int arraySize = jsonArray.length();
                for (int i = 0; i < arraySize; i++) {
                    GooglePlace place = new GooglePlace();
                    if (jsonArray.getJSONObject(i).has("name")) {
                        place.setName(jsonArray.getJSONObject(i).optString("name"));
                    }

                    if (jsonArray.getJSONObject(i).has("place_id")) {
                        place.setPlaceId(jsonArray.getJSONObject(i).optString("place_id"));
                    }
                    temp.add(place);
                }
            }
        } catch (Exception ex) {

        }

        return temp;

    }
}
