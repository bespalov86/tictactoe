package com.games.tictactoe.service;

import com.games.tictactoe.model.Game;

public class GameState {
	
	private Game game;
	private boolean youTurn;
	
	public boolean isYouTurn() {
		return youTurn;
	}
	public void setYouTurn(boolean youTurn) {
		this.youTurn = youTurn;
	}
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
}
