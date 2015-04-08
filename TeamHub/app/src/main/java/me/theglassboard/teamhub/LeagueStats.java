package me.theglassboard.teamhub;

import android.app.Activity;
import android.widget.TextView;

/**
 * Created by Kevin on 06/04/2015.
 */
public class LeagueStats {

    /*private int played;
    private int won;
    private int drawn;
    private int lost;
    private int goalsFor;
    private int goalsAgainst;
    private int goalDifference;
    private int points;*/

    private String[] stats;

    public LeagueStats(String[] stats) {

        this.stats = stats;
    }

    public LeagueStats(String position, String played, String won, String drawn, String lost, String goalsFor, String goalsAgainst, String goalDifference, String points) {

        stats = new String[9];

        stats[0] = position;
        stats[1] = played;
        stats[2] = won;
        stats[3] = drawn;
        stats[4] = lost;
        stats[5] = goalsFor;
        stats[6] = goalsAgainst;
        stats[7] = goalDifference;
        stats[8] = points;
    }

    public String getStat(int i) {

        return stats[i];
    }


    // Set the textViews for the league table section of home.xml
    public void setViews(Activity myActivity, String club) {

        final TextView positionTextView;
        final TextView clubTextView;
        final TextView pointsTextView;
        final TextView playedTextView;

        positionTextView = (TextView) myActivity.findViewById(R.id.position0);
        positionTextView.setText(stats[0] + ".");

        pointsTextView = (TextView) myActivity.findViewById(R.id.points0);
        pointsTextView.setText(stats[8]);

        playedTextView = (TextView) myActivity.findViewById(R.id.played0);
        playedTextView.setText(stats[1]);

        clubTextView = (TextView) myActivity.findViewById(R.id.team0);
        clubTextView.setText(club);
    }
}
