package me.glassboard;

public class Team {

	private String name;

	// crest image

	private ClubOfficial manager;
	private  ClubOfficial secretary;

	// primary and secondary colours

	private LeagueStats stats;

	public Team() {



	}

	public Team(String name) {

		this.name = name;

		manager = new ClubOfficial();
		secretary = new ClubOfficial();

		stats = new LeagueStats();

	}

}