package me.theglassboard.teamhub;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FragmentFixtures extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /**
         * Inflate the layout for this fragment
         */

        View view = inflater.inflate(R.layout.fragment_fixtures, container, false);

        return view;
    }

}
