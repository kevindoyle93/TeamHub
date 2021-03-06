package me.theglassboard.teamhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;


public class LoadInfo extends ActionBarActivity {

    private class Division {

        String name;
        String id;

        Division(String name, String id) {

            this.name = name;
            this.id = id;
        }

        String getName() { return name; }
        String getId() { return id; }
    }

    private Division[] divisions;
    private DataFetcher dataFetcher;

    private String fixtures;    // String representation of the JSONObject used in MainActivity

    // Try using an array list of JSONArray to add the possibility of more leagues being loaded
    private ArrayList<JSONArray> divisionsJson;

    private JSONArray teamsArray;
    private int divisionChoice;
    private int teamChoice;
    private boolean divisionChosen;
    private boolean teamsLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_info);

        // Create and populate the divisions array
        divisions = new Division[13];
        divisions[0] = new Division("Premier A", "1052");
        divisions[1] = new Division("Premier B", "1053");
        divisions[2] = new Division("Premier C", "1054");
        divisions[3] = new Division("Senior Saturday", "1057");
        divisions[4] = new Division("Division 1 Saturday", "1058");
        divisions[5] = new Division("Division 1A Saturday", "1059");
        divisions[6] = new Division("Division 2 Saturday", "1060");
        divisions[7] = new Division("Division 2A Saturday", "1061");
        divisions[8] = new Division("Division 3 Saturday", "1062");
        divisions[9] = new Division("Division 3A Saturday", "1063");
        divisions[10] = new Division("Senior Sunday", "1051");
        divisions[11] = new Division("Division 1 Sunday", "1055");
        divisions[12] = new Division("Division 3 Sunday", "1056");

        divisionsJson = new ArrayList<>();
        divisionChoice = -1;
        checkForExistingFiles();

        // List the available divisions
        final Spinner divisionDropdown = (Spinner)findViewById(R.id.divisionsDropdown);
        final String[] items = new String[]{

                "Press here to choose",
                divisions[0].getName(),
                divisions[1].getName(),
                divisions[2].getName(),
                divisions[3].getName(),
                divisions[4].getName(),
                divisions[5].getName(),
                divisions[6].getName(),
                divisions[7].getName(),
                divisions[8].getName(),
                divisions[9].getName(),
                divisions[10].getName(),
                divisions[11].getName(),
                divisions[12].getName()
        };
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        divisionDropdown.setAdapter(adapter2);

        divisionDropdown.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        int position = divisionDropdown.getSelectedItemPosition() - 1;

                        if(position >= 0) {

                            fetchData(divisions[position].getId());

                            Toast fetchingTeams = Toast.makeText(getApplicationContext(), "Fetching teams in: " + divisions[position].getName(), Toast.LENGTH_SHORT);
                            fetchingTeams.show();

                            divisionChosen = true;
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                }
        );

        // This spinner will list the teams in the selected division
        // once this is chosen. The default teamChoice will be 0.
        teamChoice = 0;

        final Spinner teamDropdown = (Spinner) findViewById(R.id.teamsDropdown);

        if(divisionsJson.size() == 0) {

            // final Spinner teamDropdown = (Spinner) findViewById(R.id.teamsDropdown);
            ArrayList<String> items2 = new ArrayList<>();
            items2.add("Choose your division first");
            adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
            teamDropdown.setAdapter(adapter2);
        }
        else {

            makeTeamDropdown();

        }

        Button btn = (Button)findViewById(R.id.open_activity_button);

        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                if(teamsLoaded) {

                    saveData();

                    Intent mainScreen = new Intent(LoadInfo.this, MainActivity.class);
                    startActivity(mainScreen);
                }
                else if(divisionChosen){

                    Toast notReady = Toast.makeText(getApplicationContext(), "Still downloading...", Toast.LENGTH_SHORT);
                    notReady.show();
                }
                else{

                    Toast notReady = Toast.makeText(getApplicationContext(), "Select a division", Toast.LENGTH_SHORT);
                    notReady.show();
                }
            }
        });
    }

    private void fetchData(String competitionID) {

        dataFetcher = new DataFetcher(this);
        dataFetcher.setCompetitionID(competitionID);
        dataFetcher.execute();
    }

    private void saveData() {

        ObjectManager objectManager = new ObjectManager(LoadInfo.this);

        String[] fileNames = {"teamsJSONArray", "fixturesJSONArray", "currentTeam"};
        String currentTeam = (String)((JSONObject)divisionsJson.get(divisionChoice).get(teamChoice)).get("team");

        objectManager.saveObject(divisionsJson.get(divisionChoice).toString(), fileNames[0]);
        objectManager.saveObject(fixtures, fileNames[1]);
        objectManager.saveObject(currentTeam, fileNames[2]);
    }

    private void checkForExistingFiles() {

        ObjectManager objectManager = new ObjectManager(this);

        String[] fileNames = {"teamsJSONArray", "fixturesJSONArray"};

        if(!objectManager.fileExists(fileNames[0])) {

            return;
        }

        JSONParser parser = new JSONParser();

        try {

            Object obj = objectManager.readObject(fileNames[0]);
            addDivisionToArray((JSONArray) parser.parse((String)obj));

            obj = objectManager.readObject(fileNames[1]);
            setFixtures((String)obj);

        } catch (ParseException e) {

            Log.d("ERROR", "LOADING PREVIOUS FILES");
        }


    }

    public void setFixtures(String fixtures) { this.fixtures = fixtures; }

    public void setTeamsArray(ArrayList<JSONObject> teams) {

        teamsArray = new JSONArray();

        for(int i = 0; i < teams.size(); i++) {

            teamsArray.add(teams.get(i));
        }

    }

    public void addDivisionToArray(ArrayList<JSONObject> teams) {

        teamsArray = new JSONArray();

        for(int i = 0; i < teams.size(); i++)
            teamsArray.add(teams.get(i));

        divisionsJson.add(teamsArray);

        divisionChoice++;
    }

    public void makeTeamDropdown() {

        // List the teams according to the division
        final Spinner teamDropdown = (Spinner)findViewById(R.id.teamsDropdown);

        ArrayList<String> items = new ArrayList<>();

        for(int i = 0; i < divisionsJson.get(divisionChoice).size(); i++) {

            JSONObject team = (JSONObject)divisionsJson.get(divisionChoice).get(i);
            items.add((String) team.get("team"));
        }

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        teamDropdown.setAdapter(adapter2);

        teamDropdown.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int arg2, long arg3) {
                        int position = teamDropdown.getSelectedItemPosition();

                        teamChoice = position;
                        Log.d("Team chosen", "" + position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {

                    }
                }
        );

        teamsLoaded = true;
    }

    public void noResponseFromInternet() {

        String errorMessage = "No response from the server. Check your connectivity.";

        Toast noResponse = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG);
        noResponse.show();

        Intent restart = new Intent(this, LoadInfo.class);
        startActivity(restart);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_load_info, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
