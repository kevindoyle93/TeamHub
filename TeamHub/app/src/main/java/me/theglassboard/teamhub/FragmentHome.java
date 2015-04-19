package me.theglassboard.teamhub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;


public class FragmentHome extends Fragment {

    // Variables to hold information to be used/displayed
    private JSONArray teamsJson;
    private JSONArray fixturesJson;
    private int currentTeamPosition;
    private JSONObject currentTeam;
    private ArrayList<Team> teams;

    private ObjectManager objectManager;


    public void setFixturesJson(JSONArray fixturesJson) {
        this.fixturesJson = fixturesJson;
        Log.d("FIXTURES JSON", this.fixturesJson.toString());
    }

    public void setTeamsJson(JSONArray teamsJson) {
        this.teamsJson = teamsJson;
        Log.d("TEAM JSON", this.teamsJson.toString());
    }

    public void setCurrentTeamPosition(int currentTeamPosition) {
        this.currentTeamPosition = currentTeamPosition;
        Log.d("CURRENT TEAM POS", this.currentTeam.toString());
    }

    public void setCurrentTeam(JSONObject currentTeam) {
        this.currentTeam = currentTeam;
        Log.d("CURRENT TEAM", this.currentTeam.toString());
    }

    public void setTeams(ArrayList<Team> teams) {
        this.teams = teams;
        Log.d("TEAMS", this.teams.toString());
    }


    // The Views that can be accessed/altered in this fragment
    private TextView fixtureHeadingView;
    private TextView homeTeamView;
    private TextView homeScoreView;
    private TextView versusView;
    private TextView awayScoreView;
    private TextView awayTeamView;
    private TextView locationAndDateView;
    private TableLayout leagueTableView;

    private Activity currentActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        objectManager = new ObjectManager(getActivity());

        /**
         * Inflate the layout for this fragment
         */

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Fixture f = teams.get(currentTeamPosition).getFixture(teams.get(currentTeamPosition).getLatestMatch());

        // Assign the Views
        homeTeamView = (TextView)view.findViewById(R.id.homeTeam);
        homeTeamView.isClickable();
        setTeamListener(homeTeamView, f.getHomeTeam());
        setView(homeTeamView, f.getHomeTeam());

        if (f.getHomeScore() != null) {

            homeScoreView = (TextView) view.findViewById(R.id.homeScore);
            setView(homeScoreView, f.getHomeScore());

            awayScoreView = (TextView) view.findViewById(R.id.awayScore);
            setView(awayScoreView, f.getAwayScore());

            fixtureHeadingView = (TextView) view.findViewById(R.id.resultsAndFixtures);
            setView(fixtureHeadingView, "Latest Result");
        }
        else  {

            fixtureHeadingView = (TextView) view.findViewById(R.id.resultsAndFixtures);
            setView(fixtureHeadingView, "Next Fixture");
        }

        versusView = (TextView) view.findViewById((R.id.versus));
        setView(versusView, "-");

        locationAndDateView = (TextView)view.findViewById(R.id.locationAndTime);
        setView(locationAndDateView, f.getLocation() + ", " + f.getDate() + ", " + f.getTime());

        awayTeamView = (TextView)view.findViewById(R.id.awayTeam);
        awayTeamView.isClickable();
        setTeamListener(awayTeamView, f.getAwayTeam());
        setView(awayTeamView, f.getAwayTeam());


        // Make the league table
        leagueTableView = (TableLayout)view.findViewById(R.id.Table);
        createLeagueTable(leagueTableView);

        return view;
    }

    public void makeTeams() {

        teams = new ArrayList<>();


        for(int i = 0; i < teamsJson.size(); i++) {

            LeagueStats stats;

            currentTeam = (JSONObject)teamsJson.get(i);

            String teamName = (String) (currentTeam.get("team"));
            String teamAgeGroup = "";
            String teamHomePitch = "";
            String teamManager = "";

            stats = new LeagueStats(String.valueOf(i + 1),
                    (String) currentTeam.get("played"),
                    (String) currentTeam.get("won"),
                    (String) currentTeam.get("drawn"),
                    (String) currentTeam.get("lost"),
                    (String) currentTeam.get("goals_for"),
                    (String) currentTeam.get("goals_against"),
                    (String) currentTeam.get("goal_difference"),
                    (String) currentTeam.get("points"));

            teams.add(new Team(teamName, /* teamAgeGroup, teamHomePitch, teamManager, */stats));
        }

        for(int i = 0; i < fixturesJson.size(); i++) {

            JSONObject fixture = (JSONObject)fixturesJson.get(i);

            Fixture f = new Fixture((String)fixture.get("home_team"),
                    (String)fixture.get("away_team"),
                    (String)fixture.get("home_score"),
                    (String)fixture.get("away_score"),
                    (String)fixture.get("pitch/_text"),
                    splitDateAndTime((String)fixture.get("date_and_time"), "date"),
                    splitDateAndTime((String)fixture.get("date_and_time"), "time"),
                    (String)fixture.get("referee")
            );

            if(f.getTime() != null && (f.getHomeTeam().equalsIgnoreCase(teams.get(currentTeamPosition).getClub()) || f.getAwayTeam().equalsIgnoreCase(teams.get(currentTeamPosition).getClub()))) {

                teams.get(currentTeamPosition).addToFixtures(f);
            }
        }
    }

    public TextView getHomeTeamView() { return homeTeamView; }

    private void setView(TextView textView, String text) {

        textView.setText(text);
    }

    private void createLeagueTable(TableLayout leagueTable) {

        int teamsToDisplay = 3;

        // My edge cases are if the current team are first or last
        if(currentTeamPosition == 0) {

            for(int i = 0; i < teamsToDisplay; i++) {

                TableRow row = teams.get(i).getLeagueStats().setViews(getActivity(), teams.get(i).getClub());
                setTeamListener(row, teams.get(i).getClub());
                leagueTable.addView(row);
            }
        }
        else if(currentTeamPosition == teams.size() - 1) {

            for(int i = (currentTeamPosition - teamsToDisplay + 1); i < teams.size(); i++) {

                TableRow row = teams.get(i).getLeagueStats().setViews(getActivity(), teams.get(i).getClub());
                setTeamListener(row, teams.get(i).getClub());
                leagueTable.addView(row);
            }
        }
        else {

            for(int i = currentTeamPosition - 1; i < (currentTeamPosition - 1 + teamsToDisplay); i++) {

                TableRow row = teams.get(i).getLeagueStats().setViews(getActivity(), teams.get(i).getClub());
                setTeamListener(row, teams.get(i).getClub());
                leagueTable.addView(row);
            }
        }
    }

    private String splitDateAndTime(String whole, String split) {

        String ret;
        int lengthOfDate = 8;

        if(whole == null)
            return null;

        if(split.equals("date")) {

            if(whole.length() == lengthOfDate)
                return whole;

            else
                ret = whole.substring(0, whole.indexOf(' '));

            return  ret;
        }
        else if(split.equals("time") && whole.length() > lengthOfDate) {

            ret = whole.substring(whole.indexOf(' ') + 1);

            return ret;
        }
        else
            return null;
    }

    private void findCurrentTeam(String searchKey) {

        for(int i = 0; i < teamsJson.size(); i++) {

            JSONObject object = (JSONObject)teamsJson.get(i);
            if(object.get("team").equals(searchKey)) {

                currentTeamPosition = i;

                return;
            }
        }
    }

    public void setCurrentActivity(Activity activity) {

        currentActivity = activity;

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

    private void setTeamListener(TableRow teamView, final String teamName) {

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
