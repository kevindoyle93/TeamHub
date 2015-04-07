package me.theglassboard.teamhub;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;

import org.json.simple.JSONObject;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String leagueTableURL = "https://api.import.io/store/data/a1bfaa9f-6143-499a-a00f-d559e10c5de1/_query?input/webpage/url=http%3A%2F%2Faul.comortais.com%2Fcompetition.aspx%3Fid%3D1060&_user=f263fc1d-792b-4302-a562-a993a22d1c65&_apikey=kMgmJWavxVUJZsXcD4uLxN7rBlgy5%2BeFllVwXUa3RIit6tSAEH2CwxWxc4C%2BtlMF%2B9ait%2BgnNz9dAqoI%2BKDKEw%3D%3D";

        DataFetcher dataFetcher = new DataFetcher(this);
        dataFetcher.execute(leagueTableURL);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
    }


    private JSONObject teamJson;

    public void setJson(JSONObject team) {

        teamJson = team;
        makeTeam();
    }


    private Team team;
    private LeagueStats stats;

    private void makeTeam() {

        String teamName = (String)(teamJson.get("team"));
        String teamAgeGroup = "Senior";
        String teamHomePitch = "Edenmore Crescent";
        String teamManager = "Ciarán O'Driscoll";

        stats = new LeagueStats((String)teamJson.get("played"),
                                (String)teamJson.get("won"),
                                (String)teamJson.get("drawn"),
                                (String)teamJson.get("lost"),
                                (String)teamJson.get("goals_for"),
                                (String)teamJson.get("goals_against"),
                                (String)teamJson.get("goal_difference"),
                                (String)teamJson.get("points"));

        team = new Team(teamName, teamAgeGroup, teamHomePitch, teamManager, stats);

        team.setViews(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /* Hopefully this gets rid of the three dots menu
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
    */
}
