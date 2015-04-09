package me.theglassboard.teamhub;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import org.json.simple.JSONObject;


public class MainActivity extends ActionBarActivity {

    DataFetcher dataFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {

            Log.d("SAVED INSTANCE STATE", "NOT NULL");
        }
        else {

            Intent thisIntent = getIntent();
            teamPosition = thisIntent.getIntExtra("teamToGet", 0);

            dataFetcher = new DataFetcher(this);
            dataFetcher.execute();

            setContentView(R.layout.home);
        }
    }


    private JSONObject teamJson;

    public void setJson(JSONObject team) {

        teamJson = team;
        makeTeam();
    }


    private Team team;
    private int teamPosition;

    public void makeTeam() {

        LeagueStats stats;

        // This would change depending on the team selected by the user
        // teamPosition = 6;

        int secondLeagueTeamPosition;
        int thirdLeagueTeamPosition;

        secondLeagueTeamPosition = (teamPosition > 0) ? teamPosition - 1 : teamPosition + 1;
        thirdLeagueTeamPosition = (teamPosition > 0) ? teamPosition + 1 : teamPosition + 2;

        teamJson = dataFetcher.getTeamInfo(teamPosition);
        // JSONObject secondTeamJson = dataFetcher.getTeamInfo(secondLeagueTeamPosition);
        // JSONObject thirdTeamJson = dataFetcher.getTeamInfo(thirdLeagueTeamPosition);

        String teamName = (String)(teamJson.get("team"));
        String teamAgeGroup = "Senior";
        String teamHomePitch = "Edenmore Crescent";
        String teamManager = "Ciarán O'Driscoll";

        stats = new LeagueStats(String.valueOf(teamPosition + 1),
                                (String)teamJson.get("played"),
                                (String)teamJson.get("won"),
                                (String)teamJson.get("drawn"),
                                (String)teamJson.get("lost"),
                                (String)teamJson.get("goals_for"),
                                (String)teamJson.get("goals_against"),
                                (String)teamJson.get("goal_difference"),
                                (String)teamJson.get("points"));

        team = new Team(teamName, teamAgeGroup, teamHomePitch, teamManager, stats);

        /*stats = new LeagueStats(String.valueOf(teamPosition + 1),
                (String)secondTeamJson.get("played"),
                (String)secondTeamJson.get("won"),
                (String)secondTeamJson.get("drawn"),
                (String)secondTeamJson.get("lost"),
                (String)secondTeamJson.get("goals_for"),
                (String)secondTeamJson.get("goals_against"),
                (String)secondTeamJson.get("goal_difference"),
                (String)secondTeamJson.get("points"));

        teamName = (String)secondTeamJson.get("team");
        teamAgeGroup = null;
        teamHomePitch = null;
        teamManager = null;

        Team secondTeam = new Team(teamName, teamAgeGroup, teamHomePitch, teamManager, stats);

        stats = new LeagueStats(String.valueOf(teamPosition + 1),
                (String)thirdTeamJson.get("played"),
                (String)thirdTeamJson.get("won"),
                (String)thirdTeamJson.get("drawn"),
                (String)thirdTeamJson.get("lost"),
                (String)thirdTeamJson.get("goals_for"),
                (String)thirdTeamJson.get("goals_against"),
                (String)thirdTeamJson.get("goal_difference"),
                (String)thirdTeamJson.get("points"));

        teamName = (String)thirdTeamJson.get("team");
        teamAgeGroup = null;
        teamHomePitch = null;
        teamManager = null;

        Team thirdTeam = new Team(teamName, teamAgeGroup, teamHomePitch, teamManager, stats);*/


        for(int i = 0; i < dataFetcher.getFixtureInfoSize(); i++) {

            JSONObject fixturesJson = dataFetcher.getFixtureInfo(i);

            Fixture f = new Fixture((String)fixturesJson.get("home_team"),
                                    (String)fixturesJson.get("away_team"),
                                    (String)fixturesJson.get("home_score"),
                                    (String)fixturesJson.get("away_score"),
                                    (String)fixturesJson.get("pitch/_text"),
                                    (String)fixturesJson.get("date_and_time"),
                                    (String)fixturesJson.get("referee")
                    );

            if(f.getTime() != null && (f.getHomeTeam().equalsIgnoreCase(team.getClub()) || f.getAwayTeam().equalsIgnoreCase(team.getClub()))) {

                team.addToFixtures(f);
            }
        }

        team.setViews(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            startActivity(new Intent(this, LoadInfo.class));
        }

        if(id == R.id.refresh_settings) {

            Log.d("Next team", "" + (teamPosition + 1) % dataFetcher.getNumberOfTeams());
            teamPosition = (teamPosition + 1) % dataFetcher.getNumberOfTeams();

            dataFetcher = new DataFetcher(this);
            dataFetcher.execute();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        // savedInstanceState.putDataFetche;
        // savedInstanceState.putInt(STATE_LEVEL, mCurrentLevel);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
}
