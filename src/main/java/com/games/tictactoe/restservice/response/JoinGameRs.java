package com.games.tictactoe.restservice.response;

public class JoinGameRs extends BaseResponse {
	
	private String accessToken;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

}
