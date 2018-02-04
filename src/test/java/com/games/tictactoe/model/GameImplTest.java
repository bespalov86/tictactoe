package com.games.tictactoe.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GameImplTest {
	
	@Test
	public void TestGameImplementation() {
		
		Player player = new Player("userName", "accessToken");
		GameImpl game = new GameImpl("token", player, 3);

		assertEquals("token", game.getToken());
		assertEquals(player, game.getOwner());
		assertNull(game.getOpponent());
	}

}
