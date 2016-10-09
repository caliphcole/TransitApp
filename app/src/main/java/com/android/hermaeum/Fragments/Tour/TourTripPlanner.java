package com.android.hermaeum.Fragments.Tour;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.hermaeum.R;

/**
 * Created by Caliph Cole on 07/05/2015.
 */
public class TourTripPlanner extends Fragment {


    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.tourtripplanner,container,false);




        return rootView;
    }




}
