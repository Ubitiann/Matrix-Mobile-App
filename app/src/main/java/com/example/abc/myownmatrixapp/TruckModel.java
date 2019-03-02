package com.example.abc.myownmatrixapp;

import com.google.android.gms.maps.model.LatLng;

public class TruckModel {

    private String mUserId;
    private LatLng latLng;
    private double mLatitude;
    private double mLongitude;

    public TruckModel(String mUserId, double mLatitude, double mLongitude) {
        this.mUserId = mUserId;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        LatLng latLng=new LatLng( mLatitude,mLongitude );
        this.latLng=latLng;

    }

    public TruckModel() {
    }

    public void formLatlng(){
        LatLng latLng=new LatLng( mLatitude,mLongitude );
        this.latLng=latLng;
    }

    public TruckModel(LatLng latLng, String mUserId) {
        this.latLng = latLng;
        this.mUserId=mUserId;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }
}
