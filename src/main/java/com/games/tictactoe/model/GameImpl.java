package com.games.tictactoe.model;

import java.util.ArrayList;
import java.util.Collection;

public class GameImpl implements Game {

	private static final char OWNER_STEP_SYMBOL = 'X';
	private static final char OPPONENT_STEP_SYMBOL = '0';
	private static final char FREE_FIELD_SYMBOL = '?';
	
	private static final String NO_SUCH_FIELD_MESSAGE = "No such field";
	private static final String OK_STEP_MESSAGE = "Ok";
	private static final String FIELD_BUSY_MESSAGE = "Field is busy";
	private static final String NOT_YOUR_TURN_MESSAGE = "Now not your turn!";

	private final String token;
	private final int size;
	private final Player owner;
	private Player opponent;
	private Collection<Player> spectators = new ArrayList<>();
	private Player currentPlayer;
	private char[][] field;
	private State state;
	private long startTimestamp = 0;
	private long lastStepTimestamp = 0;
 
 	public GameImpl(String token, Player owner, int size) {
		this.token = token;
		this.owner = owner;
		this.size = size;
		
		state = State.Ready;

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
	public Collection<Player> getSpectators() {
		return spectators;
	}
	
	@Override
	public char[][] getField() {
		return field;
	}

	@Override
	public State getState() {
		return state;
	}

	@Override
	public StepResult doStep(int row, int col, Player player) throws IncorrectStepException {
		
		// TODO need discuss when start game
		if (state == State.Ready) {
			start();
		}
		
		lastStepTimestamp = System.currentTimeMillis();
		
		if (row < 1 || row > size || col < 1 || col > size) {
			throw new IncorrectStepException(NO_SUCH_FIELD_MESSAGE);
		}
		
		StepResult result = null;

		if (player.equals(owner)) { // owner wants to do step
			if (currentPlayer.equals(owner)) { // owner's turn
				if (field[row-1][col-1] == FREE_FIELD_SYMBOL) { // field is free
					field[row-1][col-1] = OWNER_STEP_SYMBOL;    // do step
					currentPlayer = opponent;				// switch turn
					result = StepResult.createSuccessful(OK_STEP_MESSAGE);
				} else {
					result = StepResult.createUnsuccessful(FIELD_BUSY_MESSAGE);
				}
			} else {
				result = StepResult.createUnsuccessful(NOT_YOUR_TURN_MESSAGE);
			}
		} else if (player.equals(opponent)) { // opponent wants to do step
			if (currentPlayer.equals(opponent)) { // opponent's turn
				if (field[row-1][col-1] == FREE_FIELD_SYMBOL) { // field is free
					field[row-1][col-1] = OPPONENT_STEP_SYMBOL; // do step
					currentPlayer = owner;                      // switch turn
					result = StepResult.createSuccessful(OK_STEP_MESSAGE);
				} else {
					result = StepResult.createUnsuccessful(FIELD_BUSY_MESSAGE);
				}
			} else {
				result = StepResult.createUnsuccessful(NOT_YOUR_TURN_MESSAGE);
			}
		}
		
		result = StepResult.createUnsuccessful("Looks like you are spectator...");
		
		return result;
	}

	private void initField() {
		field = new char[size][size];
		for (int row = 0; row < size;  row++) {
			for (int col = 0; col < size;  col++) {
				field[row][col] = FREE_FIELD_SYMBOL;
			}
		}
	}

	private void start() {
		startTimestamp = System.currentTimeMillis();
		state = State.Playing;
	}

	@Override
	public long getDuration() {
		
		return System.currentTimeMillis() - startTimestamp;
	}

	@Override
	public long getActivityTime() {
		
		return System.currentTimeMillis() - lastStepTimestamp;
	}

}
