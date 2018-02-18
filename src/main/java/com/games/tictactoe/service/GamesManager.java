package com.games.tictactoe.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.games.tictactoe.model.Game;
import com.games.tictactoe.model.Game.State;
import com.games.tictactoe.model.IncorrectStepException;
import com.games.tictactoe.model.NoGameException;
import com.games.tictactoe.service.storage.GamesStorage;

/**
 * Games Manager
 */
@Service
public class GamesManager {

  private static final int MIN_FIELD_SIZE = 3;
  private static final String TOO_SMALL_FIELD_MESSAGE = "Minimum field size is " + MIN_FIELD_SIZE;
  private static final String NO_GAME_WITH_TOKEN_MESSAGE = "No game with token ";
  private static final String NO_GAME_WITH_PLAYER_MESSAGE = "No game with player token ";
  private static final String GAME_TOKEN_PATTERN = "gameToken";
  private static final String PLAYER_ACCESS_TOKEN_PATTERN = "accessToken";
  // TODO move to properties
  private static final int GAME_NON_ACTIVITY_MILLISECONDS = 5 * 60 * 1000; // 5 minutes
  private static final int GAME_ACTIVITY_CHECK_PERIOD_MILLISECONDS = 100;

  @Autowired
  @Qualifier("dbStorage")
  private GamesStorage gamesStorage;

  /**
   * Persons tokens map, key - person token, value - game token
   */
  private Map<String, String> personToGameTokens = new ConcurrentHashMap<>();

  /**
   * Games storage, key - game token, value - game
   */
  private Map<String, Game> games = new ConcurrentHashMap<>();

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
  }

  /**
   * Creating new game.
   * 
   * @param userName
   *          game creator and owner name
   * @param size
   *          field size
   * @return new created game object
   * @throws NoGameException
   *           if field size too small
   */
  public Game createNewGame(String playerName, int size) throws NoGameException {

    if (size < MIN_FIELD_SIZE) {
      throw new NoGameException(TOO_SMALL_FIELD_MESSAGE);
    }

    String playerToken = generateNewPlayerAccessToken();
    String gameToken = generateNewGameToken();
    Game newGame = new Game(gameToken, size, playerToken, playerName);

    personToGameTokens.put(playerToken, gameToken);
    games.put(gameToken, newGame);

    synchronized (newGame) {
      gamesStorage.createPlayer(newGame.getOwner());  
    }

    return newGame;
  }

  /**
   * Joining existing game.
   * 
   * @param gameToken
   *          game token to join
   * @param userName
   *          user name
   * @return accessToken of joined user
   * @throws NoGameException
   *           if no existing game with such token
   */
  public String joinGame(String gameToken, String playerName) throws NoGameException {

    Game game = games.get(gameToken);
    if (game == null) {
      throw new NoGameException(NO_GAME_WITH_TOKEN_MESSAGE + gameToken);
    }
    
    String playerToken = generateNewPlayerAccessToken();
    synchronized (game) {
      if (game.getOpponent() == null) {
        game.createOpponent(playerToken, playerName);
      } else {
        game.addSpectator(playerToken, playerName);
      }
      
      personToGameTokens.put(playerToken, gameToken);
      gamesStorage.createPlayer(game.getOpponent());
    }

    return playerToken;
  }

  /**
   * Runs step for player with given token.
   * 
   * @param row
   *          row of step
   * @param col
   *          column of step
   * @param playerToken
   *          token of player who do step
   * @throws NoGameException
   *          if no existing for this player
   * @throws IncorrectStepException
   *          if step is incorrect for any reason
   */
  public void doStep(int row, int col, String playerToken)
      throws NoGameException, IncorrectStepException {

    Game game = getGameByPlayerToken(playerToken);
    synchronized (game) {
      game.doStep(row, col, playerToken);

      if (game.getState() == State.Finished) {
        gamesStorage.saveGameResult(game);
      }
    }
  }
  
  /**
   * Getting all games list.
   * 
   * @return games list
   */
  public List<Game> getGamesList() {
    return new ArrayList<>(games.values());
  }

  public Game.State getGameState(String playerToken) throws NoGameException {
    
    Game game = getGameByPlayerToken(playerToken);
    return game.getState();
  }

  private void startCheckGamesActivity() {
    checkGamesLifeThread = new Thread() {
      @Override
      public void run() {
        while (needCheckGamesLife) {
          
          for (Iterator<Game> iterator = games.values().iterator(); iterator.hasNext();) {
            Game game = iterator.next();
            if (game.getActivityTime() >= GAME_NON_ACTIVITY_MILLISECONDS) {
              List<String> gamePersonTokens = game.getGamePersonTokens();
              gamePersonTokens.forEach(token -> {
                personToGameTokens.remove(token);
              });

              iterator.remove();
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
  }

  private String generateNewPlayerAccessToken() {
    return PLAYER_ACCESS_TOKEN_PATTERN + System.currentTimeMillis() + personToGameTokens.size();
  }

  private String generateNewGameToken() {
    return GAME_TOKEN_PATTERN + System.currentTimeMillis() + games.size();
  }

  public Game getGameByPlayerToken(String playerToken) throws NoGameException {
    String gameToken = personToGameTokens.get(playerToken);
    if (gameToken == null) {
      throw new NoGameException(NO_GAME_WITH_PLAYER_MESSAGE + playerToken);
    }

    Game game = games.get(gameToken);
    if (game == null) {
      throw new NoGameException(NO_GAME_WITH_TOKEN_MESSAGE + gameToken);
    }

    return game;
  }
}
