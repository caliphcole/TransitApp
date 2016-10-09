package com.android.hermaeum.Data;

/**
 * Created by Caliph Cole on 07/01/2015.
 */
public class TimeDataItem {


    private String time;
    private String via;
    private String nexttimes ="";

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public void setNexttimes(String name){

            if(nexttimes.equals("")) {
                this.nexttimes += name;
            }else {
                this.nexttimes = this.nexttimes +"\n"+ name;
            }


    }
    public String getNexttimes(){

        return nexttimes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }




}
