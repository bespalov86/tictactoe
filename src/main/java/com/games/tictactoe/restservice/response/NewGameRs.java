package com.games.tictactoe.restservice.response;

public class NewGameRs extends BaseResponse {
	
	private String accessToken;
	private String gameToken;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getGameToken() {
		return gameToken;
	}
	public void setGameToken(String gameToken) {
		this.gameToken = gameToken;
	}

}
