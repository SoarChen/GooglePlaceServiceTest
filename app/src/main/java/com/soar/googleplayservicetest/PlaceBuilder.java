package com.soar.googleplayservicetest;

import com.google.android.gms.location.places.Place;

/**
 * Created by user on 2016/1/4.
 */
public class PlaceBuilder {
    public static PlaceWrapper getPlaceWrapper(Place place) {
        PlaceWrapper placeWrapper = new PlaceWrapper();
        placeWrapper.setName(place.getName().toString());
        placeWrapper.setAddress(place.getAddress().toString());
        placeWrapper.setPhoneNumber(place.getPhoneNumber().toString());
        return placeWrapper;
    }
}
