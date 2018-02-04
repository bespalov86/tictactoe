package com.games.tictactoe.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.games.tictactoe.model.Game;
import com.games.tictactoe.model.NoSuchGameException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=TestConfiguration.class, loader=AnnotationConfigContextLoader.class)
public class GamesManagerTest {
	
	@Autowired
	private GamesManager manager;
	
	@Before
	public void clear() {
		manager.clearAll();
	}

	@Test
	public void TestAddGames() {
		
		Game game1 = manager.createNewGame("userName1", 3);
		Game game2 = manager.createNewGame("userName2", 5);
		List<Game> games = manager.getGamesList();
		
		assertNotNull(games);
		assertEquals(2, games.size());
		assertNotNull(game1.getToken());
		assertNotNull(game2.getToken());
		assertNotEquals(game1.getToken(), game2.getToken());
	}

	@Test
	public void TestJoinGame() throws NoSuchGameException {
		
		Game game = manager.createNewGame("userName", 3);
		String gameToken = game.getToken();

		assertNotNull(gameToken);
		assertNotNull(game.getOwner());
		assertNotNull(game.getOwner().getAccessToken());

		String opponentAccessToken = manager.joinGame(gameToken, "opponentName");
		assertNotNull(opponentAccessToken);
		assertNotEquals(game.getOwner(), game.getOpponent());
		assertNotEquals(game.getOwner().getAccessToken(), opponentAccessToken);
	}

	@Test(expected = NoSuchGameException.class)
	public void TestJoinNonExistingGame() throws NoSuchGameException {

		Game game = manager.createNewGame("userName", 3);
		String gameToken = game.getToken();

		manager.joinGame(gameToken + "abc", "userName");
	}
	
}

