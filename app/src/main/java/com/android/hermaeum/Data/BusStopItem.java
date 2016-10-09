package com.android.hermaeum.Data;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Caliph Cole on 07/15/2015.
 */
public class BusStopItem {

    private String busStopName;
    private LatLng busstopcord;


    public String getBusStopName(){

        return busStopName;
    }

    public void setBusStopName(String busStopName){

        this.busStopName = busStopName;
    }

    public void setBusstopcord(LatLng busstopcord){
        this.busstopcord = busstopcord;
    }

    public LatLng getBusstopcord(){
        return busstopcord;
    }
}
