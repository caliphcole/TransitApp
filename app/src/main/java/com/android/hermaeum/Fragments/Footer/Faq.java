package com.android.hermaeum.Fragments.Footer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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
public class Faq extends Fragment{
    public static final String ARG_STRING = "FAQ";

    private View rootView;
    private TextView question1;
    private TextView answer1;
    private TextView question2;
    private TextView answer2;
    private TextView question3;
    private TextView answer3;
    private TextView question4;
    private TextView answer4;
    private TextView question5;
    private TextView answer5;
    private TextView question6;
    private TextView answer6;




    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        rootView = inflater.inflate(R.layout.faq,container,false);

        question1 = (TextView)rootView.findViewById(R.id.question1);
        answer1 = (TextView)rootView.findViewById(R.id.answer1);

        question2 = (TextView)rootView.findViewById(R.id.question2);
        answer2 = (TextView)rootView.findViewById(R.id.answer2);

        question3 = (TextView)rootView.findViewById(R.id.question3);
        answer3 = (TextView)rootView.findViewById(R.id.answer3);

        question4 = (TextView)rootView.findViewById(R.id.question4);
        answer4 = (TextView)rootView.findViewById(R.id.answer4);
        question5 = (TextView)rootView.findViewById(R.id.question5);
        answer5 = (TextView)rootView.findViewById(R.id.answer5);
        question6 = (TextView)rootView.findViewById(R.id.question6);
        answer6 = (TextView)rootView.findViewById(R.id.answer6);


        addContent();



        return rootView;
    }

    private void addContent() {
        String [] questions = getActivity().getResources().getStringArray(R.array.questions);
        String [] answers = getActivity().getResources().getStringArray(R.array.answers);



        question1.setText(questions[0]);
        answer1.setText(answers[0]);

        question2.setText(questions[1]);
        answer2.setText(answers[1]);

        question3.setText(questions[2]);
        answer3.setText(answers[2]);

        question4.setText(questions[3]);
        answer4.setText(answers[3]);

        question5.setText(questions[4]);
        answer5.setText(answers[4]);

        question6.setText(questions[5]);
        answer6.setText(answers[5]);

    }

    public void setActionBarTitle(String mTitle) {
        ActionBar actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);

        actionBar.setTitle(mTitle);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        setActionBarTitle(ARG_STRING);

    }

    public static Faq newInstance(int someInt, String s) {
        Faq ffragment = new Faq();
        Bundle args = new Bundle();
        args.putInt(s, someInt);
        ffragment.setArguments(args);
        return ffragment;
    }

}

