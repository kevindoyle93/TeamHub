package me.theglassboard.teamhub;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Kevin on 30/03/2015.
 */
public class HomeFragment extends Fragment {

    View rootView;

    private String awayTeam = "Howth Celtic F.C.";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.home, container, false);

        /*TextView t = (TextView)getView().findViewById(R.id.awayTeam);
        t.setText(awayTeam);*/

        return rootView;
    }

    public void setAwayTeam(Activity myActivity) {


    }
}
