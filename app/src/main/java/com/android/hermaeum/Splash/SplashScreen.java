package com.android.hermaeum.Splash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.hermaeum.Data.DBHelper;
import com.android.hermaeum.Fragments.Transit.MainScreen;
import com.android.hermaeum.MainActivity;
import com.android.hermaeum.R;
import com.android.hermaeum.app.AppController;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Caliph Cole on 06/16/2015.
 */
public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 6000;

    //url for static routes, timetable,busstop
    private String URL_FEED = "http://jatransit.appspot.com/routes";
    //private final String URL_TIMETABLE_FEED = "http://test123calil.co.nf/monaspot/testingmultiplearray.php";
    private final String URL_TRIPPLANNERDATA_FEED="https://www.hermaeum.com/stops";//http://test123calil.co.nf/monaspot/tripplannerdatascript.php";
    private final String URL_TIMETABLE_FEED = "https://www.hermaeum.com/timetable";

    private static final String TAG = MainScreen.class.getSimpleName();


    private Timer myTimer;

    private Context contextForDialog = null;

    DBHelper timetabledb ;
    DBHelper tripplannerdb;
    //Time table variables


    String timetablename;
    private boolean check = true;
    private MaterialDialog materialDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splashscreen);

        updateTripplanner();
        updatetimetable();
        contextForDialog = this;



        // new Handler().postDelayed(new Runnable() {
        myTimer = new Timer(); //Set up a timer, to execute TimerMethod repeatedly
        myTimer.schedule(new TimerTask() {
            /*
             * Showing splash screen with a timer.
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);

                if(check) {
                    startActivity(i);


                    //close this activity
                    finish();
                }


            }
        }, SPLASH_TIME_OUT);
    }

    private void updateTripplanner(){

        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                URL_TRIPPLANNERDATA_FEED, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                   // Log.d("trip","check2");
                    new ConnectAsyncTask2(response, URL_TRIPPLANNERDATA_FEED).execute();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                check = false;
                materialDialog = new MaterialDialog(SplashScreen.this).setTitle("Internet Issues").setMessage("No Internet Connection or Server is Down\nNeed Internet Access to Retrieve Data\nfrom Server.\n\nTurn on Data Service or Connect to\nWIFI to Access Bus Route Data.")
                        .setPositiveButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                materialDialog.dismiss();
                                finish();

                            }
                        });
                materialDialog.show();
            }
        });
        jsonReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);

    }

    private void updatetimetable(){


        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                URL_TIMETABLE_FEED, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    new ConnectAsyncTask1(response,URL_TIMETABLE_FEED).execute();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                check = false;
                materialDialog = new MaterialDialog(SplashScreen.this).setTitle("Internet Issues").setMessage("No Internet Connection or Server is Down\nNeed Internet Access to Retrieve Data\nfrom Server.\n\nTurn on Data Service or Connect to\nWIFI to Access Bus Route Data.")
                        .setPositiveButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                materialDialog.dismiss();
                                finish();

                            }
                        });
                materialDialog.show();
            }
        });
        jsonReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



        // Adding request to volley request queue
        AppController.getInstance().addToRequestQueue(jsonReq);

    }

    private int numberoftables(JSONObject response){
        try {
            JSONArray feedArraymain = response.getJSONArray("TimeTable");


            JSONObject weekdayObj = (JSONObject) feedArraymain.get(0);
            JSONArray  feedArraychild0 = weekdayObj.getJSONArray("weekday_");
            JSONObject saturdayObj = (JSONObject) feedArraymain.get(1);
            JSONArray  feedArraychild1 = saturdayObj.getJSONArray("saturday_");
            JSONObject sundayObj = (JSONObject) feedArraymain.get(2);
            JSONArray  feedArraychild2 = sundayObj.getJSONArray("sunday_");

            Log.d("count",feedArraychild0.length()+" "+ feedArraychild1.length()+" "+feedArraychild2.length());

            Bundle bundle = new Bundle();
            bundle.putInt("weekday",feedArraychild0.length() );
            bundle.putInt("saturday",feedArraychild1.length() );
            bundle.putInt("sunday",feedArraychild2.length() );

            MainScreen main = MainScreen.newInstance(1, MainScreen.ARG_STRING);
            main.setArguments(bundle);

        }catch (Exception e){

        }

        return 0;
    }
    private void  parseJsonFeed(JSONObject response,final String tag) {

        switch (tag) {
            case URL_TIMETABLE_FEED:
                numberoftables(response);
                try{
                    String [] day = {"weekday_","saturday_","sunday_"};
                    JSONArray feedArraymain = response.getJSONArray("TimeTable");
                    JSONArray dataArray = new JSONArray();
                    timetabledb = new DBHelper(this);

                   // Log.d("lengthmain",""+ feedArraymain.length());
                    for(int i = 0 ; i <feedArraymain.length();i++){// cycle through list of timetables

                        JSONObject feedObj = (JSONObject) feedArraymain.get(i);
                        JSONArray  feedArraychild = feedObj.getJSONArray(day[i]);

                       // Log.d("lengthchild",""+ feedArraychild.length());
                        for (int a = 0 ; a<feedArraychild.length();a++) {// cycle through individually

                            JSONObject feedArraychilddept1 = (JSONObject) feedArraychild.get(a);
                            String routeName = feedArraychilddept1.names().get(0).toString();
                            //Log.d("routenames",routeName);

                            JSONArray feedArraychilddept2 = feedArraychilddept1.getJSONArray(routeName);
                            ArrayList<String> vianames = new ArrayList<String>();
                            for (int e = 0; e < ((JSONObject) feedArraychilddept2.get(0)).names().length(); e++) {

                                vianames.add(((JSONObject) feedArraychilddept2.get(0)).names().get(e).toString());

                            }
                            if (timetabledb.checkIfTableExist(day[i] + routeName)) {
                                timetabledb.createTimeTable(day[i] + routeName, vianames);


                                //Log.d("lengthchilddept2", ""+feedArraychilddept2.length());
                                for (int b = 0; b < feedArraychilddept2.length()/2; b++) {

                                    JSONObject feedObjdept2 = (JSONObject) feedArraychilddept2.get(b);
                                    ArrayList<String> times = new ArrayList<String>();
                                    //Log.d("lengthObjdept2", ""+feedObjdept2.length());
                                    for (int c = 0; c < feedObjdept2.length(); c++) {
                                        //Log.d("routenames", feedObjdept2.names().get(c).toString());
                                        String s = feedObjdept2.names().get(c).toString();
                                        times.add(feedObjdept2.get(s).toString());

                                    }
                                    timetabledb.insertTime(day[i] + routeName, times, feedObjdept2.length());
                                    if((day[i] + routeName).equals("weekday_75")){
                                        //Log.d("lengthchilddept2", ""+feedArraychilddept2.length());
                                        //Log.d("lengthchilddept2test", "check");
                                    }
                                    //Log.d("Rows" + day[i] + routeName, "" + timetabledb.tableRowCount(day[i] + routeName));
                                }
                            }
                        }
                    }
                    //Log.d("All tables", timetabledb.getAllTables());
                  // Log.d("weekday_75",""+timetabledb.tableRowCount("weekday_75"));
                }catch(Exception e){

                }

                break;
            case URL_TRIPPLANNERDATA_FEED:
                try{
                    JSONArray feedArray = response.getJSONArray("routes_array");

                    tripplannerdb = new DBHelper(this);// instantiate the database variable
                   // Log.d("boolean", "" + tripplannerdb.checkIfTableExist("tripplannerfeed"));
                   if (tripplannerdb.checkIfTableExist("tripplannerfeed")) {
                        tripplannerdb.createTripplannerDataFeed();

                        for (int i = 0; i < feedArray.length(); i++) {

                            JSONObject feedObj = (JSONObject) feedArray.get(i);
                            String stops = "";

                            for (int a = 0; a < feedObj.getJSONArray("start_end").length(); a++) {
                                String start = feedObj.getJSONArray("start_end").getJSONObject(a).get("start").toString();
                                String end = feedObj.getJSONArray("start_end").getJSONObject(a).get("end").toString();

                                stops = ""+","+""+","+start + ";"+""+","+""+"," + end;

                            }
                            for (int a = 0; a < feedObj.getJSONArray("stops").length(); a++) {


                                String lat = feedObj.getJSONArray("stops").getJSONObject(a).get("Latitude").toString();
                                String longi = feedObj.getJSONArray("stops").getJSONObject(a).get("Longitude").toString();
                                String Stop = feedObj.getJSONArray("stops").getJSONObject(a).get("Stop").toString();

                                if (stops.isEmpty()) {
                                    stops = lat + "," + longi + "," + Stop;
                                } else {
                                    stops = stops + ";" + lat + "," + longi + "," + Stop;
                                }
                            }
                            // Log.d("stopsroute",feedObj.getString("route"));
                            // Log.d("stops",stops);
                            tripplannerdb.insertTripPlannerFeed(feedObj.getString("route"), stops);
                        }
                    }

                }catch (Exception e){
                   // Log.d("crashes", e.toString());
                }
                //Log.d("tripplanner size", ""+timetabledb.tableRowCount("tripplannerfeed"));
                break;
        }
    }

    private class ConnectAsyncTask1 extends AsyncTask<Void, Void,Void> {
        private ProgressDialog progressDialog;
        JSONObject response;
        final String tag;
        ConnectAsyncTask1(JSONObject response,final String tag){
            this.response = response;
            this.tag = tag;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Void... params) {
            parseJsonFeed(response, tag);

            return null;
        }

    }

    private class ConnectAsyncTask2 extends AsyncTask<Void, Void,Void> {
        private ProgressDialog progressDialog;
        JSONObject response;
        final String tag;
        ConnectAsyncTask2(JSONObject response,final String tag){

            this.response = response;
            this.tag = tag;
        }
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Void... params) {
            parseJsonFeed(response, tag);

            return null;
        }

    }
}

