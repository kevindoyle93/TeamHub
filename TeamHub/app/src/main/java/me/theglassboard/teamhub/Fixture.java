package me.theglassboard.teamhub;

import android.app.Activity;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Kevin on 06/04/2015.
 */
public class Fixture {

    private String homeTeam;
    private String awayTeam;
    private String homeScore;
    private String awayScore;
    private String location;
    private String time;
    private String date;
    private String referee;

    private Date dateFormat;

    public Fixture() {

    }

    public Fixture(String homeTeam, String awayTeam, String homeScore, String awayScore, String location, String date, String time, String referee) {

        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.location = location;
        this.time = time;
        this.referee = referee;

        DateFormat originalFormat = new SimpleDateFormat("dd/MM/yy", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("EEE d MMM");

        if(date != null) {

            try {

                dateFormat = originalFormat.parse(date);
                this.date = targetFormat.format(dateFormat);

            } catch (ParseException e) {

                e.printStackTrace();
            }
        }
    }


    // Public Getters
    public String getHomeTeam() {
        return homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public String getAwayScore() { return awayScore; }

    public String getHomeScore() { return homeScore; }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
    }

    public String getReferee() {
        return referee;
    }

    // Set the textViews for the league results and fixtures section of home.xml
    public void setViews(Activity myActivity) {

        TextView textViewToChange = (TextView) myActivity.findViewById(R.id.homeTeam);
        textViewToChange.setText(homeTeam);

        if (homeScore != null) {

            textViewToChange = (TextView) myActivity.findViewById(R.id.homeScore);
            textViewToChange.setText(homeScore);

            textViewToChange = (TextView) myActivity.findViewById(R.id.awayScore);
            textViewToChange.setText(awayScore);

            textViewToChange = (TextView) myActivity.findViewById(R.id.resultsAndFixtures);
            textViewToChange.setText("Latest Result");
        }
        else  {

            textViewToChange = (TextView) myActivity.findViewById(R.id.resultsAndFixtures);
            textViewToChange.setText("Next Fixture");
        }

        textViewToChange = (TextView) myActivity.findViewById((R.id.versus));
        textViewToChange.setText("-");

        textViewToChange = (TextView)myActivity.findViewById(R.id.awayTeam);
        textViewToChange.setText(awayTeam);

        textViewToChange = (TextView)myActivity.findViewById(R.id.locationAndTime);
        textViewToChange.setText(location + ", " + date + " " + time);

    }
}
