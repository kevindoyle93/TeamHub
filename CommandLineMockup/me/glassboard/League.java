package me.glassboard;

import java.util.*;

public class League {
	
	private String name;

	private ArrayList<Team> teams;

	public League() {

		teams = new ArrayList<Team>();

		getInfo();

	}

	private void getInfo() {

		Scanner input = new Scanner(System.in);

		// read in the name of the league from the command line
		System.out.println("What is the name of this league?");
		name = input.nextLine();


		// find the file with that name and read in the teams
		
		
	}

}