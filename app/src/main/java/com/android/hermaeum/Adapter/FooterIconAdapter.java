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
 * Created by Caliph Cole on 07/04/2015.
 */
public class FooterIconAdapter extends BaseAdapter {

    private Context mContext;

    // icon images in array
    private Integer[] iconimgs = {R.drawable.faq,R.drawable.terms,R.drawable.help} ;//resource link
    private String[] iconnames = {"FAQ","About","Help"};

    /**
     * MainSceenIconAdapter takes one parameter which is the context of the activity
     * @param c
     */
    public FooterIconAdapter(Context c) {
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


        rowView = inflater.inflate(R.layout.customfootergridview, null);
        rowView.setPadding(40, 40, 40, 40);
        holder.tv = (TextView)rowView.findViewById(R.id.iconlabel);
        holder.img = (ImageView)rowView.findViewById(R.id.mainscreenicon);

        holder.tv.setTextColor(mContext.getResources().getColor(R.color.white));
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
