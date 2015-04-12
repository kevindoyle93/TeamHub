package me.theglassboard.teamhub;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kevin on 06/04/2015.
 */
public class Team {

    private String club;

    /*private String ageGroup;
    private String homePitch;
    private String manager;*/

    private ArrayList<Fixture> fixtures;
    private LeagueStats leagueStats;

    public Team() {

    }

    public Team(String club, /* String ageGroup, String homePitch, String manager, */LeagueStats leagueStats) {

        this.club = club;

        /*this.ageGroup = ageGroup;
        this.homePitch = homePitch;
        this.manager = manager;*/

        this.leagueStats = leagueStats;

        fixtures = new ArrayList<>();
    }


    // Public getters
    public String getClub() { return club; }

    /*public String getAgeGroup() { return ageGroup; }

    public String getHomePitch() { return homePitch; }

    public String getManager() { return manager; }*/

    public LeagueStats getLeagueStats() { return leagueStats; }


    public void addToFixtures(Fixture f) {

        fixtures.add(f);
    }


    /**
     * Set the textViews to display the team's information
     */
    public void setViews(Activity myActivity) {

        TextView textViewToChange;

        Log.d("CURRENT TEAM NAME" , club);

        textViewToChange = (TextView)myActivity.findViewById(R.id.teamName);
        textViewToChange.setText(club.toUpperCase());

        // leagueStats.setViews(myActivity, club);

        if(fixtures.size() > 0) {

            int fixtureCount = 0;

            while(fixtureCount < fixtures.size() - 1 && fixtures.get(fixtureCount).getHomeScore() != null) {

                fixtureCount++;
            }

            fixtures.get(fixtureCount).setViews(myActivity);
        }
    }
}
