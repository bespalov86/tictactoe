package com.games.tictactoe.service.storage;

import com.games.tictactoe.model.Game;
import com.games.tictactoe.model.Player;

public interface GamesStorage {
	
	/**
	 * @param player to save in storage
	 * @return result
	 */
	public int createPlayer(Player player);
	
	/**
	 * @param game to save in storage
	 * @return result
	 */
	public int saveGameResult(Game game);
}
