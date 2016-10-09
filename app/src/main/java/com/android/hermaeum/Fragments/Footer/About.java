package com.android.hermaeum.Fragments.Footer;

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
import android.widget.TextView;

import com.android.hermaeum.R;

/**
 * Created by Caliph Cole on 06/02/2015.
 */
public class About extends Fragment{

    public static final String ARG_STRING = "About";
    private View rootView;
    private TextView body;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.about, container, false);
        body = (TextView)rootView.findViewById(R.id.body);
        addContent();

        return rootView;
    }

    private void addContent() {

        String bodyText = getString(R.string.about_body);
        body.setText(bodyText);
    }

    public void setActionBarTitle(String mTitle) {
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        Log.d("Title: ", mTitle);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        setActionBarTitle(ARG_STRING);

    }

    public static About newInstance(int someInt, String someTitle){

        About afragment = new About();
        Bundle args = new Bundle();
        args.putInt(someTitle, someInt);
        afragment.setArguments(args);
        return afragment;
    }
}
