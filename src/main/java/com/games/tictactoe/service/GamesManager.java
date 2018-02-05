/**
 * 
 */
package com.games.tictactoe.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.games.tictactoe.model.IncorrectStepException;
import com.games.tictactoe.model.Game;
import com.games.tictactoe.model.NoSuchGameException;
import com.games.tictactoe.model.Player;
import com.games.tictactoe.model.StepResult;

/**
 * Games Manager
 */
@Service
public class GamesManager {
	
	private static final String GAME_TOKEN_PATTERN = "gameToken";
	private static final String PLAYER_ACCESS_TOKEN_PATTERN = "accessToken";
	
	/**
	 * Games Factory
	 */
	@Autowired
	private GamesFactory gamesFactory;
	
	/**
	 * Players storage, key - accessToken, value - player
	 */
	private Map<String, Player> players = new HashMap<>();

	/**
	 * Games storage, key - gameToken, value - game
	 */
	private Map<String, Game> games = new HashMap<>();

	/**
	 * Clears all games and players
	 */
	public void clearAll() {
		games.clear();
		players.clear();
	}
	
	/**
	 * @param userName	game creator and owner name
	 * @param size		field size
	 * @return			new created game object
	 */
	public Game createNewGame(String userName, int size) {

		String accessToken = generateNewAccessToken();
		Player newPlayer = new Player(userName, accessToken);
		players.put(accessToken, newPlayer);
		
		String gameToken = generateNewGameToken();
		Game newGame = gamesFactory.createNewGame(gameToken, newPlayer, size);
		games.put(gameToken, newGame);
		
		newPlayer.setGameToken(gameToken);
		
		return newGame;
	}
	
	/**
	 * @return games list
	 */
	public List<Game> getGamesList() {
		return new ArrayList<>(games.values());
	}
	
	/**
	 * @param gameToken	game token to join
	 * @param userName	user name
	 * @return			accessToken of joined user
	 * @throws NoSuchGameException 
	 */
	public String joinGame(String gameToken, String userName) throws NoSuchGameException {
		
		Game game = games.get(gameToken);
		if (game == null) {
			throw new NoSuchGameException("No game with token " + gameToken);	
		}
		
		String accessToken = generateNewAccessToken();
		Player newPlayer = new Player(userName, accessToken);
		newPlayer.setGameToken(gameToken);
		players.put(accessToken, newPlayer);
		
		if (game.getOpponent() == null) {
			game.setOpponent(newPlayer);
		} else { // add spectator
			game.addSpectator(newPlayer);
		}

		return accessToken;
	}
	
	/**
	 * @param row of step
	 * @param col of step
	 * @param accessToken of player wanted to do step
	 * @return if step is successful
	 */
	public StepResult doStep(int row, int col, String accessToken) {
		
		Player player = players.get(accessToken);
		if (player == null) {
			return null; // maybe throw exception
		}
		
		Game game = games.get(player.getGameToken());
		if (game == null) {
			return null; // maybe throw exception
		}

		try {
			return game.doStep(row, col, player);
		} catch (IncorrectStepException e) {
		}
		
		return null;
	}
	
	
	private String generateNewAccessToken() {
		return PLAYER_ACCESS_TOKEN_PATTERN + players.size();
	}
	
	private String generateNewGameToken() {
		return GAME_TOKEN_PATTERN + games.size();
	}

}
