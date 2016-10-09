package com.android.hermaeum.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.hermaeum.Data.TimeDataItem;
import com.android.hermaeum.R;

import java.util.List;

/**
 * Created by Caliph Cole on 06/11/2015.
 */
public class ScheduleAdapter extends BaseAdapter {


    private Activity activity;
    private LayoutInflater inflater;
    private List<TimeDataItem> feedItems;
    private String[][] childarray;
    private String via;

    public ScheduleAdapter(Activity actitity, List<TimeDataItem>feedItems){

        this.activity = actitity;
        this.feedItems = feedItems;


    }

    public void setVia(String via){

        this.via=via;

    }
    public String [][] getChildArray(){
        childarray = new  String[feedItems.get(0).getTime().split(",").length][feedItems.size()];
        for(int a = 0 ; a<feedItems.get(0).getTime().split(",").length;a++){

            for(int b= 0; b< feedItems.size();b++){

                childarray[a][b]=feedItems.get(b).getVia()+"-"+feedItems.get(b).getTime().split(",")[a];
                try {
                    Log.d("getchild", feedItems.get(b).getTime().split(",")[a]);
                }catch (Exception e){
                    Log.d("getchild", "null");
                }
            }


        }

        return childarray;
    }
    public String getVia(){
        return via;
    }


    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int i) {
        for(TimeDataItem ti: feedItems){
            if(ti.getVia().equals(getVia())){
                return ti.getTime().split(",");
            }

        }

        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null)
            view = inflater.inflate(R.layout.timeschedulelistlayout, null);

        TextView time = (TextView) view.findViewById(R.id.time);


        time.setText(feedItems.get(i).getTime());



        return view;
    }
}