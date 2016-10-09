package com.android.hermaeum.Fragments.Transit;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.hermaeum.Adapter.AutoCompleteAdapter;
import com.android.hermaeum.Data.DBHelper;
import com.android.hermaeum.R;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Caliph Cole on 06/02/2015.
 */
public class TripPlanner extends Fragment implements View.OnClickListener, AutoCompleteTextView.OnEditorActionListener,View.OnFocusChangeListener{

    public static final String ARG_STRING = "Trip Planner";


    private View rootView;
    private AutoCompleteTextView textOrigin;
    private AutoCompleteTextView textDestination;
    private CheckBox orgLocation,desLocation;
    private ImageView canceltext1,canceltext2;
    ArrayAdapter<String> adapter;



    private TripPlannerResult tripPlannerResults;

    private TextView time;


    private MaterialDialog materialDialog;
    private  FloatingActionButton helpbutton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.tripplanner,container,false);
        textOrigin = (AutoCompleteTextView)rootView.findViewById(R.id.origin);
        textDestination = (AutoCompleteTextView)rootView.findViewById(R.id.destination);
        orgLocation = (CheckBox)rootView.findViewById(R.id.location1);
        desLocation = (CheckBox)rootView.findViewById(R.id.location2);
        canceltext1 = (ImageView)rootView.findViewById(R.id.cancel1);
        canceltext2 =(ImageView)rootView.findViewById(R.id.cancel2);





        helpbutton = (FloatingActionButton)rootView.findViewById(R.id.help);

        helpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog = new MaterialDialog(getActivity()).setTitle("Help Menu").setMessage("Enter origin/use current location and enter destination")
                        .setPositiveButton("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                materialDialog.dismiss();
                            }
                        });
                materialDialog.show();
            }
        });

        //function will query database to help user choose best input

        orgLocation.setOnClickListener(this);
        desLocation.setOnClickListener(this);
        canceltext1.setOnClickListener(this);
        canceltext2.setOnClickListener(this);
        textDestination.setOnEditorActionListener(this);
        textOrigin.setOnEditorActionListener(this);

        new HelpSuggestion().execute();




        return rootView;
    }

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

    @Override
    public void onFocusChange(View view, boolean b) {
        switch(view.getId()){

            case R.id.origin:

                new HelpSuggestion().execute();
                break;

            case R.id.destination:

                new HelpSuggestion().execute();

                break;
        }
    }


    private class HelpSuggestion extends AsyncTask<Void, Void,ArrayAdapter<String>> {

        DBHelper db = new DBHelper(getActivity());


        @Override
        protected ArrayAdapter<String> doInBackground(Void... params) {

          /*  try {
               Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.UK);

                List<Address> results = geocoder.getFromLocationName(text,3,17.860120,-77.094654,18.168402,-76.686178);

                if (results.size() == 0) {
                    return null;
                }
                ArrayList<String> address = new ArrayList<String>();
                for(int i=0;i<results.size();i++){

                    address.add(results.get(i).getFeatureName());
                }

                return new AutoCompleteAdapter(getActivity(), android.R.layout.select_dialog_item,android.R.id.text1,address);


            } catch (Exception e) {
                Log.e("", "Something went wrong: ", e);
                return null;
            }*/

            try{
                Log.d("Checking","checking");
                return new AutoCompleteAdapter(getActivity(), android.R.layout.select_dialog_item,android.R.id.text1,db.helpTextQuery());

            } catch (Exception e) {
                Log.e("", "Something went wrong: ", e);
                return null;
            }

        }
        @Override
        protected void onPostExecute(ArrayAdapter<String> result) {
            super.onPostExecute(result);

            adapter = result;
            if(textOrigin.isFocused()) {

                textOrigin.setAdapter(adapter);
                Log.d("Checking", "checking1");
            }else{
                Log.d("Checking","checking2");
                textDestination.setAdapter(adapter);

            }

        }
    }

    @Override
    public void onClick(View view) {


        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            // Fall back to coarse location.
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        switch(view.getId()){

            case R.id.location1:
                boolean checked1 = ((CheckBox)view).isChecked();
                if(checked1){
                    textOrigin.setText(getAddress(location.getLatitude(),location.getLongitude()));
                }else{
                    textOrigin.setText("");
                }
                break;
            case R.id.location2:
                boolean checked2 = ((CheckBox)view).isChecked();
                if(checked2){
                    textDestination.setText(getAddress(location.getLatitude(),location.getLongitude()));
                }else{
                    textDestination.setText("");
                }
                break;
            case R.id.cancel1:
                textOrigin.setText("");
                orgLocation.setChecked(false);
                break;
            case R.id.cancel2:
                textDestination.setText("");
                desLocation.setChecked(false);
                break;
      }

    }

    public static TripPlanner newInstance(int someInt, String s) {

        TripPlanner tfragment = new TripPlanner();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        tfragment.setArguments(args);
        return tfragment;
    }

    public void setActionBarTitle(String mTitle) {
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) { setActionBarTitle(ARG_STRING);}



    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(i== EditorInfo.IME_ACTION_SEARCH){
            //get text from text feild and spinners
            //do error checking
            String origin = textOrigin.getText().toString().trim();
            String destine = textDestination.getText().toString().trim();

       /* if(origin.isEmpty()||destine.isEmpty()){
            textOrigin.setHint("Enter Origin");
            textDestination.setHint("Enter Destination");
        }else if(!(hrboolean && minboolean&&daysboolean&&meridiemboolean)){

            Toast.makeText(getActivity(), "Select drop down", Toast.LENGTH_SHORT).show();
        }  else {*/

            Bundle bundle = new Bundle();
            bundle.putString("origin", origin);
            bundle.putString("destination", destine);
            // bundle.putString("hr",hr.getSelectedItem().toString());
            // bundle.putString("min",min.getSelectedItem().toString());
            //bundle.putString("meridiem",meridiem.getSelectedItem().toString());
            //bundle.putString("days",days.getSelectedItem().toString());

            tripPlannerResults = tripPlannerResults.newInstance(0, tripPlannerResults.ARG_STRING);
            tripPlannerResults.setArguments(bundle);

            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.container, tripPlannerResults, tripPlannerResults.ARG_STRING);
            ft.addToBackStack(null);
            ft.commit();
        }
        return  true;
    }


}
