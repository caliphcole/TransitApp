package com.android.hermaeum.Fragments.Transit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.hermaeum.Data.DBHelper;
import com.android.hermaeum.Data.FeedItem;
import com.android.hermaeum.Data.Routing;
import com.android.hermaeum.R;
import com.android.hermaeum.app.AppController;
import com.android.hermaeum.volley.JSONParserRouting;
import com.gc.materialdesign.views.ButtonRectangle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Caliph Cole on 06/30/2015.
 */
public class Map extends Fragment implements OnMapReadyCallback, View.OnClickListener{

    public static final String ARG_STRING = "Map";
    private String URL_FEED = "http://jatransit.appspot.com/live";
    private String URL_COORDINATES = "https://www.hermaeum.com/coordinates";

    static View rootView;
    private GoogleMap gMap = null;
    private SupportMapFragment mapFragment;
    private ButtonRectangle search;
    private AutoCompleteTextView searchbox;
    private MaterialDialog materialDialog;

    private List<FeedItem> feedItems;


    LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {

            rootView = inflater.inflate(R.layout.map, container, false);


        } catch (InflateException e) {
    /* map is already there, just return view as it is */
        }
        feedItems = new ArrayList<FeedItem>();

        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        search = (ButtonRectangle) rootView.findViewById(R.id.search_button);
        searchbox = (AutoCompleteTextView)rootView.findViewById(R.id.searchbox);
        search.setOnClickListener(this);


        updateFeed("", "");
        //update();
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!enabled){
            enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        // check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to
        // go to the settings
        if (!enabled) {
            materialDialog = new MaterialDialog(getActivity()).setTitle("Enable Location").setMessage("Press Ok to Navigate to Location Settings")
                    .setPositiveButton("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            materialDialog.dismiss();
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });
            materialDialog.show();

        }




        return rootView;
    }

    private void closekeyBoard(){

        try {
            View view = getActivity().getCurrentFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }catch (NullPointerException e){

        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        setupMap(map);
    }

    public void setupMap(GoogleMap map) {
       /* gMap = map;
        gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(18.012, -76.797), 2));
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
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(jamhome, 12));
        } catch (Exception e) {

        }

       /* timer = new Timer();

        if(mapType.compareTo("JA") == 0 )
        {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(jamhome, 13));


            timer.scheduleAtFixedRate( new TimerTask() {
                public void run() {
                    try
                    {
                        //new JATransitLiveFeed().execute("http://developer.mbta.com/lib/GTRTFS/Alerts/VehiclePositions.pb");
                        new JATransitLiveFeed().execute("http://jatransit.appspot.com/live");
                    }
                    catch (Exception e)
                    {
                        Log.d(TAG, "JATransitLiveFeed scheduleAtFixedRate Error: " + e.toString());
                    }
                }
            }, 0, 20000);
        }*/

    }


    public static Map newInstance(int someInt, String s) {
        Map pfragment = new Map();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        pfragment.setArguments(args);
        return pfragment;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onClick(View view) {
        closekeyBoard();
        DBHelper db = new DBHelper(getActivity());
        Routing routing = new Routing();

        String s = db.searchrouteMap(searchbox.getText().toString());
        String [] tempstring = s.split(",");
        for(int i = 0 ; i< tempstring.length-1;i++) {
            String url = routing.makeURL(Double.parseDouble(tempstring[i].split("/")[0]), Double.parseDouble(tempstring[i].split("/")[1]), Double.parseDouble(tempstring[i + 1].split("/")[0]), Double.parseDouble(tempstring[i + 1].split("/")[1]));
            new ConnectAsyncTask(url).execute();
        }

    }

    private class ConnectAsyncTask extends AsyncTask<Void, Void, String> {
        String url;
        ConnectAsyncTask(String urlPass){
            url = urlPass;
        }

        @Override
        protected String doInBackground(Void... params) {
            JSONParserRouting jParser = new JSONParserRouting();
            String json = jParser.getJSONFromUrl(url);
            //Log.d("json", json);
            return json;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result!=null){
                drawPath(result);
            }
        }
    }


    public void drawPath(String result){
        try{
            //transform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString  = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            for(int z = 0; z<list.size()-1;z++){
                LatLng src = list.get(z);
                LatLng dest = list.get(z+ 1);
                mapFragment.getMap().addPolyline(new PolylineOptions().add(new LatLng(src.latitude,src.longitude),new LatLng(dest.latitude,dest.longitude)).width(10).color(Color.BLUE).geodesic(true));

            }

        }catch (Exception e){

        }
    }

    private List<LatLng> decodePoly(String encoded){
        List <LatLng> poly = new ArrayList<LatLng>();

        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    public void updateFeed(String coordinates, String live){

            JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                    URL_COORDINATES, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    if (response != null) {
                        parseCoordinateFeed(response);
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

    public void parseCoordinateFeed(JSONObject response)throws NullPointerException{
        try {
            JSONArray feedArray = response.getJSONArray("coordinates");
            DBHelper db = new DBHelper(getActivity());// instantiate the database variable
            // Log.d("boolean", "" + tripplannerdb.checkIfTableExist("tripplannerfeed"));
            if (db.checkIfTableExist("routecoordinates")) {
              db.createRouteCoordinatesTable();
            }
            if( db.tableRowCount("routecoordinates")==0) {
                for (int i = 0; i < feedArray.length(); i++) {

                    JSONObject feedObj = (JSONObject) feedArray.get(i);
                    db.insertCoordinates(feedObj.getString("route"),feedObj.getString("coordinates"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void parseJsonFeed(JSONObject response)throws NullPointerException {

        try {
            JSONArray feedArray = response.getJSONArray("trackedBus");


            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
                FeedItem item = new FeedItem();
                item.setRoute(feedObj.getString("route_id"));

                item.setOrigin(feedObj.getString("origin"));
                item.setVia(feedObj.getString("via"));
                item.setDestination(feedObj.getString("destination"));


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

                double c = location.distanceTo(buslocation);

                item.setDistance((int) (c / 1000) + " km");

                //Log.d("distance", "" + c);
                double time = c / (Double.parseDouble(feedObj.getString("velocity")) * 0.277778);

                item.setTimeStamp((int) time / 60 + "min");

                //=======================================================================
                item.setCurrentlocation(getAddress(Double.parseDouble(feedObj.getString("lat")), Double.parseDouble(feedObj.getString("long"))));

                DBHelper db = new DBHelper(getActivity());
                item = db.getBusStopData(item, location);
                feedItems.add(0, item);
            }
        }catch (Exception e){

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

}
