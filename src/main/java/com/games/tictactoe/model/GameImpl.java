package com.games.tictactoe.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class GameImpl implements Game {

	private static final char OWNER_STEP_SYMBOL = 'X';
	private static final char OPPONENT_STEP_SYMBOL = '0';
	private static final char FREE_FIELD_SYMBOL = '?';
	
	private static final String NO_SUCH_FIELD_MESSAGE = "No such field";
	private static final String OK_STEP_MESSAGE = "Ok";
	private static final String FIELD_BUSY_MESSAGE = "Field is busy";
	private static final String NOT_YOUR_TURN_MESSAGE = "Now not your turn!";
	private static final String GAME_FINISHED = "Game already finished";

	private final String token;
	private final int size;
	private final Player owner;
	private Player opponent;
	private Collection<Player> spectators = new ArrayList<>();
	private Player currentPlayer;
	private Player winner;
	private char[][] field;
	private State state;
	private long startTimestamp = 0;
	private long lastStepTimestamp = 0;
	private GameResult result = new GameResult("");

	public GameImpl(String token, Player owner, int size) {
		this.token = token;
		this.owner = owner;
		this.size = size;
		
		state = State.Ready;
		startTimestamp = System.currentTimeMillis();
		lastStepTimestamp = startTimestamp;

		initField();
		currentPlayer = owner;
	}

	@Override
	public int getSize() {
		return size;
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
	public Player getCurrentPlayer() {
		return currentPlayer;
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
	public long getDuration() {
		
		return System.currentTimeMillis() - startTimestamp;
	}

	@Override
	public long getActivityTime() {
		
		return System.currentTimeMillis() - lastStepTimestamp;
	}

	@Override
	public StepResult doStep(int row, int col, Player player) throws IncorrectStepException {
		
		if (state != State.Ready && state != State.Playing) {
			throw new IncorrectStepException(GAME_FINISHED);
		}
		
		if (row < 1 || row > size || col < 1 || col > size) {
			throw new IncorrectStepException(NO_SUCH_FIELD_MESSAGE);
		}

		// TODO need discuss when start game
		if (state == State.Ready) {
			start();
		}
		
		lastStepTimestamp = System.currentTimeMillis();
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
		} else {
			result = StepResult.createUnsuccessful("Looks like you are spectator...");			
		}

		if (result.isSuccessful()) {
			checkStepForWin(row, col, player);

			if (state == State.Playing) {
				checkForDraw();				
			}
		}

		return result;
	}

	@Override
	public GameResult getResult() {
		return result;
	}
	
	/**
	 * yes, manual check horizontal, vertical and diagonals
	 * maybe extract data to list and check list if necessary in future
	 */
	private void checkStepForWin(int stepRow, int stepCol, Player player) {
		// natural int
		char stepSymbol = field[stepRow-1][stepCol-1];

		// check horizontal
		boolean win = true;
		for (int col = 0; col < size; col++) {
			if (field[stepRow-1][col] != stepSymbol) {
				win = false;
				break;
			}
		}
		if (win) {
			stopWin(player);
			return;
		}
		
		//check vertical
		win = true;		
		for (int row = 0; row < size; row++) {
			if (field[row][stepCol-1] != stepSymbol) {
				win = false;
				break;
			}
		}
		if (win) {
			stopWin(player);
			return;
		}
		
		//check diagonal 1
		win = true;		
		for (int i = 0; i < size; i++) {
			if (field[i][i] != stepSymbol) {
				win = false;
				break;
			}
		}
		if (win) {
			stopWin(player);
			return;
		}
		
		//check diagonal 2
		win = true;		
		for (int i = 0; i < size; i++) {
			if (field[i][size - i - 1] != stepSymbol) {
				win = false;
				break;
			}
		}
		if (win) {
			stopWin(player);
			return;
		}
	}

	
	/**
	 * we will search FREE_FIELD_SYMBOL
	 * in other algorithm we can e.g. count steps
	 */
	private void checkForDraw() {
		
		boolean freeFieldExist = false;
		
		for (int row = 0; row < size && !freeFieldExist; row++) {
			for (char c : field[row]) {
				if (c == FREE_FIELD_SYMBOL) {
					freeFieldExist = true;
					break;
				}
			}
			
		}
		
		if (!freeFieldExist) {
			state = State.Done;
			result = new GameResult("draw");
		}
	}
	
	private void initField() {
		field = new char[size][size];
		for (char[] row : field) {
			Arrays.fill(row, FREE_FIELD_SYMBOL);
		}
	}

	private void start() {
		state = State.Playing;
	}
	
	private void stopWin(Player winner) {
		
		this.winner = winner;
		state = State.Done;
		
		if (winner.equals(owner)) {
			result = new GameResult("owner");			
		} else if (winner.equals(opponent)) {
			result = new GameResult("opponent");
		}
	}


	@Override
	public Player getWinner() {
		return winner;
	}
}
