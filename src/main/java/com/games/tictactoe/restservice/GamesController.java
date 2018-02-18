package com.games.tictactoe.restservice;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.games.tictactoe.model.Game;
import com.games.tictactoe.model.IncorrectStepException;
import com.games.tictactoe.model.NoGameException;
import com.games.tictactoe.restservice.response.DoStepRs;
import com.games.tictactoe.restservice.response.GameStateRs;
import com.games.tictactoe.restservice.response.GamesListRs;
import com.games.tictactoe.restservice.response.JoinGameRs;
import com.games.tictactoe.restservice.response.NewGameRs;
import com.games.tictactoe.service.GamesManager;

@RestController
public class GamesController {

  @Autowired
  private GamesManager gamesManager;

  @RequestMapping(value = "/games", method = POST)
  public NewGameRs createNewGame(
      @RequestParam("userName") String userName,
      @RequestParam("size") int size) {

    NewGameRs response = new NewGameRs();

    try {
      Game game = gamesManager.createNewGame(userName, size);
      response.initSuccessfulParameters();
      response.setAccessToken(game.getOwner().getToken());
      response.setGameToken(game.getToken());
    } catch (NoGameException e) {
      response.initUnsuccessfulParameters();
    } catch (Exception e) {
      response.initUnsuccessfulParameters();
      // other messages      
    }

    return response;
  }

  @RequestMapping(value = "/games", method = GET)
  public GamesListRs getGamesList() {

    GamesListRs response = new GamesListRs();

    try {
      List<Game> games = gamesManager.getGamesList();
      response.initSuccessfulParameters();
      response.initGames(games);
    } catch (Exception e) {
      response.initUnsuccessfulParameters();
      // other messages
    }

    return response;
  }

  @RequestMapping(value = "/join", method = POST)
  public JoinGameRs joinGame(
      @RequestParam("gameToken") String gameToken,
      @RequestParam("userName") String userName) {

    JoinGameRs response = new JoinGameRs();

    try {
      String accessToken = gamesManager.joinGame(gameToken, userName);
      response.initSuccessfulParameters();
      response.setAccessToken(accessToken);
    } catch (NoGameException e) {
      response.initUnsuccessfulParameters();
    } catch (Exception e) {
      response.initUnsuccessfulParameters();
      // other messages
    }

    return response;
  }

  @RequestMapping(value = "/step", method = POST)
  public DoStepRs doStep(
      @RequestHeader("accessToken") String accessToken,
      @RequestParam("row") int row,
      @RequestParam("col") int col) {

    DoStepRs response = new DoStepRs();

    try {
      gamesManager.doStep(row, col, accessToken);
      response.initSuccessfulParameters();
    } catch (NoGameException | IncorrectStepException e1) {
      response.initUnsuccessfulParameters();
      // other messages
    }

    return response;
  }

  @RequestMapping(value = "/state", method = GET)
  public GameStateRs getGameState(
      @RequestHeader("accessToken") String accessToken) {

    GameStateRs response = new GameStateRs();
    
    try {
      Game game = gamesManager.getGameByPlayerToken(accessToken);
      response.initSuccessfulParameters();
      response.initGameState(accessToken, game);
    } catch (NoGameException e) {
      response.initUnsuccessfulParameters();
    } catch (Exception e) {
      response.initUnsuccessfulParameters();
      // other messages
    }

    return response;
  }

}
