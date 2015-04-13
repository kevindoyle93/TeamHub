package me.theglassboard.teamhub;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.TableRow;
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
    public TableRow setViews(Activity myActivity, String club) {

        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        TableRow.LayoutParams statsParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        statsParams.setMargins(5, 5, 5, 5);

        // TODO: Create the table cells for the stats. Make an if(myActivity instanceof LeagueTable) to return more information
        TableRow row = new TableRow(myActivity);
        row.setLayoutParams(rowParams);

        final TextView positionTextView;
        final TextView clubTextView;
        final TextView pointsTextView;
        final TextView playedTextView;

        positionTextView = new TextView(myActivity);
        positionTextView.setText(stats[0] + ".");
        positionTextView.setLayoutParams(statsParams);
        positionTextView.setTextColor(Color.DKGRAY);
        positionTextView.setGravity(Gravity.CENTER);

        positionTextView.setPadding(10, 0, 0, 0);
        row.addView(positionTextView);

        clubTextView = new TextView(myActivity);
        clubTextView.setText(club);
        clubTextView.setLayoutParams(statsParams);
        clubTextView.setTextColor(Color.DKGRAY);
        clubTextView.setGravity(Gravity.LEFT);
        row.addView(clubTextView);

        playedTextView = new TextView(myActivity);
        playedTextView.setText(stats[1]);
        playedTextView.setLayoutParams(statsParams);
        playedTextView.setTextColor(Color.DKGRAY);
        playedTextView.setGravity(Gravity.CENTER);
        row.addView(playedTextView);

        pointsTextView = new TextView(myActivity);
        pointsTextView.setText(stats[8]);
        pointsTextView.setLayoutParams(statsParams);
        pointsTextView.setTextColor(Color.DKGRAY);
        pointsTextView.setGravity(Gravity.CENTER);
        pointsTextView.setPadding(0, 0, 0, 10);
        row.addView(pointsTextView);

        return row;
    }

    // Set the textViews for the league table section of home.xml
    public TableRow setAllViews(Activity myActivity, String club) {

        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        TableRow.LayoutParams statsParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        statsParams.setMargins(5, 5, 5, 5);

        // TODO: Create the table cells for the stats. Make an if(myActivity instanceof LeagueTable) to return more information
        TableRow row = new TableRow(myActivity);
        row.setLayoutParams(rowParams);

        final TextView positionTextView;
        final TextView clubTextView;
        final TextView pointsTextView;
        final TextView wonTextView;
        final TextView drawnTextView;
        final TextView lostTextView;
        final TextView playedTextView;


        positionTextView = new TextView(myActivity);
        positionTextView.setText(stats[0] + ".");
        positionTextView.setLayoutParams(statsParams);
        positionTextView.setTextColor(Color.DKGRAY);
        positionTextView.setGravity(Gravity.CENTER);

        positionTextView.setPadding(10, 0, 0, 0);
        row.addView(positionTextView);

        clubTextView = new TextView(myActivity);
        clubTextView.setText(club);
        clubTextView.setLayoutParams(statsParams);
        clubTextView.setTextColor(Color.DKGRAY);
        clubTextView.setGravity(Gravity.LEFT);
        row.addView(clubTextView);

        playedTextView = new TextView(myActivity);
        playedTextView.setText(stats[1]);
        playedTextView.setLayoutParams(statsParams);
        playedTextView.setTextColor(Color.DKGRAY);
        playedTextView.setGravity(Gravity.CENTER);
        row.addView(playedTextView);

        wonTextView = new TextView(myActivity);
        wonTextView.setText(stats[2]);
        wonTextView.setLayoutParams(statsParams);
        wonTextView.setTextColor(Color.DKGRAY);
        wonTextView.setGravity(Gravity.CENTER);
        row.addView(wonTextView);

        drawnTextView = new TextView(myActivity);
        drawnTextView.setText(stats[2]);
        drawnTextView.setLayoutParams(statsParams);
        drawnTextView.setTextColor(Color.DKGRAY);
        drawnTextView.setGravity(Gravity.CENTER);
        row.addView(drawnTextView);

        lostTextView = new TextView(myActivity);
        lostTextView.setText(stats[2]);
        lostTextView.setLayoutParams(statsParams);
        lostTextView.setTextColor(Color.DKGRAY);
        lostTextView.setGravity(Gravity.CENTER);
        row.addView(lostTextView);

        pointsTextView = new TextView(myActivity);
        pointsTextView.setText(stats[8]);
        pointsTextView.setLayoutParams(statsParams);
        pointsTextView.setTextColor(Color.DKGRAY);
        pointsTextView.setGravity(Gravity.CENTER);
        pointsTextView.setPadding(0, 0, 0, 10);
        row.addView(pointsTextView);

        return row;
    }
}
