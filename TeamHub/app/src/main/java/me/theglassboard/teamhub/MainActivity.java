package me.theglassboard.teamhub;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {

            Log.d("SAVED INSTANCE STATE", "NOT NULL");
        }
        else {

            Log.d("SAVED INSTANCE STATE", "NULL");

            JSONParser parser = new JSONParser();

            try {

                Object obj = parser.parse(getIntent().getStringExtra("teamJSON"));
                teamsJson = (JSONArray)obj;

                obj = parser.parse(getIntent().getStringExtra("fixturesJSON"));
                fixturesJson = (JSONArray)obj;

                currentTeamPosition = getIntent().getIntExtra("teamPosition", 0);
            }
            catch(ParseException pe) {

                System.out.println("position: " + pe.getPosition());
                System.out.println(pe);
            }
        }

        setContentView(R.layout.home);
        makeTeams();
    }


    private JSONArray teamsJson;
    private JSONArray fixturesJson;

    private int currentTeamPosition;
    private JSONObject currentTeam;

    private ArrayList<Team> teams;

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

            teams.add(new Team(teamName, teamAgeGroup, teamHomePitch, teamManager, stats));
        }


        for(int i = 0; i < fixturesJson.size(); i++) {

            JSONObject fixture = (JSONObject)fixturesJson.get(i);

            Fixture f = new Fixture((String)fixture.get("home_team"),
                                    (String)fixture.get("away_team"),
                                    (String)fixture.get("home_score"),
                                    (String)fixture.get("away_score"),
                                    (String)fixture.get("pitch/_text"),
                                    (String)fixture.get("date_and_time"),
                                    (String)fixture.get("referee")
                    );

            if(f.getTime() != null && (f.getHomeTeam().equalsIgnoreCase(teams.get(currentTeamPosition).getClub()) || f.getAwayTeam().equalsIgnoreCase(teams.get(currentTeamPosition).getClub()))) {

                teams.get(currentTeamPosition).addToFixtures(f);
            }
        }

        teams.get(currentTeamPosition).setViews(this);
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

            /*Log.d("Next team", "" + (teamPosition + 1) % dataFetcher.getNumberOfTeams());
            teamPosition = (teamPosition + 1) % dataFetcher.getNumberOfTeams();

            dataFetcher = new DataFetcher(this);
            dataFetcher.execute();*/
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
