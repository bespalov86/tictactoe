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
	 * @return game field size
	 */
	int getSize();
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
	 * @return player who's turn to do step
	 */
	Player getCurrentPlayer();
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
	 * @return result of game
	 */
	GameResult getResult();
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
	/**
	 * @return winner of game
	 */
	Player getWinner();
	
	public enum State {
		Ready,
		Playing,
		Done
	}
}
