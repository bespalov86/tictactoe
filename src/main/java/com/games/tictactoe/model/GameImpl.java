package com.games.tictactoe.model;

import java.util.ArrayList;
import java.util.Collection;

public class GameImpl implements Game {
	
	private String token;
	private int size;
	private Player owner;
	private Player opponent;
	private Collection<Player> spectators = new ArrayList<>();
	
	public GameImpl(String token, Player owner, int size) {
		this.token = token;
		this.owner = owner;
		this.size = size;
	}

	@Override
	public String getToken() {
		return token;
	}

	@Override
	public Player getOwner() {
		return owner;
	}

	@Override
	public Player getOpponent() {
		return opponent;
	}

	@Override
	public void setOpponent(Player opponent) {
		this.opponent = opponent;
	}

	@Override
	public void addSpectator(Player spectator) {
		spectators.add(spectator);
	}
}
