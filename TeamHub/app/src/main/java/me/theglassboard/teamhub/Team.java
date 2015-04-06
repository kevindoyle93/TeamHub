package me.theglassboard.teamhub;

import android.app.Activity;
import android.widget.TextView;

/**
 * Created by Kevin on 06/04/2015.
 */
public class Team {

    private String name;
    private String ageGroup;
    private String homePitch;
    private String manager;

    private Fixture nextFixture;
    private LeagueStats leagueStats;

    // Training
    // Crest

    public Team() {

    }

    public Team(String name, String ageGroup, String homePitch, String manager) {

        this.name = name;
        this.ageGroup = ageGroup;
        this.homePitch = homePitch;
        this.manager = manager;
    }


    // Public getters
    public String getName() {

        return name;
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
        textViewToChange.setText(name.toUpperCase());

        textViewToChange = (TextView)myActivity.findViewById(R.id.homeTeam);
        textViewToChange.setText(name);

    }



}
