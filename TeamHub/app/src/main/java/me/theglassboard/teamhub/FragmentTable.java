package me.theglassboard.teamhub;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;


public class FragmentTable extends Fragment {

    ArrayList<Team> teams;

    public void setTeams(ArrayList<Team> teams) { this.teams = teams; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_table, container, false);

        TableLayout table = (TableLayout)view.findViewById(R.id.LeagueTablePutHere);
        createLeagueTable(table);

        return view;
    }

    private void createLeagueTable(TableLayout leagueTable) {

        if(leagueTable == null)
            Log.d("LEAGUE TABLE", "IS NULL");

        for(int i = 0; i < teams.size(); i++) {

            TableRow row = teams.get(i).getLeagueStats().setAllViews(getActivity(), teams.get(i).getClub());
            leagueTable.addView(row);
        }

    }

}
