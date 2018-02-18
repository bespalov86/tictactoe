package com.games.tictactoe.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.games.tictactoe.model.Game;
import com.games.tictactoe.model.NoGameException;
import com.games.tictactoe.service.storage.GamesStorage;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes=TestConfiguration.class, loader=AnnotationConfigContextLoader.class)
@SpringBootTest
public class GamesManagerTest {

	@MockBean(name="dbStorage")
	private GamesStorage gamesStorage;
	
	@Autowired
	private GamesManager manager;
	
	@Before
	public void clear() {
		manager.clearAll();
	}

	@Test
	public void TestAddGames() throws NoGameException {
		
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
	public void TestJoinGame() throws NoGameException {
		
		Game game = manager.createNewGame("userName", 3);
		String gameToken = game.getToken();

		assertNotNull(gameToken);
		assertNotNull(game.getOwner());
		assertNotNull(game.getOwner().getToken());

		String opponentAccessToken = manager.joinGame(gameToken, "opponentName");
		assertNotNull(opponentAccessToken);
		assertNotEquals(game.getOwner(), game.getOpponent());
		assertNotEquals(game.getOwner().getToken(), opponentAccessToken);
	}

	@Test(expected = NoGameException.class)
	public void TestJoinNonExistingGame() throws NoGameException {

		Game game = manager.createNewGame("userName", 3);
		String gameToken = game.getToken();

		manager.joinGame(gameToken + "abc", "userName");
	}
	
}

