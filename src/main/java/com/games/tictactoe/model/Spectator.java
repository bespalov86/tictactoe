package com.games.tictactoe.model;

public class Spectator extends GamePerson {

  public Spectator(String name, String token, String gameToken) {
    super(name, token, gameToken);
  }

  @Override
  public String toString() {
    return "spectator" + super.toString();
  }
}
