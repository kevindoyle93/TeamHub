package me.theglassboard.teamhub;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class FragmentFixtures extends Fragment {

    private Team team;

    public void setTeam(Team team) { this.team = team; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /**
         * Inflate the layout for this fragment
         */

        View view = inflater.inflate(R.layout.fragment_fixtures, container, false);

        // Views to contain fixture information
        LinearLayout fixtureList = (LinearLayout)view.findViewById(R.id.FixtureListLayout);
        LinearLayout fixtureContainer;

        LinearLayout.LayoutParams fixtureContainerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fixtureContainerParams.setMargins(0, 5, 0, 5);

        LinearLayout.LayoutParams teamNameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        LinearLayout.LayoutParams scoreParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams versusParams = new LinearLayout.LayoutParams(19, LinearLayout.LayoutParams.MATCH_PARENT);

        // Views to display fixture information
        TextView homeTeam;
        TextView homeScore;
        TextView versus;
        TextView awayScore;
        TextView awayTeam;

        // TODO: For each fixture, add a horizontal LinearLayout and add five TextViews to that to display the information
        for(Fixture f : team.getAllFixtures()) {

            Log.d("HOME TEAM", f.getHomeTeam());

            fixtureContainer = new LinearLayout(getActivity());
            fixtureContainer.setLayoutParams(fixtureContainerParams);
            fixtureContainer.setOrientation(LinearLayout.HORIZONTAL);

            homeTeam = new TextView(getActivity());
            teamNameParams.setMargins(0, 0, 10, 0);
            homeTeam.setLayoutParams(teamNameParams);
            homeTeam.setText(f.getHomeTeam());
            homeTeam.setGravity(Gravity.LEFT);
            fixtureContainer.addView(homeTeam);

            if(f.getHomeScore() != null) {

                homeScore = new TextView(getActivity());
                homeScore.setLayoutParams(scoreParams);
                homeScore.setText(f.getHomeScore());
                fixtureContainer.addView(homeScore);
            }

            versus = new TextView(getActivity());
            versus.setLayoutParams(versusParams);
            versus.setText("-");
            versus.setGravity(Gravity.CENTER);
            fixtureContainer.addView(versus);

            if(f.getAwayScore() != null) {

                awayScore = new TextView(getActivity());
                awayScore.setLayoutParams(scoreParams);
                awayScore.setText(f.getAwayScore());
                fixtureContainer.addView(awayScore);
            }

            awayTeam = new TextView(getActivity());
            teamNameParams.setMargins(10, 0, 0, 0);
            awayTeam.setLayoutParams(teamNameParams);
            awayTeam.setGravity(Gravity.RIGHT);
            awayTeam.setText(f.getAwayTeam());
            fixtureContainer.addView(awayTeam);


            fixtureList.addView(fixtureContainer);
        }

        return view;
    }

}
