package me.glassboard;

import java.util.*;

public class ClubOfficial {
	
	private String name;
	private String club;
	private String phoneNumber;
	private String email;

	public ClubOfficial() {

		Scanner input = new Scanner(System.in);

		System.out.println("What is the name of this Official?");
		input.nextLine();

		System.out.println("What club do they belong to?");
		input.nextLine();

		System.out.println("What is their phone number?");
		input.nextLine();

		System.out.println("What is their email adsress?");
		input.nextLine();

	}

}