package com.games.tictactoe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class GameTest {

  private final String token = "token";

  private final String ownerToken = "accessToken1";
  private final String ownerName = "userName1";

  private final String opponentToken = "accessToken2";
  private final String opponentName = "userName2";

  private Game game = null;

  @Before
  public void init() {
    game = new Game(token, 3, ownerToken, ownerName);
  }

  @Test
  public void gameCreating() {

    assertNotNull(game.getOwner());
    assertNull(game.getOpponent());

    assertEquals(ownerName, game.getOwner().getName());
    assertEquals(ownerToken, game.getOwner().getToken());
  }

  @Test
  public void join() {

    game.createOpponent(opponentToken, opponentName);

    assertNotNull(game.getOwner());
    assertNotNull(game.getOpponent());

    assertNotEquals(game.getOwner(), game.getOpponent());
    assertEquals(opponentToken, game.getOpponent().getToken());
  }

  @Test(expected = IncorrectStepException.class)
  public void stepGameNotReady() throws IncorrectStepException {

    game.doStep(0, 0, ownerToken);
  }

  @Test
  public void stepGamePlaying() throws IncorrectStepException {

    game.createOpponent(opponentToken, opponentName);
    assertEquals(Game.State.Ready, game.getState());

    game.doStep(0, 0, ownerToken);
    assertEquals(Game.State.Playing, game.getState());
  }

  @Test
  public void stepGameWin() throws IncorrectStepException {
    game.createOpponent(opponentToken, opponentName);

    game.doStep(0, 0, ownerToken);
    game.doStep(1, 0, opponentToken);
    game.doStep(0, 1, ownerToken);
    game.doStep(2, 0, opponentToken);
    game.doStep(0, 2, ownerToken);

    assertEquals(Game.State.Finished, game.getState());
    assertEquals(ownerToken, game.getWinner().getToken());
  }
}