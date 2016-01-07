package com.soar.googleplayservicetest;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/1/6.
 */
public class GoogleAutocomplete {

    private String mDescription = null;
    private String mPlaceId = null;
    private ArrayList<Pair<Integer, Integer>> mMatchedStrings = null;

    public String getDescription() {
        return mDescription;
    }

    public CharSequence getDescription(CharacterStyle characterStyle) {
        SpannableStringBuilder span = new SpannableStringBuilder(mDescription);
        final int matchedStringsSize = mMatchedStrings.size();
        for (int i = 0; i < matchedStringsSize; ++i) {
            Pair<Integer, Integer> matchedString = mMatchedStrings.get(i);
            if (matchedString != null) {
                span.setSpan(characterStyle, matchedString.second, matchedString.second + matchedString.first, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return span;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(String placeId) {
        mPlaceId = placeId;
    }

    public void setMatcheStrings(ArrayList matchedStrings) {
        mMatchedStrings = matchedStrings;
    }

    public static List<GoogleAutocomplete> parseGoogleAutocomplete(String response) {
        ArrayList temp = new ArrayList();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.has("predictions")) {
                JSONArray jsonArray = jsonObject.getJSONArray("predictions");
                final int arraySize = jsonArray.length();
                for (int i = 0; i < arraySize; i++) {
                    GoogleAutocomplete autocomplete = new GoogleAutocomplete();
                    if (jsonArray.getJSONObject(i).has("description")) {
                        autocomplete.setDescription(jsonArray.getJSONObject(i).optString("description"));
                    }

                    if (jsonArray.getJSONObject(i).has("place_id")) {
                        autocomplete.setPlaceId(jsonArray.getJSONObject(i).optString("place_id"));
                    }

                    if (jsonArray.getJSONObject(i).has("matched_substrings")) {
                        JSONArray matchedSubstrings = jsonArray.getJSONObject(i).getJSONArray("matched_substrings");
                        final int matchedSubstringsSize = matchedSubstrings.length();
                        ArrayList<Pair<Integer, Integer>> matchedStrings = new ArrayList<>();
                        for (int j = 0; j < matchedSubstringsSize; ++j) {
                            if (matchedSubstrings.getJSONObject(j).has("length") && matchedSubstrings.getJSONObject(j).has("offset")) {
                                int length = matchedSubstrings.getJSONObject(j).getInt("length");
                                int offset = matchedSubstrings.getJSONObject(j).getInt("offset");
                                matchedStrings.add(new Pair<Integer, Integer>(length, offset));
                            }
                        }
                        autocomplete.setMatcheStrings(matchedStrings);
                    }

                    temp.add(autocomplete);
                }
            }
        } catch (Exception ex) {

        }

        return temp;

    }
}
