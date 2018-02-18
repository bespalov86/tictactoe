package com.games.tictactoe.model;

abstract class GamePerson {
  
  protected final String token;
  protected final String name;
  protected final String gameToken;
  
  protected GamePerson(String token, String name, String gameToken) {
    this.token = token;
    this.name = name;
    this.gameToken = gameToken;
  }

  public String getName() {
    return name;
  }

  public String getToken() {
    return token;
  }

  public String getGameToken() {
    return gameToken;
  }
  
  @Override
  public String toString() {
    return name;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((gameToken == null) ? 0 : gameToken.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((token == null) ? 0 : token.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GamePerson other = (GamePerson) obj;
    if (gameToken == null) {
      if (other.gameToken != null)
        return false;
    } else if (!gameToken.equals(other.gameToken))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (token == null) {
      if (other.token != null)
        return false;
    } else if (!token.equals(other.token))
      return false;
    return true;
  }

}
