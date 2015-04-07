package me.theglassboard.teamhub;

import java.io.IOException;
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
public class DataFetcher extends AsyncTask<String, Void, String> {

    public String awayTeam = "Howth Celtic";
    public MainActivity myActivity;

    private String json;


    public DataFetcher(MainActivity myActivity) {

        this.myActivity = myActivity;
    }

    protected String doInBackground(String... urls) {

        String jsonWhole;

        try {

            String leagueTableURL = "https://api.import.io/store/data/a1bfaa9f-6143-499a-a00f-d559e10c5de1/_query?input/webpage/url=http%3A%2F%2Faul.comortais.com%2Fcompetition.aspx%3Fid%3D1060&_user=f263fc1d-792b-4302-a562-a993a22d1c65&_apikey=kMgmJWavxVUJZsXcD4uLxN7rBlgy5%2BeFllVwXUa3RIit6tSAEH2CwxWxc4C%2BtlMF%2B9ait%2BgnNz9dAqoI%2BKDKEw%3D%3D";
            jsonWhole = Jsoup.connect(leagueTableURL).ignoreContentType(true).execute().body();

            int jsonStart = jsonWhole.indexOf('[');
            int jsonEnd = jsonWhole.indexOf(']', jsonStart + 1) + 1;

            jsonWhole = jsonWhole.substring(jsonStart, jsonEnd);

            Log.d("JSON: ", jsonWhole);

            return jsonWhole;

        } catch (IOException e) {
            // e.printStackTrace();
            Log.d("EXCEPTION: ", "Getting JSON didn't work.");

            return null;
        }

    }

    @Override
    protected void onPostExecute(String jsonWhole) {

        json = jsonWhole;

        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(json);
            JSONArray array = (JSONArray)obj;

            JSONObject team1 = (JSONObject)array.get(0);

            // Try to set the MainActivity JSONObject
            myActivity.setJson(team1);

        } catch(ParseException pe) {

            System.out.println("position: " + pe.getPosition());
            System.out.println(pe);
        }

        // TODO: Do all assigning/creating of teams, fixtures, etc.
    }

    protected void setAwayTeam() {

        JSONParser parser = new JSONParser();

        final TextView textViewToChange;
        textViewToChange = (TextView)myActivity.findViewById(R.id.awayTeam);

        try {
            Object obj = parser.parse(json);
            JSONArray array = (JSONArray)obj;

            JSONObject team1 = (JSONObject)array.get(0);

            // Try to set the MainActivity JSONObject
            myActivity.setJson(team1);

            String awayTeam = (String)(team1.get("team"));
            // textViewToChange.setText(awayTeam);

        } catch(ParseException pe) {

            System.out.println("position: " + pe.getPosition());
            System.out.println(pe);
        }
    }
}
