package me.theglassboard.teamhub;

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
public class TrainingFragment extends Fragment {

    View rootView;
    private String giveItAGo = "Did this work?";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.training, container, false);

        TextView t = (TextView)getView().findViewById(R.id.meh);
        t.setText(giveItAGo);

        return rootView;
    }
}
