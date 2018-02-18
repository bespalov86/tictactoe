package com.games.tictactoe.model;

public class Player extends GamePerson {
  
  private final char stepChar;
  
  public Player(String token, String name, char stepChar, String gameToken) {
    super(token, name, gameToken);
    this.stepChar = stepChar; 
  }

  public char getStepChar() {
    return stepChar;
  }

  @Override
  public String toString() {
    return "player " + super.toString();
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + stepChar;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    Player other = (Player) obj;
    if (stepChar != other.stepChar)
      return false;
    return true;
  }

}
