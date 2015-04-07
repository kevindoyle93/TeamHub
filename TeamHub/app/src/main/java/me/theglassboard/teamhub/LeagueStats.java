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

    public LeagueStats(String played, String won, String drawn, String lost, String goalsFor, String goalsAgainst, String goalDifference, String points) {

        stats = new String[8];

        stats[0] = played;
        stats[1] = won;
        stats[2] = drawn;
        stats[3] = lost;
        stats[4] = goalsFor;
        stats[5] = goalsAgainst;
        stats[6] = goalDifference;
        stats[7] = points;
    }

    public String getStat(int i) {

        return stats[i];
    }


    // Set the textViews for the league table section of home.xml
    public void setViews(Activity myActivity) {

        final TextView pointsTextView;
        final TextView playedTextView;

        pointsTextView = (TextView)myActivity.findViewById(R.id.points0);
        pointsTextView.setText(stats[7]);

        playedTextView= (TextView)myActivity.findViewById(R.id.played0);
        playedTextView.setText(stats[0]);

    }
}
