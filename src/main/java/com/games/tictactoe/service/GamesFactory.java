package com.games.tictactoe.service;

import org.springframework.stereotype.Service;

import com.games.tictactoe.model.Game;
import com.games.tictactoe.model.GameImpl;
import com.games.tictactoe.model.Player;

@Service
public class GamesFactory {
	
	public Game createNewGame(String token, Player owner, int size) {
		return new GameImpl(token, owner, size);
	}
}
