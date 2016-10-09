package com.android.hermaeum.Fragments.Transit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.hermaeum.Adapter.ScheduleAdapter;
import com.android.hermaeum.Data.DBHelper;
import com.android.hermaeum.Data.TimeDataItem;
import com.android.hermaeum.R;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Caliph Cole on 06/11/2015.
 */
public class Schedule extends Fragment implements AdapterView.OnItemSelectedListener,AdapterView.OnItemClickListener{

    public static final String ARG_STRING = "Schedule";

    private View rootView;
    private ListView listView;
    private ScheduleAdapter listAdapter;

    private List<TimeDataItem> feedItems;

    private DBHelper routedb;

    private MaterialDialog materialDialog;
    private FloatingActionButton helpbutton;

    private Spinner week,time,place,deptarrival,direction;
    private TextView routenumber,resultholder;

    private boolean weekboolean =false,timeboolean=false,placeboolean=false,deptarrivalboolean=false,directionboolean = false;

    ArrayAdapter<CharSequence> adapter1,adapter2,adapter3,adapter4;
    private DBHelper db;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.schedule, container, false);

        routenumber = (TextView)rootView.findViewById(R.id.route);
        resultholder = (TextView)rootView.findViewById(R.id.resultholder);
        listView = (ListView) rootView.findViewById(R.id.listView);
        db = new DBHelper(getActivity());


        feedItems = new ArrayList<TimeDataItem>();




        routenumber = (TextView)rootView.findViewById(R.id.route);
        week = (Spinner)rootView.findViewById(R.id.week);
        time = (Spinner)rootView.findViewById(R.id.time);
        place =(Spinner)rootView.findViewById(R.id.place);
        deptarrival =(Spinner)rootView.findViewById(R.id.deptarrival);
        direction = (Spinner)rootView.findViewById(R.id.direction);



        week.setOnItemSelectedListener(this);
        time.setOnItemSelectedListener(this);
        place.setOnItemSelectedListener(this);
        deptarrival.setOnItemSelectedListener(this);
        direction.setOnItemSelectedListener(this);

        Bundle bundle = getArguments();
        routenumber.setText(bundle.getString("route"));
        resultholder.setText("Result times display here...");

        String [] orgdes = new String[3];
        orgdes[0] = "Select Direction of travel";
        orgdes[1] = bundle.getString("origin")+" to "+ bundle.getString("destination");
        orgdes[2] = bundle.getString("origin")+" from "+ bundle.getString("destination");
        spinnerAdapterHandler(bundle.getString("route"),orgdes, week, time, direction, deptarrival);

        listAdapter = new ScheduleAdapter(getActivity(), feedItems);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);

        return rootView;

    }

    private void spinnerAdapterHandler(String routenumber,String[] orgdes,Spinner a, Spinner b, Spinner c,Spinner d){




        adapter1 = ArrayAdapter.createFromResource(getActivity(),
                R.array.dayoftheweek, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        a.setAdapter(adapter1);


       adapter2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.time, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        b.setAdapter(adapter2);



      adapter4 = ArrayAdapter.createFromResource(getActivity(),
                R.array.deptarrival, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        d.setAdapter(adapter4);

        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,orgdes);

        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        c.setAdapter(adapter5);
    }

    private void update(String routenumber,String week,String timeofday,String via,String deptarrival,String direction) {


        feedItems.clear();
        List<TimeDataItem> q = new ArrayList<TimeDataItem>();
        DBHelper db = new DBHelper(getActivity());

        //send argument to database class for query
        q =db.getTimeQuery(week,routenumber,timeofday,via,deptarrival,direction);

        for(int i = 0; i<q.size();i++){

            feedItems.add(q.get(i));
        }

        listAdapter.setVia(via + "_" + deptarrival);

               // Log.d("feed content:", "" + feedItems.get(i).getTimeStamp());

        Log.d("feed size:", "" + feedItems.size());


        listAdapter.notifyDataSetChanged();
        if(listAdapter.isEmpty()){
            resultholder.setText("No Results");
        }else {
            resultholder.setText("");
        }

    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

      switch (adapterView.getId()) {
           case R.id.week:
               //Toast.makeText(getActivity(), "week", Toast.LENGTH_SHORT).show();
               switch (i){
                   case 0:
                      // Toast.makeText(getActivity(), "Select", Toast.LENGTH_SHORT).show();
                       weekboolean=false;
                       break;
                   default:
                       weekboolean = true;
                       try {
                           ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, db.getschedulevia(week.getSelectedItem().toString()+"_"+routenumber.getText().toString()));
                           // Specify the layout to use when the list of choices appears
                           adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
                           place.setAdapter(adapter3);


                       }catch (Exception e){

                       }

               }
               break;
           case R.id.place:
               //Toast.makeText(getActivity(), "place", Toast.LENGTH_SHORT).show();
               switch (i){
                   case 0:
                       //Toast.makeText(getActivity(), "Select", Toast.LENGTH_SHORT).show();
                       placeboolean=false;
                       break;
                   default:
                     //  Toast.makeText(getActivity(), "hwt", Toast.LENGTH_SHORT).show();
                       placeboolean=true;
                       break;


               }
               break;
          case R.id.time:
              //Toast.makeText(getActivity(), "time of day", Toast.LENGTH_SHORT).show();
              switch (i){
                  case 0:
                     // Toast.makeText(getActivity(), "Select", Toast.LENGTH_SHORT).show();
                      timeboolean=false;
                      break;
                  default:
                     // Toast.makeText(getActivity(), "Morning", Toast.LENGTH_SHORT).show();
                      timeboolean=true;
                      break;


              }
              break;
          case R.id.deptarrival:
              //Toast.makeText(getActivity(), "depart and arrival", Toast.LENGTH_SHORT).show();
              switch (i){
                  case 0:
                    //  Toast.makeText(getActivity(), "Select", Toast.LENGTH_SHORT).show();
                      deptarrivalboolean=false;
                      break;
                  case 1:
                     // Toast.makeText(getActivity(), "arrival", Toast.LENGTH_SHORT).show();
                      deptarrivalboolean=true;
                      break;
                  case 2:
                     // Toast.makeText(getActivity(), "departure", Toast.LENGTH_SHORT).show();
                      deptarrivalboolean=true;
                      break;

              }
              break;
          case R.id.direction:
              //Toast.makeText(getActivity(), "depart and arrival", Toast.LENGTH_SHORT).show();
              switch (i){
                  case 0:
                      //  Toast.makeText(getActivity(), "Select", Toast.LENGTH_SHORT).show();
                      directionboolean=false;
                      break;
                  case 1:
                      // Toast.makeText(getActivity(), "arrival", Toast.LENGTH_SHORT).show();
                      directionboolean=true;
                      break;
                  case 2:
                      // Toast.makeText(getActivity(), "departure", Toast.LENGTH_SHORT).show();
                      directionboolean=true;
                      break;

              }
              break;

       }

        if(placeboolean&&timeboolean&&deptarrivalboolean&&weekboolean&&directionboolean){


            update(routenumber.getText().toString(),week.getSelectedItem().toString(), time.getSelectedItem().toString(), place.getSelectedItem().toString(), deptarrival.getSelectedItem().toString(),direction.getSelectedItem().toString());


        }else {
            feedItems.clear();
            listAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void setActionBarTitle(String mTitle) {
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        setActionBarTitle(ARG_STRING);

    }

    public static Schedule newInstance(int someInt, String s) {

        Schedule sfragment = new Schedule();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        sfragment.setArguments(args);
        return sfragment;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String tempstring = "";
        DBHelper viadb = new DBHelper(getActivity());
        ArrayList<String> temparraylist= viadb.getAllVia(week.getSelectedItem().toString() + "_" + routenumber.getText().toString());
        for(int a = 0; a<temparraylist.size(); a++) {
            if (tempstring.equals("")) {
                tempstring = temparraylist.get(a) +": "+ feedItems.get(i).getNexttimes().split("\n")[a];
            }else{
                tempstring =tempstring+"\n"+temparraylist.get(a) +": "+ feedItems.get(i).getNexttimes().split("\n")[a];;
            }
        }
        materialDialog = new MaterialDialog(getActivity()).setTitle("Timetable")
                .setMessage(tempstring)
                .setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        materialDialog.dismiss();
                    }
                });
        materialDialog.show();
    }
}
