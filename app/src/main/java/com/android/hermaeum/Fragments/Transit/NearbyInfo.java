package com.android.hermaeum.Fragments.Transit;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.hermaeum.R;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Caliph Cole on 06/05/2015.
 */
public class NearbyInfo extends Fragment implements OnMapReadyCallback {

    public static final String ARG_STRING = "Nearby bus details..";

    private static View rootView;

    private String origin;
    private String destine;
    private String route;
    private String location;
    private String distance;
    private String time;
    private String lat, longi;
    private SupportMapFragment mapFragment;

    private TextView distanceview, timestampview, routeview, originview, destinationview, locationview;
    private ButtonRectangle nearestBusStop;

    private MaterialDialog materialDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);


        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {

            rootView = inflater.inflate(R.layout.nearbyinfo, container, false);


        }catch (InflateException e) {
    /* map is already there, just return view as it is */
        }

        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

       nearestBusStop = (ButtonRectangle) rootView.findViewById(R.id.nearbusstop);
        nearestBusStop.setTextColor(getResources().getColor(R.color.green));

        nearestBusStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog.show();
            }
        });
        materialDialog = new MaterialDialog(getActivity()).setTitle("Navigate to Street View").setMessage("This intent will open google maps application, to navigate back into SmartTransit press the back button 5 times.")
                .setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDialog.dismiss();
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + longi);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                }).setNegativeButton("CANCEL", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDialog.dismiss();
                    }
                });

        //Retrieving data from nearbygroup route
        Bundle bundle = getArguments();

        origin = bundle.getString("origin");
        destine = bundle.getString("destination");
        route = bundle.getString("route");
        location = bundle.getString("currentlocation");
        distance = bundle.getString("distance");
        time = bundle.getString("time");
        lat = bundle.getString("lat");
        longi = bundle.getString("long");

        busDetails(origin, destine, route, location, distance, time);


        return rootView;
    }

    private void busDetails(String origin, String destine, String route, String location, String distance, String time) {


        originview = (TextView) rootView.findViewById(R.id.org);
        originview.setText(origin);


        destinationview = (TextView) rootView.findViewById(R.id.des);
        destinationview.setText(destine);


        locationview = (TextView) rootView.findViewById(R.id.location);
        locationview.setText(location);


        distanceview = (TextView) rootView.findViewById(R.id.distance);
        distanceview.setText(distance);


        timestampview = (TextView) rootView.findViewById(R.id.approxtime);
        timestampview.setText(time);


        routeview = (TextView) rootView.findViewById(R.id.route);
        routeview.setText(route);




    }

    public void setActionBarTitle(String mTitle) {
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        setActionBarTitle(ARG_STRING);

    }

    public void onClick(View view) {

    }

    public static NearbyInfo newInstance(int someInt, String s) {

        NearbyInfo nfragment = new NearbyInfo();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        nfragment.setArguments(args);
        return nfragment;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        setupMap(map);
    }

    public void setupMap(GoogleMap map) {
       /* gMap = map;
        gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(18.012, -76.797), 20));
*/
        try {
            map.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                //Fall back to coarse location.
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            LatLng jamhome = new LatLng(location.getLatitude(), location.getLongitude());
            map.getUiSettings().setZoomControlsEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(jamhome, 15));
        } catch (Exception e) {

        }
    }
}
