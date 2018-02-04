package com.games.tictactoe.model;

public interface Game {
	
	String getToken();
	Player getOwner();
	Player getOpponent();
	void setOpponent(Player opponent);
	void addSpectator(Player spectator);

}
