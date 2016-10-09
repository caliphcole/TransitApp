package com.android.hermaeum.Data;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Caliph Cole on 03/10/2015.
 */
public class FeedItem {

    private String route;
    private String origin;
    private String via;
    private String destination;
    private String routetype;

    private String latitude;
    private String longitude;
    private String velocity;

    private String distance;

    private String timeStamp;
    private String busStopName;
    private Location busstopcord;



    private String currentlocation;

    public FeedItem(String route, String origin, String via, String destination, String routetype, String timeStamp, String distance){

        super();
        this.route = route;
        this.origin = origin;
        this.via = via;
        this.destination = destination;
        this.routetype=routetype;
        this.timeStamp = timeStamp;
        this.distance = distance;

    }
    public FeedItem(){

    }



    public String getBusStopName(){

        return busStopName;
    }

    public void setBusStopName(String busStopName){

        this.busStopName = busStopName;
    }

    public void setBusstopcord(Location busstopcord){
        this.busstopcord = busstopcord;
    }

    public Location getBusstopcord(){
        return busstopcord;
    }

    public String getVelocity() {
        return velocity;
    }

    public void setVelocity(String velocity) {
        this.velocity = velocity;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setRouteType(String routetype){
        this.routetype=routetype;
    }
    public String getRoutetype(){
        return routetype;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }



    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }



    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }



    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
    public String getCurrentlocation() {
        return currentlocation;
    }

    public void setCurrentlocation(String currentlocation) {
        this.currentlocation = currentlocation;
    }



}
