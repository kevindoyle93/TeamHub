package me.theglassboard.teamhub;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class FragmentFixtures extends Fragment {

    private Team team;
    private ObjectManager objectManager;

    public void setTeam(Team team) { this.team = team; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        objectManager = new ObjectManager(getActivity());

        /**
         * Inflate the layout for this fragment
         */

        View view = inflater.inflate(R.layout.fragment_fixtures, container, false);

        // Views to contain fixture information
        LinearLayout fixtureList = (LinearLayout)view.findViewById(R.id.FixtureListLayout);
        LinearLayout fixtureContainer;

        LinearLayout.LayoutParams fixtureContainerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fixtureContainerParams.setMargins(0, 0, 0, 12);

        LinearLayout.LayoutParams teamNameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        LinearLayout.LayoutParams scoreParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams versusParams = new LinearLayout.LayoutParams(19, LinearLayout.LayoutParams.MATCH_PARENT);

        // Views to display fixture information
        TextView homeTeam;
        TextView homeScore;
        TextView versus;
        TextView awayScore;
        TextView awayTeam;


        for(int i = team.numberOfFixtures(); i > 0; i--) {

            Fixture f = team.getFixture(i - 1);

            fixtureContainer = new LinearLayout(getActivity());
            fixtureContainer.setLayoutParams(fixtureContainerParams);
            fixtureContainer.setOrientation(LinearLayout.HORIZONTAL);
            fixtureContainer.setPadding(15, 0, 25, 0);

            homeTeam = new TextView(getActivity());
            teamNameParams.setMargins(0, 0, 10, 0);
            homeTeam.setLayoutParams(teamNameParams);
            homeTeam.setText(f.getHomeTeam());
            homeTeam.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.abc_text_size_small_material));
            homeTeam.setGravity(Gravity.LEFT | Gravity.CENTER);
            homeTeam.isClickable();
            setTeamListener(homeTeam, f.getHomeTeam());
            fixtureContainer.addView(homeTeam);

            if(f.getHomeScore() != null) {

                homeScore = new TextView(getActivity());
                homeScore.setLayoutParams(scoreParams);
                homeScore.setText(f.getHomeScore());
                homeScore.setGravity(Gravity.CENTER);
                homeScore.setTextColor(Color.DKGRAY);
                homeScore.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.abc_text_size_small_material));
                fixtureContainer.addView(homeScore);

                versus = new TextView(getActivity());
                versus.setLayoutParams(versusParams);
                versus.setText("-");
                versus.setGravity(Gravity.CENTER);
                versus.setTextColor(Color.DKGRAY);
                versus.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.abc_text_size_small_material));
                fixtureContainer.addView(versus);

                awayScore = new TextView(getActivity());
                awayScore.setLayoutParams(scoreParams);
                awayScore.setText(f.getAwayScore());
                awayScore.setGravity(Gravity.CENTER);
                awayScore.setTextColor(Color.DKGRAY);
                awayScore.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.abc_text_size_small_material));
                fixtureContainer.addView(awayScore);
            }
            else {

                TextView date = new TextView(getActivity());
                date.setLayoutParams(scoreParams);
                date.setGravity(Gravity.CENTER);
                date.setText(f.getDate());
                date.setTextColor(Color.DKGRAY);
                date.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.abc_text_size_small_material));
                fixtureContainer.addView(date);
            }

            awayTeam = new TextView(getActivity());
            teamNameParams.setMargins(10, 0, 0, 0);
            awayTeam.setLayoutParams(teamNameParams);
            awayTeam.setGravity(Gravity.RIGHT | Gravity.CENTER);
            awayTeam.setText(f.getAwayTeam());
            awayTeam.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.abc_text_size_small_material));
            awayTeam.isClickable();
            setTeamListener(awayTeam, f.getAwayTeam());
            fixtureContainer.addView(awayTeam);


            fixtureList.addView(fixtureContainer);
        }

        return view;
    }

    private void setTeamListener(TextView teamView, final String teamName) {

        teamView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Overwrite the team name file
                objectManager.saveObject(teamName, "currentTeam");

                // Reload the main page
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
    }

}
