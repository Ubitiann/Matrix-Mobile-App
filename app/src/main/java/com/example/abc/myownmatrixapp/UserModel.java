package com.example.abc.myownmatrixapp;

public class UserModel {

    private double mLatitude;
    private double mLongitude;


    public UserModel(){

    }

    public UserModel(double mLatitude, double mLongitude) {
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;

    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }


}
