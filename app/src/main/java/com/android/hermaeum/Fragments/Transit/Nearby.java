package com.android.hermaeum.Fragments.Transit;

import android.app.SearchManager;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.hermaeum.Adapter.NearbyFeedListAdapter;
import com.android.hermaeum.Data.DBHelper;
import com.android.hermaeum.Data.FeedItem;
import com.android.hermaeum.Main;
import com.android.hermaeum.R;
import com.android.hermaeum.app.AppController;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Caliph Cole on 06/02/2015.
 */
public class Nearby extends Fragment implements AdapterView.OnItemClickListener{

    public static final String ARG_STRING = "Nearby";

    //Refresh variables
    private static int REFRESH_TIME_IN_SECONDS = 5;
    private SwipeRefreshLayout swipeRefreshLayout;

    public View rootView;
    private ListView listview;
    private NearbyFeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private DBHelper db;

    private Main main;



    String[] busstopfeed = {"TestRoute", "18.025782,-76.853840", "18.025532/-76.854312",
            "18.025891/-76.853591",
            "18.026044/-76.847950",
            "18.026133/-76.840930",
            "18.026156/-76.839638",
            "18.025567/-76.833372",
            "18.025393/-76.821914"};
    static int position;
    private MaterialDialog materialDialog;
    private  FloatingActionButton helpbutton;

    private int pos;

    private String URL_FEED = "http://jatransit.appspot.com/live";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.nearbylistview, container, false);

        listview = (ListView) rootView.findViewById(R.id.listView);


        feedItems = new ArrayList<FeedItem>();
        listAdapter = new NearbyFeedListAdapter(getActivity(), feedItems);
        listview.setAdapter(listAdapter);

        listview.setOnItemClickListener(this);

        helpbutton = (FloatingActionButton)rootView.findViewById(R.id.help);

            helpbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    materialDialog = new MaterialDialog(getActivity()).setTitle("Help Menu").setMessage("Swipe down to refresh\nclick list item for more info")
                            .setPositiveButton("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    materialDialog.dismiss();
                                }
                            });
                    materialDialog.show();
                }
            });

        update();
        refresh();

        return rootView;
    }


    /**
     * The refresh function
     */
    private void refresh() {

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.container);

        swipeRefreshLayout.setEnabled(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                feedItems = new ArrayList<FeedItem>();

                //check if feed as changed before updating
                update();

                listAdapter = new NearbyFeedListAdapter(getActivity(), feedItems);
                listview.setAdapter(listAdapter);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopSwipeRefresh();


                    }
                }, REFRESH_TIME_IN_SECONDS * 1000);
            }

            private void stopSwipeRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }

        });

        swipeRefreshLayout.setColorScheme(R.color.black,
                R.color.red, R.color.green,
                R.color.green);

    }



    public void update() {


        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                URL_FEED, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    parseJsonFeed(response);
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                materialDialog = new MaterialDialog(getActivity()).setTitle("Internet Issues").setMessage("No internet connection or Server down")
                        .setPositiveButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                materialDialog.dismiss();
                                main = main.newInstance(position, main.ARG_STRING);
                                FragmentManager fm = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();

                                if (main.isAdded()) {
                                    ft.show(main);
                                } else {
                                    // fragment needs to be added to frame container
                                    ft.replace(R.id.container, main, main.ARG_STRING);
                                }
                                ft.commit();

                            }
                        });
                materialDialog.show();
            }
        });
        jsonReq.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        Log.d("timeout", "" + jsonReq.getTimeoutMs());
        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);

    }

    //Retrieve the data from teh json feed
    private void parseJsonFeed(JSONObject response)throws NullPointerException{

        try {
            JSONArray feedArray = response.getJSONArray("trackedBus");



            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                FeedItem item = new FeedItem();
                item.setRoute( feedObj.getString("route_id"));

                item.setOrigin(feedObj.getString("origin"));
                item.setVia(feedObj.getString("via"));
                item.setDestination( feedObj.getString("destination"));


                item.setVelocity(feedObj.getString("velocity"));
                item.setLongitude(feedObj.getString("long"));
                item.setLongitude(feedObj.getString("lat"));

                //Calculate the currentlocation, distance and timestamp

                //Getting the bus location on the map from the long and lat provided from the feed
                Location buslocation = new Location("");
                buslocation.setLatitude(Double.parseDouble(feedObj.getString("lat")));
                buslocation.setLongitude(Double.parseDouble(feedObj.getString("long")));


                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    // Fall back to coarse location.
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                //Calculate the distance between the bus and the newest bus stop on the route
                //should have the feed for the bus stops on the route
                /**
                 * algorithm to calculate this
                 * get user location
                 * use user location to get the closest bus stop on that particular route
                 * when this nearest bus stop is found
                 * calculate the distance between coordinate up to the closest coordinate on the route to that bus stop
                 *
                 * pseudocode
                 *
                 * argumment - location, location
                 * arraylist list = new arraylist();
                 * function calculatedistance(pointonroute, bustoplist){
                 * for(int i=0;i < bustoplist.length,i++){
                 *     distance = busstoplist[i].distanceto(pointroute)
                 *     if(distance <threshold){
                 *
                 *          list.add(busstoplist[i])
                 *          }
                 *     }
                 *     elimatereplication(list)
                 *
                 *  }
                 *
                 *  function getbusstoponroute(listofpointonroute, busstoplist){
                 *
                 *
                 *      for(int i =0; i<listofpointonroute ;i++){
                 *
                 *              calculatedistance(listofpointonroute[i],busstoplist);
                 *         }
                 *     }
                 *
                 */


            //wrong calculate this calculation check the distance to the users location and not the distance to the bus stop============================================================
                double  c = location.distanceTo(buslocation);

                item.setDistance((int) (c / 1000) +" km");

                //Log.d("distance", "" + c);
                double time = c/(Double.parseDouble(feedObj.getString("velocity"))*0.277778);

                item.setTimeStamp((int) time / 60 + "min");

                //=======================================================================
                item.setCurrentlocation(getAddress(Double.parseDouble(feedObj.getString("lat")), Double.parseDouble(feedObj.getString("long"))));

               // Log.d("Current Location",item.getCurrentlocation());

                //if((int)Integer.parseInt(item.getDestination().split(" ")[0])<10)
                        //feedItems.add(0, item);




           /* //This is the code that will be used with the feed
            ArrayList<Double> temp = new ArrayList<>();
            FeedItem item = new FeedItem();
            Location busstoplocation = new Location("");
            LocationManager locationManager;
            Location location;
            for(int i = 0 ; i<busstopfeed.length;i++){

                switch(i){
                    case 0:

                        item.setRoute(busstopfeed[i]);
                        item.setDestination("papine");
                        item.setOrigin("duhaney park");
                        break;
                    case 1:
                        Location buslocation = new Location("");
                        buslocation.setLatitude(Double.parseDouble(busstopfeed[i].split(",")[0]));
                        buslocation.setLongitude(Double.parseDouble(busstopfeed[i].split(",")[1]));

                        item.setCurrentlocation(getAddress(Double.parseDouble(busstopfeed[i].split(",")[0]), Double.parseDouble(busstopfeed[i].split(",")[1])));
                        break;
                    case 8:

                        // get location of bus
                        busstoplocation.setLatitude(Double.parseDouble(busstopfeed[i].split("/")[0]));
                        busstoplocation.setLongitude(Double.parseDouble(busstopfeed[i].split("/")[1]));

                        //get location of user
                        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location == null) {
                            // Fall back to coarse location.
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }

                        //calculate the distance between bus stop and user and store distances in a arraylist
                        double c=busstoplocation.distanceTo(location);
                        temp.add(c);

                        Log.d("temp item",""+temp.get(0));
                        Log.d("temp item",""+temp.get(1));
                        Log.d("temp item",""+temp.get(2));
                        Log.d("temp item",""+temp.get(3));
                        Log.d("temp item",""+temp.get(4));
                        Log.d("temp item",""+temp.get(5));
                        buslocation = new Location("");
                        buslocation.setLatitude(Double.parseDouble(busstopfeed[1].split(",")[0]));
                        buslocation.setLongitude(Double.parseDouble(busstopfeed[1].split(",")[1]));

                        position = getPosition(temp);

                        Log.d("position",""+position);

                        busstoplocation.setLatitude(Double.parseDouble(busstopfeed[position].split("/")[0]));
                        busstoplocation.setLongitude(Double.parseDouble(busstopfeed[position].split("/")[1]));

                        item.setDistance("" + (int) (buslocation.distanceTo(busstoplocation)) / 1000);

                        double time = buslocation.distanceTo(busstoplocation)/60;

                        item.setTimeStamp((int) time / 60 + "min");
                        break;

                    default:
                        Log.d("position in array",""+i);

                        // get location of bus
                        busstoplocation.setLatitude(Double.parseDouble(busstopfeed[i].split("/")[0]));
                        busstoplocation.setLongitude(Double.parseDouble(busstopfeed[i].split("/")[1]));

                        //get location of user
                        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location == null) {
                            // Fall back to coarse location.
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }

                        //calculate the distance between bus stop and user and store distances in a arraylist
                        c=busstoplocation.distanceTo(location);
                        temp.add(c);
                        //Log.d("temp item",c+"");


                        break;

                }
            }*/

                DBHelper db = new DBHelper(getActivity());
                item = db.getBusStopData(item, location);
            feedItems.add(0, item);

        }
            //Collections.sort(feedItems, new liveFeedComparator());

            listAdapter.notifyDataSetChanged();
            rootView.findViewById(R.id.loadingfeedbar).setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    // private helper function
    private String getAddress(double lat, double lng) {

        String add ="";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            add = obj.getFeatureName();

            Log.v("IGA", "Address" + add);
            return add;
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return add;
    }



   /* private static class liveFeedComparator implements Comparator<FeedItem> {

        @Override
        public int compare(FeedItem lhs,
                           FeedItem rhs) {
            if(Integer.parseInt(lhs.getDistance().replaceAll("[^0-9?!\\.]", ""))<Integer.parseInt(rhs.getDistance().replaceAll("[^0-9?!\\.]", "")))
                return -1;
            else if(Integer.parseInt(lhs.getDistance().replaceAll("[^0-9?!\\.]",""))>Integer.parseInt(rhs.getDistance().replaceAll("[^0-9?!\\.]","")))
                return 1;
            else
                return 0;
        }

    }*/



    // getting the miniumum value
    public static int getPosition(ArrayList<Double> array){
        position = 0;
        double minValue = array.get(0);
        for(int i=1;i<array.size();i++){
            if(array.get(i) < minValue){
                minValue = array.get(i);
                position = i;
            }
        }
        return position +2;
    }

    public void setActionBarTitle(String mTitle) {
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(true);
        // menu.getItem(1).setVisible(false);
        // menu.getItem(3).setVisible(false);
        setActionBarTitle(ARG_STRING);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));

        searchView.setQueryHint("Enter Route#, Origin, Destination, Via or Type");
        searchView.setOnCloseListener(new SearchView.OnCloseListener(){

            @Override
            public boolean onClose() {
                listAdapter.getOriginalfeedItems();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {return true;}

            @Override
            public boolean onQueryTextChange(String newText) {
                if(listAdapter.getOriginalListCount()>0){
                    listAdapter.getFilter().filter(newText);
                }

                return true;
            }


        });

    }

    public static Nearby newInstance(int someInt, String s) {

        Nearby nfragment = new Nearby();
        Bundle args = new Bundle();
        args.putInt("Nearby", someInt);
        nfragment.setArguments(args);
        return nfragment;
    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        position = i;
        FeedItem item = feedItems.get(position);
        //Get data from the list item selected pass it to the nearbyinfo fragment
        Bundle bundle = new Bundle();
        bundle.putString("route", item.getRoute());
        bundle.putString("origin", item.getOrigin());
        bundle.putString("destination",item.getDestination());
        bundle.putString("currentlocation", item.getCurrentlocation());
        bundle.putString("distance",item.getDistance());
        bundle.putString("time",item.getTimeStamp());
        bundle.putString("lat",""+item.getBusstopcord().getLatitude());
        bundle.putString("long",""+item.getBusstopcord().getLongitude());

        //setup transaction of the nearbygroup info fragment

        NearbyInfo info = NearbyInfo.newInstance(1, NearbyInfo.ARG_STRING);
        info.setArguments(bundle);
        FragmentManager fm4 = getActivity().getSupportFragmentManager();
        FragmentTransaction ft4 =  fm4.beginTransaction();

        if (info.isAdded()){

            ft4.show(info);
        } else {

            ft4.replace(R.id.container, info, info.ARG_STRING);
        }

        ft4.addToBackStack(null);
        ft4.commit();

    }
}
