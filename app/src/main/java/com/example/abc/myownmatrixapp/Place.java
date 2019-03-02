package com.example.abc.myownmatrixapp;

import com.google.android.gms.location.places.PlaceBuffer;

public class Place {
    private String placeId;
    private String placeText;

    public Place( String placeText) {

        this.placeText = placeText;
    }
    public Place (){

    }
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceText() {
        return placeText;
    }

    public void setPlaceText(String placeText) {
        this.placeText = placeText;
    }
    public String toString(){
        return placeText;
    }
}
