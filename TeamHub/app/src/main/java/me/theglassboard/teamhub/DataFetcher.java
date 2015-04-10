package me.theglassboard.teamhub;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;


/**
 * Created by Kevin on 03/04/2015.
 */
public class DataFetcher extends AsyncTask<String, Void, Void> {

    //public MainActivity myActivity;
    public Activity myActivity;

    private String competitionID;
    private String leagueURL;
    private String fixtureURL;

    private String json;
    private JSONArray teamsArray;
    private ArrayList<JSONObject> teamObjects;
    private JSONArray fixturesArray;
    private ArrayList<JSONObject> fixtureObjects;

    private String leagueTableJson;
    private String fixturesJson;


    /* Public Accessors */
    public void setCompetitionID(String id) { competitionID = id; }

    public ArrayList<JSONObject> getAllTeamObjects() {

        if(teamObjects.size() > 0)
            return  teamObjects;

        else
            return  null;
    }

    public JSONObject getTeamInfo(int i) { return teamObjects.get(i); }

    public int getNumberOfTeams() { return teamObjects.size(); }

    public JSONObject getFixtureInfo(int i) { return fixtureObjects.get(i); }

    public int getFixtureInfoSize() { return fixturesArray.size(); }


    public DataFetcher(Activity myActivity) {

        this.myActivity = myActivity;

        teamObjects = new ArrayList<>();
        fixtureObjects = new ArrayList<>();

        // 1052 means the app will default to Premier A
        competitionID = "1052";
    }

    protected Void doInBackground(String... urls) {

        try {

            leagueURL = "https://api.import.io/store/data/a1bfaa9f-6143-499a-a00f-d559e10c5de1/_query?input/webpage/url=http%3A%2F%2Faul.comortais.com%2Fcompetition.aspx%3Fid%3D" + competitionID + "&_user=f263fc1d-792b-4302-a562-a993a22d1c65&_apikey=kMgmJWavxVUJZsXcD4uLxN7rBlgy5%2BeFllVwXUa3RIit6tSAEH2CwxWxc4C%2BtlMF%2B9ait%2BgnNz9dAqoI%2BKDKEw%3D%3D";
            fixtureURL = "https://api.import.io/store/data/1eacba70-90fe-4225-8c11-ea3cc2c27112/_query?input/webpage/url=http%3A%2F%2Faul.comortais.com%2FFixtures.aspx%3Fcompid%3D" + competitionID + "&_user=f263fc1d-792b-4302-a562-a993a22d1c65&_apikey=kMgmJWavxVUJZsXcD4uLxN7rBlgy5%2BeFllVwXUa3RIit6tSAEH2CwxWxc4C%2BtlMF%2B9ait%2BgnNz9dAqoI%2BKDKEw%3D%3D";

            leagueTableJson = Jsoup.connect(leagueURL).ignoreContentType(true).execute().body();
            fixturesJson = Jsoup.connect(fixtureURL).ignoreContentType(true).execute().body();

            int leagueTableJsonStart = leagueTableJson.indexOf('[');
            int leagueTableJsonEnd = leagueTableJson.indexOf(']', leagueTableJsonStart + 1) + 1;
            leagueTableJson = leagueTableJson.substring(leagueTableJsonStart, leagueTableJsonEnd);

            int fixturesJsonStart = fixturesJson.indexOf('[');
            int fixturesJsonEnd = fixturesJson.indexOf(']', fixturesJsonStart + 1) + 1;
            fixturesJson = fixturesJson.substring(fixturesJsonStart, fixturesJsonEnd);

            return null;

        } catch (IOException e) {
            // e.printStackTrace();
            Log.d("EXCEPTION: ", "Getting JSON didn't work.");

            return null;
        }

    }

    @Override
    protected void onPostExecute(Void v) {

        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(leagueTableJson);
            teamsArray = (JSONArray)obj;

            for(int i = 0; i < teamsArray.size(); i++) {

                teamObjects.add((JSONObject)teamsArray.get(i));
            }

            obj = parser.parse(fixturesJson);
            fixturesArray = (JSONArray)obj;

            for(int i = 0; i < fixturesArray.size(); i++) {

                fixtureObjects.add((JSONObject)fixturesArray.get(i));
            }

            // Try to set the MainActivity JSONObject
            // myActivity.makeTeam();
            if(myActivity instanceof LoadInfo) {

                ((LoadInfo) myActivity).setTeamsArray(teamObjects);
                ((LoadInfo) myActivity).setFixtures(fixturesJson);
            }

        } catch(ParseException pe) {

            System.out.println("position: " + pe.getPosition());
            System.out.println(pe);
        }

        // TODO: Do all assigning/creating of teams, fixtures, etc.
    }


}
