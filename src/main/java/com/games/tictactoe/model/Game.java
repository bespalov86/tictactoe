package com.games.tictactoe.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Game Tictactoe. Class contains players: owner, opponent; spectators, game field.
 */
public class Game {

  public enum State {
    Creating, Ready, Playing, Finished
  }

  private static final char FREE_FIELD_SYMBOL = '?';
  private static final char OWNER_STEP_SYMBOL = 'X';
  private static final char OPPONENT_STEP_SYMBOL = '0';

  private static final String FIELD_BUSY_MESSAGE = "Field is busy";
  private static final String NOT_YOUR_TURN_MESSAGE = "Now not your turn!";
  private static final String GAME_FINISHED_MESSAGE = "Game already finished";

  private final String token;
  private final int size;
  private final GameField field;
  private final Player owner;
  private Player opponent;
  private final Collection<Spectator> spectators = new ArrayList<>();
  private Player currentPlayer;
  private Player winner;
  private State state = State.Creating;
  private long startTimestamp = 0;
  private long lastStepTimestamp = 0;

  /**
   * Game constructor.
   * 
   * @param token
   *          game token
   * @param size
   *          game field size
   * @param ownerToken
   *          token of player who created game
   * @param ownerName
   *          name of player who created game
   */
  public Game(String token, int size, String ownerToken, String ownerName) {
    this.token = token;
    this.size = size;
    field = new GameField(size, FREE_FIELD_SYMBOL);

    owner = new Player(ownerToken, ownerName, OWNER_STEP_SYMBOL, token);
    currentPlayer = owner;
  }

  /**
   * Creates opponent player.
   * 
   * @param token
   *          token of opponent player
   * @param name
   *          name of opponent player
   */
  public void createOpponent(String token, String name) {
    opponent = new Player(token, name, OPPONENT_STEP_SYMBOL, this.token);

    onGameReady();
  }

  /**
   * Adds spectator to game.
   * 
   * @param token
   *          token of spectator
   * @param name
   *          name of spectator
   */
  public void addSpectator(String token, String name) {
    spectators.add(new Spectator(name, token, this.token));
  }

  /**
   * Runs step in game for player with given token.
   * 
   * @param row
   *          row of step
   * @param col
   *          column of step
   * @param playerToken
   *          token of player who do step
   * @throws IncorrectStepException
   *           if any process incorrect
   */
  public void doStep(int row, int col, String playerToken) throws IncorrectStepException {

    if (state != State.Ready && state != State.Playing) {
      throw new IncorrectStepException(GAME_FINISHED_MESSAGE);
    }

    if (!playerToken.equals(currentPlayer.getToken())) {
      throw new IncorrectStepException(NOT_YOUR_TURN_MESSAGE);
    }

    // TODO need discuss when start game
    if (state == State.Ready) {
      onGameStarted();
    }

    lastStepTimestamp = System.currentTimeMillis();
    Step step = field.doStep(row, col, currentPlayer.getStepChar());

    switch (step.getResult()) {
      case Busy:
        throw new IncorrectStepException(FIELD_BUSY_MESSAGE);
      case Win:
        winner = currentPlayer;
        onGameFinished();
        break;
      case Draw:
        onGameFinished();
        break;
      case Ok:
        if (currentPlayer == owner) {
          currentPlayer = opponent;
        } else if (currentPlayer == opponent) {
          currentPlayer = owner;
        } else {
          // TODO maybe throw exception
        }
        break;
      default:
        break;
    }
  }

  /**
   * Game token.
   * 
   * @return game token
   */
  public String getToken() {
    return token;
  }

  /**
   * @return owner of game
   */
  public Player getOwner() {
    return owner;
  }

  /**
   * @return opponent player of game
   */
  public Player getOpponent() {
    return opponent;
  }

  /**
   * @return game field size
   */
  public int getSize() {
    return size;
  }

  /**
   * @return player who's turn to do step
   */
  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  /**
   * @return game state
   */
  public State getState() {
    return state;
  }

  /**
   * @return duration of game
   */
  public long getDuration() {

    return System.currentTimeMillis() - startTimestamp;
  }

  /**
   * @return activity time
   */
  public long getActivityTime() {

    long activityTime = 0;

    switch (state) {
      case Playing:
      case Finished:
        activityTime = System.currentTimeMillis() - lastStepTimestamp;
        break;
      default:
        break;
    }

    return activityTime;
  }

  /**
   * @return winner of game
   */
  public Player getWinner() {
    return winner;
  }
  
  
  public List<String> getGamePersonTokens() {
    List<String> tokens = new ArrayList<>();
    
    tokens.add(owner.getToken());
    if (opponent != null) {
      tokens.add(opponent.getToken());      
    }
    
    spectators.forEach(spec -> tokens.add(spec.getToken()));

    return tokens;
  }

  /**
   * Returns game field.
   * 
   * @return 2-dimensional char field
   */
  public char[][] getField() {
    return field.getField();
  }

  private void onGameReady() {
    state = State.Ready;
  }

  private void onGameStarted() {
    state = State.Playing;
    startTimestamp = System.currentTimeMillis();
  }

  private void onGameFinished() {
    state = State.Finished;
  }

}
