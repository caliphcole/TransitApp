package com.android.hermaeum.Data;

/**
 * Created by Caliph Cole on 06/26/2015.
 */
public class TripDataItem {

    private String routeVia;
    private String contVia;
    private String routes;
    private int count;


    public String getRouteVia() {
        return this.routeVia;
    }
    public void setRouteVia(String routeOneVia){
            this.routeVia = routeOneVia;
    }

    public String getRoutes(){
        return routes;
    }
    public void setRoutes(String route){

            this.routes = route;
    }

    public String getContVia(){
        return contVia;
    }

    public void setContVia(String contVia){
        this.contVia=contVia;
    }
    public  int getCount(){
        return count;
    }
    public void setCount(int count){

        this.count = count;
    }
    @Override
    public String toString() {
        return getRoutes();
    }

    @Override
    public boolean equals(Object o) {

        return this.getRoutes().equals(((TripDataItem)o).getRoutes());
    }

    public int hashCode() {
        return getRoutes().hashCode();
    }
}
