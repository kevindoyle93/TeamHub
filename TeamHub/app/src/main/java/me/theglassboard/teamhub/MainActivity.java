package me.theglassboard.teamhub;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.simple.JSONObject;


public class MainActivity extends ActionBarActivity {

    DataFetcher dataFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // String leagueTableURL = "https://api.import.io/store/data/a1bfaa9f-6143-499a-a00f-d559e10c5de1/_query?input/webpage/url=http%3A%2F%2Faul.comortais.com%2Fcompetition.aspx%3Fid%3D1060&_user=f263fc1d-792b-4302-a562-a993a22d1c65&_apikey=kMgmJWavxVUJZsXcD4uLxN7rBlgy5%2BeFllVwXUa3RIit6tSAEH2CwxWxc4C%2BtlMF%2B9ait%2BgnNz9dAqoI%2BKDKEw%3D%3D";
        // String fixturesURL = "https://api.import.io/store/data/1eacba70-90fe-4225-8c11-ea3cc2c27112/_query?input/webpage/url=http%3A%2F%2Faul.comortais.com%2Ffixtures.aspx%3FteamID%3D2111%26compId%3D1060&_user=f263fc1d-792b-4302-a562-a993a22d1c65&_apikey=kMgmJWavxVUJZsXcD4uLxN7rBlgy5%2BeFllVwXUa3RIit6tSAEH2CwxWxc4C%2BtlMF%2B9ait%2BgnNz9dAqoI%2BKDKEw%3D%3D";

        Intent thisIntent = getIntent();
        teamPosition = thisIntent.getIntExtra("teamToGet", 0);

        dataFetcher = new DataFetcher(this);
        dataFetcher.execute();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
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
        JSONObject secondTeamJson = dataFetcher.getTeamInfo(secondLeagueTeamPosition);
        JSONObject thirdTeamJson = dataFetcher.getTeamInfo(thirdLeagueTeamPosition);

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

        return super.onOptionsItemSelected(item);
    }
}
