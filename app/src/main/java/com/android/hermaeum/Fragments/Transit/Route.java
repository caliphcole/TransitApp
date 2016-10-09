package com.android.hermaeum.Fragments.Transit;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.hermaeum.Adapter.RouteStaticFeedListAdapter;
import com.android.hermaeum.Data.DBHelper;
import com.android.hermaeum.Data.FeedItem;
import com.android.hermaeum.Main;
import com.android.hermaeum.R;
import com.android.hermaeum.app.AppController;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Caliph Cole on 06/02/2015.
 */
public class Route extends Fragment implements AdapterView.OnItemClickListener {

    public static final String ARG_STRING = "Bus Route";

    private View rootView;
    private ListView listView;
    private RouteStaticFeedListAdapter listAdapter;

    private List<FeedItem> feedItems;
    private DBHelper routedb;

    //========================
    private ViewFlipper mViewFlipper;
    private Integer[] ads = {R.drawable.adone,R.drawable.adtwo};
    private GestureDetector mGestureDetector;

    //============
    private MaterialDialog materialDialog;
    private  FloatingActionButton helpbutton;
    private Main main;

    //URL for the static route data
    private String URL_FEED = "http://jatransit.appspot.com/routes";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.listview, container, false);

        listView = (ListView) rootView.findViewById(R.id.listView);



        feedItems = new ArrayList<FeedItem>();

        listAdapter = new RouteStaticFeedListAdapter(getActivity(), feedItems);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);



        helpbutton = (FloatingActionButton)rootView.findViewById(R.id.help);

        helpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog = new MaterialDialog(getActivity()).setTitle("Help Menu").setMessage("Swipe down to refresh\nclick list item for static timetable")
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

        return rootView;
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
                DBHelper db = new DBHelper(getActivity());
                if(!db.checkIfTableExist("routes")&& db.tableRowCount("routes")>0) {
                    List<FeedItem> temp = db.getAllRoutesCheck();// get all the routes from the table

                    // this for loop converts the database value in to a json array
                    for (int i = 0; i < temp.size(); i++) {

                        FeedItem item = new FeedItem();
                        item.setRoute(temp.get(i).getRoute());
                        item.setOrigin(temp.get(i).getOrigin());
                        item.setDestination(temp.get(i).getDestination());
                        item.setVia(temp.get(i).getVia());
                        item.setRouteType(temp.get(i).getRoutetype());

                        feedItems.add(0, item);
                        listAdapter.notifyDataSetChanged();
                    }

                }else {
                    materialDialog = new MaterialDialog(getActivity()).setTitle("Internet Issues").setMessage("No internet connection or Server down")
                            .setPositiveButton("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    materialDialog.dismiss();
                                    main = main.newInstance(1, main.ARG_STRING);
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

            }
        });

        //Adding request to volley request queue
       AppController.getInstance().addToRequestQueue(jsonReq);
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     */
    private void parseJsonFeed(JSONObject response) {
        try {
            //Store the json  feed in the json array
            JSONArray feedArray = response.getJSONArray("routes");

            //instantiate a temperary jsonarray to check if static feed as been modified
            JSONArray dataArray = new JSONArray();

            routedb = new DBHelper(getActivity());// instantiate the database variable

            if(routedb.checkIfTableExist("routes") || routedb.tableRowCount("routes")<1){
                routedb.createRouteTable();
                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);

                    routedb.insertRoute(feedObj.getString("route"), feedObj.getString("origin"), feedObj.getString("destination"), feedObj.getString("via"), feedObj.getString("route_type"));

                }

                List<FeedItem> temp = routedb.getAllRoutesCheck();// get all the routes from the table

                // this for loop converts the database value in to a json array
                for (int i = 0; i < temp.size(); i++) {

                    FeedItem item = new FeedItem();
                    item.setRoute(temp.get(i).getRoute());
                    item.setOrigin(temp.get(i).getOrigin());
                    item.setDestination(temp.get(i).getDestination());
                    item.setVia(temp.get(i).getVia());
                    item.setRouteType(temp.get(i).getRoutetype());

                    feedItems.add(0, item);
                    listAdapter.notifyDataSetChanged();
                }

            }else{
                List<FeedItem> temp = routedb.getAllRoutesCheck();// get all the routes from the table

                // this for loop converts the database value in to a json array
                for (int i = 0; i < temp.size(); i++) {

                    FeedItem item = new FeedItem();
                    item.setRoute(temp.get(i).getRoute());
                    item.setOrigin(temp.get(i).getOrigin());
                    item.setDestination(temp.get(i).getDestination());
                    item.setVia(temp.get(i).getVia());
                    item.setRouteType(temp.get(i).getRoutetype());

                    feedItems.add(0,item);
                    listAdapter.notifyDataSetChanged();
                }
            }



        } catch (Exception e) {

        }


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // instantiate the timetable fragment
        DBHelper db = new DBHelper(getActivity());
            FeedItem item = feedItems.get(i);

        if(!db.checkIfTableExist("weekday_"+item.getRoute())) {
            //Get data from the list item selected pass it to the nearbyinfo fragment
            Bundle bundle = new Bundle();
            bundle.putString("route", item.getRoute());
            bundle.putString("origin", item.getOrigin());
            bundle.putString("destination", item.getDestination());

            Schedule schedule = Schedule.newInstance(1, Schedule.ARG_STRING);
            schedule.setArguments(bundle);
            FragmentManager fm4 = getActivity().getSupportFragmentManager();
            FragmentTransaction ft4 = fm4.beginTransaction();

            if (schedule.isAdded()) {

                ft4.show(schedule);
            } else {

                ft4.replace(R.id.container, schedule, schedule.ARG_STRING);
            }

            ft4.addToBackStack(null);
            ft4.commit();
        }else{
            materialDialog = new MaterialDialog(getActivity()).setTitle("Info").setMessage("No Time Table Available")
                    .setPositiveButton("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            materialDialog.dismiss();

                        }
                    });
            materialDialog.show();
        }

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
    public void setActionBarTitle(String mTitle) {
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public static Route newInstance(int someInt, String s){

        Route rFragment = new Route();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        rFragment.setArguments(args);
        return rFragment;
    }
}


