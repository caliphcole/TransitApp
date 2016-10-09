package com.android.hermaeum.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.android.hermaeum.Data.FeedItem;
import com.android.hermaeum.R;
import com.gc.materialdesign.views.ButtonRectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Caliph Cole on 06/04/2015.
 */
public class NearbyFeedListAdapter  extends BaseAdapter implements Filterable{

    private Activity activity;
    private LayoutInflater inflater;
    private List<FeedItem> feedItems;
    private NearbyFilter filter;
    private List<FeedItem> originalfeedItems;

    private ButtonRectangle moreinfo;
    private ButtonRectangle nearbybusstop;


    public NearbyFeedListAdapter(Activity activity, List<FeedItem> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
        this.originalfeedItems = feedItems;


        getFilter();
    }







    @Override
    public int getCount() {
        return feedItems.size();
    }

    @Override
    public Object getItem(int i) {
        return feedItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.nearbygroup, null);

        TextView route = (TextView) convertView.findViewById(R.id.route);


        TextView currentLocation = (TextView) convertView.findViewById(R.id.currentlocation);
        TextView distance = (TextView) convertView.findViewById(R.id.distance);
        TextView timestamp = (TextView) convertView.findViewById(R.id.arrivaltime);

        FeedItem item = feedItems.get(i);

        route.setText(item.getRoute());


        String s = item.getDistance();
        distance.setText(s);
        timestamp.setText(item.getTimeStamp());
        currentLocation.setText(item.getCurrentlocation());

        return convertView;
    }



    @Override
    public Filter getFilter() {

        if (filter == null) {

            filter = new NearbyFilter();
        }

        return filter;
    }

    //Getter method that handles the
    public boolean getOriginalfeedItems() {

        feedItems = originalfeedItems;
        notifyDataSetChanged();
        return true;
    }

    public int getOriginalListCount() {
        return originalfeedItems.size();
    }

    private class NearbyFilter extends Filter {

        /*
        Inner class that handles the filtering of the listview
         */

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            List<FeedItem> filteredItems = new ArrayList<FeedItem>();


            if (constraint != null && constraint.toString().length() > 0) {

                if (originalfeedItems != null && originalfeedItems.size() > 0) {

                    for (FeedItem a : originalfeedItems) {

                        Log.d("routetest", a.getRoute());
                        if (a.getRoute().toLowerCase().contains(constraint.toString())) {
                            Log.d("Tag1", constraint.toString());
                            filteredItems.add(a);
                        } else if (a.getOrigin().toLowerCase().contains(constraint.toString())) {
                            Log.d("Tag2", a.getOrigin());
                            filteredItems.add(a);
                        } else if (a.getDestination().toLowerCase().contains(constraint.toString())) {
                            Log.d("Tag3", constraint.toString());
                            filteredItems.add(a);
                        } else if (a.getVia().toLowerCase().contains(constraint.toString())) {
                            Log.d("Tag4", constraint.toString());
                            filteredItems.add(a);
                        } else if (a.getRoutetype().toLowerCase().contains(constraint.toString())) {
                            filteredItems.add(a);
                        }


                    }
                    results.values = filteredItems;
                }

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            try {
                feedItems = (List<FeedItem>) results.values;
                notifyDataSetChanged();
            } catch (Exception e) {
                feedItems = originalfeedItems;
                notifyDataSetChanged();
            }
        }
    }
}