package me.theglassboard.teamhub;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;


public class MainActivity extends /*ActionBar*/FragmentActivity {

    private JSONArray teamsJson;
    private JSONArray fixturesJson;
    private int currentTeamPosition;
    private JSONObject currentTeam;
    private ArrayList<Team> teams;

    private Fragment fragment;
    private FragmentHome homeFragment;
    private FragmentFixtures fixturesFragment;
    private FragmentTable tableFragment;

    private ImageButton menuButton;


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

            setContentView(R.layout.home);
            setListeners();
            createPopupMenu();
            makeTeams();
            setTextView((TextView) findViewById(R.id.teamName), teams.get(currentTeamPosition).getClub());

            setUpHomeFragment();
            setUpFixturesFragment();
            setUpTableFragment();

            switchContent(homeFragment);

        }
        else {
            // Run the LoadInfoActivity
            Intent loadInfo = new Intent(MainActivity.this, LoadInfo.class);
            startActivity(loadInfo);
        }

    }

    private void setTextView(TextView textView, String text) {

        textView.setText(text);
    }

    private void setUpHomeFragment() {

        homeFragment = new FragmentHome();
        homeFragment.setCurrentTeam(currentTeam);
        homeFragment.setCurrentTeamPosition(currentTeamPosition);
        homeFragment.setFixturesJson(fixturesJson);
        homeFragment.setTeams(teams);
        homeFragment.setTeamsJson(teamsJson);
    }

    private void setUpFixturesFragment() {

        fixturesFragment = new FragmentFixtures();
        fixturesFragment.setTeam(teams.get(currentTeamPosition));
    }

    private void setUpTableFragment() {

        tableFragment = new FragmentTable();
        tableFragment.setTeams(teams);
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

        ImageButton homeButton = (ImageButton)findViewById(R.id.homeButton);

        homeButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                ImageButton thisButton;
                ImageButton oldButton;

                // Change the highlighted TextView
                thisButton = (ImageButton)findViewById(R.id.homeButton);

                if(fragment instanceof FragmentFixtures)
                    oldButton = (ImageButton)findViewById(R.id.fixturesButton);
                else
                    oldButton = (ImageButton)findViewById(R.id.tableButton);

                changeButtonColours(oldButton, thisButton);

                switchContent(homeFragment);
            }
        });

        ImageButton fixturesButton = (ImageButton)findViewById(R.id.fixturesButton);

        fixturesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                ImageButton thisButton;
                ImageButton oldButton;

                // Change the highlighted TextView
                thisButton = (ImageButton)findViewById(R.id.fixturesButton);

                if(fragment instanceof FragmentHome)
                    oldButton = (ImageButton)findViewById(R.id.homeButton);
                else
                    oldButton = (ImageButton)findViewById(R.id.tableButton);

                changeButtonColours(oldButton, thisButton);

                switchContent(fixturesFragment);
            }
        });

        ImageButton leagueTablesButton = (ImageButton)findViewById(R.id.tableButton);

        leagueTablesButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                ImageButton thisButton;
                ImageButton oldButton;

                // Change the highlighted TextView
                thisButton = (ImageButton) findViewById(R.id.tableButton);

                if (fragment instanceof FragmentHome)
                    oldButton = (ImageButton) findViewById(R.id.homeButton);
                else
                    oldButton = (ImageButton) findViewById(R.id.fixturesButton);

                changeButtonColours(oldButton, thisButton);

                switchContent(tableFragment);
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

    private void changeButtonColours(ImageButton oldFragment, ImageButton newFragment) {

        oldFragment.setBackground(null);
        newFragment.setBackground(getResources().getDrawable(R.drawable.abc_textfield_search_activated_mtrl_alpha));
    }

    private void createPopupMenu() {

        menuButton = (ImageButton)findViewById(R.id.menuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPopup();
            }
        });
    }

    private void doPopup() {

        PopupMenu popupMenu = new PopupMenu(this, menuButton);
        popupMenu.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();

                        if (id == R.id.action_settings) {

                            startActivity(new Intent(MainActivity.this, LoadInfo.class));
                            return true;
                        }

                        return false;
                    }
                }
        );

        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popupMenu.getMenu());
        popupMenu.show();
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

        if (id == R.id.action_settings) {

            startActivity(new Intent(this, LoadInfo.class));
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
