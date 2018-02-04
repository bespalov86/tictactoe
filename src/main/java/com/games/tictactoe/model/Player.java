package com.games.tictactoe.model;

public class Player {
	
	private String name;
	private String accessToken;
	private String gameToken;
	
	public Player(String name, String accessToken) {
		this.name = name;
		this.accessToken = accessToken;
	}

	public String getGameToken() {
		return gameToken;
	}

	public void setGameToken(String gameToken) {
		this.gameToken = gameToken;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
