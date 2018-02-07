/**
 * 
 */
package com.games.tictactoe.service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.games.tictactoe.model.IncorrectStepException;
import com.games.tictactoe.model.Game;
import com.games.tictactoe.model.NoGameException;
import com.games.tictactoe.model.Player;
import com.games.tictactoe.model.StepResult;

/**
 * Games Manager
 */
@Service
public class GamesManager {
	
	private static final int MIN_FIELD_SIZE = 3;
	private static final String TOO_SMALL_FIELD_MESSAGE = "Minimum field size is " + MIN_FIELD_SIZE;
	private static final String NO_GAME_WITH_TOKEN_MESSAGE = "No game with token ";
	private static final String GAME_TOKEN_PATTERN = "gameToken";
	private static final String PLAYER_ACCESS_TOKEN_PATTERN = "accessToken";
	private static final int GAME_ACTIVITY_MILLISECONDS = 5 * 60 * 1000; // 5 minutes
	private static final int GAME_ACTIVITY_CHECK_PERIOD_MILLISECONDS = 100;
	
	/**
	 * Games Factory
	 */
	@Autowired
	private GamesFactory gamesFactory;
	
	/**
	 * Players storage, key - accessToken, value - player
	 */
	private Map<String, Player> players = Collections.synchronizedMap(new HashMap<>());

	/**
	 * Games storage, key - gameToken, value - game
	 */
	private Map<String, Game> games = Collections.synchronizedMap(new HashMap<>());
	
	private boolean needCheckGamesLife = true;
	private Thread checkGamesLifeThread;
	
	@PostConstruct
	public void init() {
		startCheckGamesActivity();
	}
	
	@PreDestroy
	public void cleanup() {
		stopCheckGamesActivity();
	}

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
	 * @throws NoGameException 
	 */
	public Game createNewGame(String userName, int size) throws NoGameException {
		
		if (size < MIN_FIELD_SIZE) {
			throw new NoGameException(TOO_SMALL_FIELD_MESSAGE);
		}

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
	 * @throws NoGameException 
	 */
	public String joinGame(String gameToken, String userName) throws NoGameException {
		
		Game game = games.get(gameToken);
		if (game == null) {
			throw new NoGameException(NO_GAME_WITH_TOKEN_MESSAGE + gameToken);	
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
	
	public GameState getGameState(String accessToken) {

		Player player = players.get(accessToken);
		if (player == null) {
			return null; // maybe throw exception
		}
		
		Game game = games.get(player.getGameToken());
		if (game == null) {
			return null; // maybe throw exception
		}
		
		GameState state = new GameState();

		state.setGame(game);
		state.setYouTurn(player.equals(game.getCurrentPlayer()));
		
		return state;
	}
	
	public Game getGame(String accessToken) {
		
		Player player = players.get(accessToken);
		if (player == null) {
			return null; // maybe throw exception
		}
		
		return games.get(player.getGameToken());
	}
	
	private void startCheckGamesActivity() {
		checkGamesLifeThread = new Thread() {
			@Override
			public void run() {
				while (needCheckGamesLife) {
					
					// TODO discuss about performance if necessary
					synchronized (games) {
						for (Iterator<Game> iterator = games.values().iterator(); iterator.hasNext();) {
							Game game = iterator.next();
							if (game.getActivityTime() >= GAME_ACTIVITY_MILLISECONDS) {
								players.remove(game.getOwner().getAccessToken());
								if (game.getOpponent() != null) {
									players.remove(game.getOpponent().getAccessToken());										
								}
								game.getSpectators().forEach(spec -> {
									players.remove(spec.getAccessToken());
								});
								
								iterator.remove();
							}
						}
					}
					
					try {
						Thread.sleep(GAME_ACTIVITY_CHECK_PERIOD_MILLISECONDS);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		checkGamesLifeThread.start();
	}

	private void stopCheckGamesActivity() {
		needCheckGamesLife = false;
		try {
			checkGamesLifeThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(games.size());
		System.out.println(players.size());
		System.out.println( "cleanup done" );
	}
	
	private String generateNewAccessToken() {
		return PLAYER_ACCESS_TOKEN_PATTERN + players.size();
	}
	
	private String generateNewGameToken() {
		return GAME_TOKEN_PATTERN + games.size();
	}

}
