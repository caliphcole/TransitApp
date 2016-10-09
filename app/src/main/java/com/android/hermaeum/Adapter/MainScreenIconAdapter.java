package com.android.hermaeum.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.hermaeum.R;

/**
 * Created by Caliph Cole on 06/02/2015.
 */
public class MainScreenIconAdapter extends BaseAdapter {

    private Context mContext;

    // icon images in array
    private Integer[] iconimgs = {R.drawable.nearby,R.drawable.route,R.drawable.trip,R.drawable.map} ;//resource link
    private String[] iconnames = {"Nearby","Bus Routes","Trip","Map"};

    /**
     * MainSceenIconAdapter takes one parameter which is the context of the activity
     * @param c
     */
    public MainScreenIconAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return iconimgs.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = ( LayoutInflater ) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.customgridview, null);
        rowView.setPadding(40, 0, 40, 0);

        holder.tv = (TextView)rowView.findViewById(R.id.iconlabel);
        holder.img = (ImageView)rowView.findViewById(R.id.mainscreenicon);

        holder.tv.setText(iconnames[i]);
        holder.img.setImageResource(iconimgs[i]);


        return rowView;

    }

    /**
     * Holder class
     */
    private class Holder
    {
        TextView tv;
        ImageView img;
    }
}
