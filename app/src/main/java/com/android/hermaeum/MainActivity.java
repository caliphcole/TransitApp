package com.android.hermaeum;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.hermaeum.Fragments.Transit.Map;
import com.android.hermaeum.app.NavigationDrawerFragment;
import com.android.hermaeum.Fragments.Transit.Nearby;
import com.android.hermaeum.Fragments.Transit.Route;
import com.android.hermaeum.Fragments.Transit.TripPlanner;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    //List of Fragments
   // private MainScreen mainscreen;

    private Main main;
    private Map map;
    private TripPlanner tripPlanner;
    private Route route;
    private Nearby nearby;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            main = Main.newInstance(0,main.ARG_STRING);
            FragmentManager fragmentManager0 = getSupportFragmentManager();
            fragmentManager0.beginTransaction()
                    .replace(R.id.container,main)
                    .commit();
        }


        mTitle = getTitle();



    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        switch (position) {

            case 0://main screen
                onSectionAttached(position);

                main = main.newInstance(position,main.ARG_STRING);
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                if(main.isAdded()){
                    ft.show(main);
                }else{
                    // fragment needs to be added to frame container
                    ft.replace(R.id.container, main, main.ARG_STRING);
                }
                ft.addToBackStack(null);
                ft.commit();

                break;
            case 1://nearbygroup
                onSectionAttached(position);

                nearby = nearby.newInstance(position, nearby.ARG_STRING);
                FragmentManager fm0 = getSupportFragmentManager();
                FragmentTransaction ft0 = fm0.beginTransaction();

                if (nearby.isAdded()) {
                    ft0.show(nearby);
                } else {
                    ft0.replace(R.id.container, nearby, nearby.ARG_STRING);
                }
                ft0.addToBackStack(null);
                ft0.commit();
                break;
            case 2://route
                onSectionAttached(position);

                route = route.newInstance(position, route.ARG_STRING);
                FragmentManager fm1 = getSupportFragmentManager();
                FragmentTransaction ft1 = fm1.beginTransaction();

                if (route.isAdded()) {
                    ft1.show(route);
                } else {
                    ft1.replace(R.id.container, route, route.ARG_STRING);
                }
                ft1.addToBackStack(null);
                ft1.commit();
                break;
            case 3://tripplanner
                onSectionAttached(position);
                tripPlanner = tripPlanner.newInstance(position, tripPlanner.ARG_STRING);
                FragmentManager fm2 = getSupportFragmentManager();
                FragmentTransaction ft2 = fm2.beginTransaction();


                if (tripPlanner.isAdded()) {
                    ft2.show(tripPlanner);
                } else {
                    ft2.replace(R.id.container, tripPlanner, tripPlanner.ARG_STRING);
                }
                ft2.addToBackStack(null);
                ft2.commit();

                break;
            case 4://map
                onSectionAttached(position);
                map = map.newInstance(position,map.ARG_STRING);
                FragmentManager fm4 = getSupportFragmentManager();
                FragmentTransaction ft4 = fm4.beginTransaction();

                if(map.isAdded()){
                    ft4.show(map);
                }else{
                    ft4.replace(R.id.container,map,map.ARG_STRING);

                }
                ft4.addToBackStack(null);
                ft4.commit();

                break;

        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.Home);
                break;
            case 1:
                mTitle = getString(R.string.Nearby);
                break;
            case 2:
                mTitle = getString(R.string.Route);
                break;
            case 3:
                mTitle = getString(R.string.TripPlanner);
                break;
            case 4:
                mTitle = getString(R.string.Map);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

            getMenuInflater().inflate(R.menu.menu_main, menu);

            menu.getItem(0).setVisible(false);

            restoreActionBar();


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
