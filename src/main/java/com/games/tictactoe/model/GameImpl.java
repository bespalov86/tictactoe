package com.games.tictactoe.model;

import java.util.ArrayList;
import java.util.Collection;

public class GameImpl implements Game {

	private static final char OWNER_STEP_SYMBOL = 'X';
	private static final char OPPONENT_STEP_SYMBOL = '0';
	private static final char FREE_FIELD_SYMBOL = '?';

	private final String token;
	private final int size;
	private final Player owner;
	private Player opponent;
	private Collection<Player> spectators = new ArrayList<>();
	private Player currentPlayer;
	private char[][] field;
 
 	public GameImpl(String token, Player owner, int size) {
		this.token = token;
		this.owner = owner;
		this.size = size;

		initField();
		currentPlayer = owner;
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

	@Override
	public char[][] getField() {
		return field;
	}
	
	@Override
	public StepResult doStep(int row, int col, Player player) throws IncorrectStepException {
		if (row < 1 || row > size || col < 1 || col > size) {
			throw new IncorrectStepException("No such field " + row + "x" + col);
		}

		if (player.equals(owner)) { // owner wants to do step
			if (currentPlayer.equals(owner)) { // owner's turn
				if (field[row-1][col-1] == FREE_FIELD_SYMBOL) { // field is free
					field[row-1][col-1] = OWNER_STEP_SYMBOL;    // do step
					currentPlayer = opponent;				// switch turn
					return StepResult.createSuccessful("Ok");
				} else {
					return StepResult.createUnsuccessful("Field is busy");
				}
			} else {
				return StepResult.createUnsuccessful("Now not your turn!");
			}
		} else if (player.equals(opponent)) { // opponent wants to do step
			if (currentPlayer.equals(opponent)) { // opponent's turn
				if (field[row-1][col-1] == FREE_FIELD_SYMBOL) { // field is free
					field[row-1][col-1] = OPPONENT_STEP_SYMBOL; // do step
					currentPlayer = owner;                  // switch turn
					return StepResult.createSuccessful("ok");
				} else {
					return StepResult.createUnsuccessful("field is busy");
				}
			} else {
				return StepResult.createUnsuccessful("Now not your turn!");
			}
		}
		
		return StepResult.createUnsuccessful("Looks like you are spectator...");
	}

	private void initField() {
		field = new char[size][size];
		for (int row = 0; row < size;  row++) {
			for (int col = 0; col < size;  col++) {
				field[row][col] = FREE_FIELD_SYMBOL;
			}
		}
	}
}
