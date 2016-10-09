package com.android.hermaeum;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.android.hermaeum.Adapter.FooterIconAdapter;
import com.android.hermaeum.Fragments.Footer.About;
import com.android.hermaeum.Fragments.Footer.Faq;
import com.android.hermaeum.Fragments.Private.PrivateTaxiMainScreen;
import com.android.hermaeum.Fragments.Transit.MainScreen;
import com.android.hermaeum.Fragments.Tour.TourTripPlanner;
import com.getbase.floatingactionbutton.FloatingActionButton;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by Caliph Cole on 07/04/2015.
 */
public class Main extends Fragment
        implements ActionBar.TabListener, View.OnClickListener {

    private final Handler handler = new Handler();
    public static final String ARG_STRING = "HOME";
    ViewPager mViewPager;
    private PagerSlidingTabStrip tabs;

    private Faq faq;
    private About about;
    private MaterialDialog materialDialog;
    private FloatingActionButton leftbutton;
    private FloatingActionButton rightbutton;

    private int currentColor = 0xFF666666;
    private Drawable oldBackground = null;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.tabswipeview, container,
                false);

        leftbutton = (FloatingActionButton)rootView.findViewById(R.id.left);
        rightbutton = (FloatingActionButton)rootView.findViewById(R.id.right);

        tabs = (PagerSlidingTabStrip)rootView.findViewById(R.id.pager_title_strip);

        leftbutton.setOnClickListener(this);
        rightbutton.setOnClickListener(this);

        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getChildFragmentManager());

        // Set up the action bar.
        final ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);

        final int pagemargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,4,getResources().getDisplayMetrics());

        mViewPager.setPageMargin(pagemargin);
        tabs.setShouldExpand(true);


        tabs.setViewPager(mViewPager);
       // changeColor(currentColor);



       tabs.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
           @Override
           public void onPageSelected(int position) {
               // When swiping between different app sections, select the corresponding tab.
               // We can also use ActionBar.Tab#select() to do this if we have a reference to the
               // Tab.
               try {
                   actionBar.setSelectedNavigationItem(position);
               } catch (Exception e) {

               }
                /*rightbutton.setVisibility(View.VISIBLE);
                leftbutton.setVisibility(View.VISIBLE);
                if( !(mAppSectionsPagerAdapter.getPageTitle(position).equals("Private"))){


                }*/

           }

           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               switch (position) {

                   case 0:
                       rightbutton.setVisibility(View.INVISIBLE);
                       leftbutton.setVisibility(View.INVISIBLE);

                       try {
                           View view = getActivity().getCurrentFocus();
                           InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                                   Context.INPUT_METHOD_SERVICE);
                           imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                       } catch (NullPointerException e) {

                       }

                       break;
                   case 1:
                       rightbutton.setVisibility(View.VISIBLE);
                       leftbutton.setVisibility(View.VISIBLE);
                       rightbutton.setColorNormal(getResources().getColor(android.R.color.transparent));
                       leftbutton.setColorNormal(getResources().getColor(android.R.color.transparent));

                       try {
                           View view = getActivity().getCurrentFocus();
                           InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                                   Context.INPUT_METHOD_SERVICE);
                           imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                       } catch (NullPointerException e) {

                       }

                       break;
                   default:
                       rightbutton.setVisibility(View.INVISIBLE);
                       leftbutton.setVisibility(View.INVISIBLE);
                       try {
                           View view = getActivity().getCurrentFocus();
                           InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                                   Context.INPUT_METHOD_SERVICE);
                           imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                       } catch (NullPointerException e) {

                       }
                       break;
               }
           }
       });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));

        }

        ((GridView) rootView.findViewById(R.id.footericon))
                .setAdapter(new FooterIconAdapter(getActivity()));
        gridViewListener();



       return rootView;
    }



    public void gridViewListener() {
        ((GridView) rootView.findViewById(R.id.footericon)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                switch (position) {
                    case 0://faq
                       // onSectionAttached(position);
                        faq = Faq.newInstance(position, faq.ARG_STRING);
                        FragmentManager fm5 = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft5 = fm5.beginTransaction();


                        if (faq.isAdded()) {
                            ft5.show(faq);
                        } else {
                            ft5.replace(R.id.container, faq, faq.ARG_STRING);
                        }
                        ft5.addToBackStack(null);
                        ft5.commit();


                        break;

                    case 1://about
                        //onSectionAttached(position);
                        about = about.newInstance(position, about.ARG_STRING);
                        FragmentManager fm6 = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft6 = fm6.beginTransaction();


                        if (about.isAdded()) {
                            ft6.show(about);
                        } else {
                            ft6.replace(R.id.container, about, about.ARG_STRING);
                        }
                        ft6.addToBackStack(null);
                        ft6.commit();

                        break;
                    case 2:
                        materialDialog = new MaterialDialog(getActivity()).setTitle("Help Menu").setMessage("Nearby - List of closest bus to your location\n" +
                                ("Route - See all the static routes provides by JUTC and static timetable\nTrip - Search for routes from orgin to detination \n Search - Search for" +
                                        "for routes"))
                                .setPositiveButton("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        materialDialog.dismiss();
                                    }
                                });
                        materialDialog.show();
                        break;

                }
            }
        });
    }
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onClick(View view) {


        switch(view.getId()){
            case R.id.left:
                mViewPager.setCurrentItem(0);
                Toast.makeText(getActivity(),"left",Toast.LENGTH_SHORT).show();
                break;
            case R.id.right:
                mViewPager.setCurrentItem(2);
                Toast.makeText(getActivity(),"right",Toast.LENGTH_SHORT).show();
                break;

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // The first section of the app is the most interesting -- it offers
                    // a launchpad into the other demonstrations in this example application.
                    return new MainScreen();

                case 1:

                    return new PrivateTaxiMainScreen();

                default:

                    return new TourTripPlanner();

            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch(position){
                case 0:
                    return "Transit";
                case 1:
                    return "Private";
                default:
                    return "Tour";
            }

        }


    }






    public static Main newInstance(int someInt, String someTitle) {
        Main mfragment = new Main();
        Bundle args = new Bundle();
        args.putInt(someTitle, someInt);
        mfragment.setArguments(args);
        return mfragment;
    }

}
