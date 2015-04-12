package me.theglassboard.teamhub;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;


public class Home extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

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

            makeTeams();
        }
        else {
            // Run the LoadInfoActivity
            Intent loadInfo = new Intent(Home.this, LoadInfo.class);
            startActivity(loadInfo);
        }
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

            Log.d("CURRENT TEAM NAME" , (String) (currentTeam.get("team")));

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

        teams.get(currentTeamPosition).setViews(this);

        // TODO: make the league table
        createLeagueTable();
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


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Home) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
