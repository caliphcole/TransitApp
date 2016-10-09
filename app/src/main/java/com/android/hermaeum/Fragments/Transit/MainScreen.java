package com.android.hermaeum.Fragments.Transit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.hermaeum.Adapter.MainScreenIconAdapter;
import com.android.hermaeum.MainActivity;
import com.android.hermaeum.R;

/**
 * Created by Caliph Cole on 06/02/2015.
 */
public class MainScreen extends Fragment{

    public static final String ARG_STRING = "HOME";

    private View rootView;

    private MainActivity mainActivity;

    //The different options on main screen variables
    private Nearby nearby;
    private Route route;
    private TripPlanner tripPlanner;
    private Map map;






    private CharSequence mTitle;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.mainscreen, container,
                false);
        mainActivity = new MainActivity();
        setHasOptionsMenu(true);// use to enable the fragment have access to actionbar menu




        ((GridView) rootView.findViewById(R.id.mainscreenicon))
                .setAdapter(new MainScreenIconAdapter(getActivity()));




        gridViewListener();




        return rootView;
    }



    /**
     * Handles the position selection on the homeScreen
     */
    public void gridViewListener() {

        ((GridView) rootView.findViewById(R.id.mainscreenicon))
                .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {


                        switch (position) {

                            case 0:
                                //nearbygroup
                                onSectionAttached(position);

                                nearby = nearby.newInstance(position, nearby.ARG_STRING);
                                FragmentManager fm0 = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft0 = fm0.beginTransaction();

                                if (nearby.isAdded()) {
                                    ft0.show(nearby);
                                } else {
                                    ft0.replace(R.id.container, nearby, nearby.ARG_STRING);
                                }
                                ft0.addToBackStack(null);
                                ft0.commit();

                                break;


                            case 1:
                                //route
                                onSectionAttached(position);
                                route = route.newInstance(position, route.ARG_STRING);
                                FragmentManager fm1 = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft1 = fm1.beginTransaction();

                                if (route.isAdded()) {
                                    ft1.show(route);
                                } else {
                                    ft1.replace(R.id.container, route, route.ARG_STRING);
                                }
                                ft1.addToBackStack(null);
                                ft1.commit();
                                break;

                            case 2://tripplanner
                                onSectionAttached(position);
                                tripPlanner = tripPlanner.newInstance(position, tripPlanner.ARG_STRING);
                                FragmentManager fm2 = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft2 = fm2.beginTransaction();

                                if (tripPlanner.isAdded()) {
                                    ft2.show(tripPlanner);
                                } else {
                                    ft2.replace(R.id.container, tripPlanner, tripPlanner.ARG_STRING);
                                }
                                ft2.addToBackStack(null);
                                ft2.commit();

                                break;
                            case 3://map
                                onSectionAttached(position);
                                map = map.newInstance(position, map.ARG_STRING);
                                FragmentManager fm4 = getActivity().getSupportFragmentManager();
                                FragmentTransaction ft4 = fm4.beginTransaction();

                                if (map.isAdded()) {
                                    ft4.show(map);
                                } else {
                                    ft4.replace(R.id.container, map, map.ARG_STRING);

                                }
                                ft4.addToBackStack(null);
                                ft4.commit();

                                break;



                        }

                    }
                });



    }


    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.Nearby);
                break;
            case 1:
                mTitle = getString(R.string.Route);
                break;
            case 2:
                mTitle = getString(R.string.TripPlanner);
                break;
            case 3:
                mTitle = getString(R.string.Map);
                break;

        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {


    }

    private class DownloadRouteTask extends AsyncTask<Bundle,Integer,Void>{

        @Override
        protected Void doInBackground(Bundle... bundles) {



            return null;
        }
    }


    public static MainScreen newInstance(int someInt, String someTitle) {
        MainScreen mfragment = new MainScreen();
        Bundle args = new Bundle();
        args.putInt(someTitle, someInt);
        mfragment.setArguments(args);
        return mfragment;
    }
}
