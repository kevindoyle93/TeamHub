package me.theglassboard.teamhub;

/**
 * Created by Kevin on 06/04/2015.
 */
public class Fixture {

    private Team homeTeam;
    private Team awayTeam;
    private String location;
    private String time;
    private String referee;

    public Fixture() {

    }

    public Fixture(Team homeTeam, Team awayTeam, String location, String time, String referee) {

        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.location = location;
        this.time = time;
        this.referee = referee;
    }


    // Public Getters
    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public String getReferee() {
        return referee;
    }
}
