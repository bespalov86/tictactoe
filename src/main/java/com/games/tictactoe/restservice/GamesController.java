package com.games.tictactoe.restservice;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.games.tictactoe.model.Game;
import com.games.tictactoe.service.GamesManager;

@RestController
@RequestMapping("/games")
public class GamesController {
	
	@Autowired
	private GamesManager gamesManager;
	
	// TODO replace to POST
	@RequestMapping(value = "/new", method = GET)
	public Game createNewGame(
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "size", required = true) int size) {
		
		// TODO return response message instead of game
		return gamesManager.createNewGame(userName, size);
	}
}
