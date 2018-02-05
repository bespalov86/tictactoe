package com.games.tictactoe.model;

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
}
