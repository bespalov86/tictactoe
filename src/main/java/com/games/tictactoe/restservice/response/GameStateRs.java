package com.games.tictactoe.restservice.response;

import java.util.ArrayList;
import java.util.List;

import com.games.tictactoe.model.Game;
import com.games.tictactoe.service.GameState;

public class GameStateRs extends BaseResponse {
	
	private boolean youTurn;
	private long gameDuration;
	private List<String> field;
	private String winner;

	public boolean isYouTurn() {
		return youTurn;
	}

	public long getGameDuration() {
		return gameDuration;
	}

	public List<String> getField() {
		return field;
	}
	
	public String getWinner() {
		return winner;
	}

	public void initGameState(GameState gameState) {
		
		Game game = gameState.getGame();
		
		youTurn = gameState.isYouTurn();
		gameDuration = game.getDuration();
		winner = game.getWinner() != null ? game.getWinner().getName() : "";
		
		field = new ArrayList<>();
		
		char[][] gameField = game.getField();
		for (char[] row : gameField) {
			field.add(new String(row));
		}
	}

}
