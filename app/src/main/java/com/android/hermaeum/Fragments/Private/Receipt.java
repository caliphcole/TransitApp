package com.android.hermaeum.Fragments.Private;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.hermaeum.R;

/**
 * Created by Caliph Cole on 07/05/2015.
 */
public class Receipt extends Fragment {

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.receipt, container, false);

        return rootView;
    }

    public static Receipt newInstance(int someInt, String s) {
        Receipt rfragment = new Receipt();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        rfragment.setArguments(args);
        return rfragment;
    }
}
