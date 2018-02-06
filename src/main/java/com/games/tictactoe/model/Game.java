package com.games.tictactoe.model;

import java.util.Collection;


public interface Game {
	
	/**
	 * @return game token
	 */
	String getToken();
	/**
	 * @return owner of game
	 */
	Player getOwner();
	/**
	 * @param opponent player of game
	 */
	void setOpponent(Player opponent);
	/**
	 * @return opponent player of game
	 */
	Player getOpponent();
	/**
	 * @param spectator added to game
	 */
	void addSpectator(Player spectator);
	/**
	 * @return spectators of game
	 */
	Collection<Player> getSpectators();
	/**
	 * @return two dimensional game field
	 */
	char[][] getField();
	/**
	 * @param row of step
	 * @param col of step
	 * @param player wants to do step
	 * @return step result
	 * @throws IncorrectStepException
	 */
	StepResult doStep(int row, int col, Player player) throws IncorrectStepException;
	/**
	 * @return game state
	 */
	State getState();
	/**
	 * @return duration of game
	 */
	long getDuration();
	
	/**
	 * @return activity time
	 */
	long getActivityTime();

	public enum State {
		Ready,
		Playing,
		Done
	}
}
