package me.theglassboard.teamhub;

import android.app.Activity;
import android.widget.TextView;

/**
 * Created by Kevin on 06/04/2015.
 */
public class Team {

    private String club;
    private String ageGroup;
    private String homePitch;
    private String manager;

    private Fixture nextFixture;
    private LeagueStats leagueStats;

    // Training
    // Crest

    public Team() {

    }

    public Team(String club, String ageGroup, String homePitch, String manager, LeagueStats leagueStats) {

        this.club = club;
        this.ageGroup = ageGroup;
        this.homePitch = homePitch;
        this.manager = manager;

        this.leagueStats = leagueStats;
    }


    // Public getters
    public String getClub() {

        return club;
    }

    public String getAgeGroup() {

        return ageGroup;
    }

    public String getHomePitch() {

        return homePitch;
    }

    public String getManager() {

        return manager;
    }


    /**
     * Set the textViews to display the team's information
     */
    public void setViews(Activity myActivity) {

        TextView textViewToChange;

        textViewToChange = (TextView)myActivity.findViewById(R.id.teamName);
        textViewToChange.setText(club.toUpperCase());

        textViewToChange = (TextView)myActivity.findViewById(R.id.homeTeam);
        textViewToChange.setText(club);

        leagueStats.setViews(myActivity);

    }



}
