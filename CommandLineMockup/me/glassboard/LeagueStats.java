package me.glassboard;

import java.util.*;

public class LeagueStats {
	
	private class Stat {

		private String name;

		private int value;

		Stat(String name, int value) {

			this.name = name;
			this.value = value;

		}

	}

	private ArrayList<Stat> stats;

	public LeagueStats() {

		stats = new ArrayList<Stat>();

	}

}