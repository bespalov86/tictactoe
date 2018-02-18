package com.games.tictactoe.model;

/**
 * Contains data returning from field to game on step
 */
class Step {

  private final Result result;

  public Step(Result result) {
    this.result = result;
  }

  public Result getResult() {
    return result;
  }

  public enum Result {
    Ok, Busy, Win, Draw
  }

}
