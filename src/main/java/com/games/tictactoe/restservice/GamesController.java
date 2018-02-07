package com.games.tictactoe.restservice;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.games.tictactoe.model.Game;
import com.games.tictactoe.model.NoGameException;
import com.games.tictactoe.model.StepResult;
import com.games.tictactoe.restservice.response.DoStepRs;
import com.games.tictactoe.restservice.response.GameStateRs;
import com.games.tictactoe.restservice.response.GamesListRs;
import com.games.tictactoe.restservice.response.JoinGameRs;
import com.games.tictactoe.restservice.response.NewGameRs;
import com.games.tictactoe.service.GameState;
import com.games.tictactoe.service.GamesManager;

@RestController
@RequestMapping("/games")
public class GamesController {
	
	@Autowired
	private GamesManager gamesManager;
	
	@RequestMapping(value = "/new", method = POST)
	public NewGameRs createNewGame(
			@RequestParam("userName") String userName,
			@RequestParam("size") int size) {
		
		NewGameRs response = new NewGameRs();

		try {
			Game game = gamesManager.createNewGame(userName, size);
			response.initSuccessfulParameters();
			response.setAccessToken(game.getOwner().getAccessToken());
			response.setGameToken(game.getToken());
		} catch (NoGameException e) {
			response.initUnsuccessfulParameters();
		} catch (Exception e) {
			response.initUnsuccessfulParameters();
			// other messages
		}
		
		return response;
	}
	
	@RequestMapping(value = "/list", method = GET)
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

	@RequestMapping(value = "/do_step", method = POST)
	public DoStepRs doStep(
			@RequestHeader("accessToken") String accessToken,
			@RequestParam("row") int row,
			@RequestParam("col") int col) {
		
		DoStepRs response = new DoStepRs();

		try {
			StepResult stepResult = gamesManager.doStep(row, col, accessToken);
			if (stepResult.isSuccessful()) {
				response.initSuccessfulParameters();	
			} else {
				response.initUnsuccessfulParameters();	
			}
		} catch (Exception e) {
			response.initUnsuccessfulParameters();
		}

		return response;
	}

	@RequestMapping(value = "/state", method = POST)
	public GameStateRs getGameState(
			@RequestHeader("accessToken") String accessToken) {
		
		GameStateRs response = new GameStateRs();

		try {
			GameState gameState = gamesManager.getGameState(accessToken);
			response.initGameState(gameState);
			response.initSuccessfulParameters();	
		} catch (Exception e) {
			response.initUnsuccessfulParameters();
		}

		return response;
	}

}
