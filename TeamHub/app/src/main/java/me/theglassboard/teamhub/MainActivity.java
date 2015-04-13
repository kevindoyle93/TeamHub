package me.theglassboard.teamhub;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity {

    private JSONArray teamsJson;
    private JSONArray fixturesJson;
    private int currentTeamPosition;
    private JSONObject currentTeam;
    private ArrayList<Team> teams;

    private Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        fragment = null;

        super.onCreate(savedInstanceState);

        /**
         *  Create an ObjectManager object to check if the user has already downloaded the information
         */
        String teamsFileName = "teamsJSONArray";
        String fixturesFileName = "fixturesJSONArray";
        String currentTeamFilename = "currentTeam";
        ObjectManager jsonManager = new ObjectManager(this);

        if(jsonManager.fileExists(teamsFileName)) {

            JSONParser parser = new JSONParser();

            Object obj;
            try {

                obj = jsonManager.readObject(teamsFileName);
                obj = parser.parse((String) obj);
                teamsJson = (JSONArray)obj;

                obj = parser.parse((String)jsonManager.readObject(fixturesFileName));
                fixturesJson = (JSONArray)obj;

                findCurrentTeam((String)jsonManager.readObject(currentTeamFilename));
            }
            catch (ParseException e) {

                Log.d("Exception", "Couldn't load files", e);
            }

        }
        else {
            // Run the LoadInfoActivity
            Intent loadInfo = new Intent(MainActivity.this, LoadInfo.class);
            startActivity(loadInfo);
        }

        setContentView(R.layout.home);
        setListeners();

        FragmentHome homeFragment = new FragmentHome();
        homeFragment.setCurrentTeam(currentTeam);
        homeFragment.setCurrentTeamPosition(currentTeamPosition);
        homeFragment.setFixturesJson(fixturesJson);
        homeFragment.setTeams(teams);
        homeFragment.setTeamsJson(teamsJson);

        switchContent(homeFragment);

        //makeTeams();
        // ((FragmentHome)fragment).readData();

    }

    public void setTextViews(TextView textView) {

        textView.setText("TEAM!");
        //textView.setText(teams.get(currentTeamPosition).getFixture(teams.get(currentTeamPosition).getLatestMatch()).getHomeTeam());
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

        //setTextViews();

        //teams.get(currentTeamPosition).setViews(this);

        // TODO: make the league table
        //createLeagueTable();
    }

    private void createLeagueTable() {

        final TableLayout leagueTable = (TableLayout)findViewById(R.id.Table);
        int teamsToDisplay = 3;

        // My edge cases are if the current team are first or last
        if(currentTeamPosition == 0) {

            for(int i = 0; i < teamsToDisplay; i++) {

                TableRow row = teams.get(i).getLeagueStats().setViews(this, teams.get(i).getClub());
                leagueTable.addView(row);
            }
        }
        else if(currentTeamPosition == teams.size() - 1) {

            for(int i = (currentTeamPosition - teamsToDisplay + 1); i < teams.size(); i++) {

                TableRow row = teams.get(i).getLeagueStats().setViews(this, teams.get(i).getClub());
                leagueTable.addView(row);
            }
        }
        else {

            for(int i = currentTeamPosition - 1; i < (currentTeamPosition - 1 + teamsToDisplay); i++) {

                TableRow row = teams.get(i).getLeagueStats().setViews(this, teams.get(i).getClub());
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

    private void setListeners() {

        final TextView homButton = (TextView)findViewById(R.id.homeButton);

        homButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                switchContent(new FragmentHome());
            }
        });

        TextView fixturesButton = (TextView)findViewById(R.id.fixturesButton);

        fixturesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                switchContent(new FragmentFixtures());

                // Change the highlighted TextView
                /*TextView textViewToChange = (TextView)findViewById(R.id.fixturesButton);
                Uri path = Uri.parse("android.resource://com.segf4ult.test/" + R.drawable.abc_cab_background_top_mtrl_alpha);
                Drawable underline = Drawable.createFromPath(path);
                textViewToChange.setBackground(underline);*/

            }
        });

        TextView leagueTablesButton = (TextView)findViewById(R.id.tableButton);

        leagueTablesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                //switchContent();
            }
        });
    }

    public void switchContent(Fragment fragment) {

        this.fragment = fragment;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.Sections, fragment)
                .commit();

        invalidateOptionsMenu();
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
