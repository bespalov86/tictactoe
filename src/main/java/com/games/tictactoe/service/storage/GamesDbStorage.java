package com.games.tictactoe.service.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.games.tictactoe.model.Game;
import com.games.tictactoe.model.Player;

@Repository("dbStorage")
public class GamesDbStorage implements GamesStorage {
	
	private static final String PLAYERS_TABLE_NAME = "players";
	private static final String CREATE_PLAYERS_TABLE_STATEMENT = 
			"CREATE TABLE IF NOT EXISTS " + PLAYERS_TABLE_NAME + " ("
					+ "accessToken VARCHAR(250) NOT NULL, "
					+ "name VARCHAR(250) NOT NULL, "
					+ "gameToken VARCHAR(250) NOT NULL, "
					+ "PRIMARY KEY(accessToken));";

	private static final String CREATE_PLAYER_STATEMENT =
			"INSERT INTO " + PLAYERS_TABLE_NAME + " (accessToken, name, gameToken) "
					+ "VALUES (?, ?, ?);";

	private static final String GAMES_TABLE_NAME = "games";
	private static final String CREATE_GAMES_TABLE_STATEMENT = 
			"CREATE TABLE IF NOT EXISTS " + GAMES_TABLE_NAME + " ("
					+ "token VARCHAR(250) NOT NULL, "
					+ "winner VARCHAR(250), " 
					+ "PRIMARY KEY(token));";

	private static final String CREATE_GAME_STATEMENT =
			"INSERT INTO " + GAMES_TABLE_NAME + " (token, winner) "
					+ "VALUES (?, ?);";

	@Value("${database.connection.url}")
	private String url;
	
	@Value("${database.connection.user}")
	private String user;
	
	@Value("${database.connection.password}")
	private String password;

	// TODO for debug
	@Value("${on.exit.print.stored.data}")
	private boolean printDataOnExit;
	
	@PostConstruct
	public void initDatabase() {
		
//    executeStatement("drop table  games;");
//    executeStatement("drop table  players;");

		executeStatement(CREATE_PLAYERS_TABLE_STATEMENT);
		executeStatement(CREATE_GAMES_TABLE_STATEMENT);
	}
	
	@PreDestroy
	public void destroy() {

		if (!printDataOnExit) {
			return;
		}
		
		try (Connection connection = DriverManager.getConnection(url, user, password)) {

			ResultSet rs = connection.createStatement().executeQuery("select * from " + PLAYERS_TABLE_NAME + ";");
			System.out.println("table " + PLAYERS_TABLE_NAME);
			printResultSet(rs);
			
			rs = connection.createStatement().executeQuery("select * from " + GAMES_TABLE_NAME + ";");
			System.out.println("\ntable " + GAMES_TABLE_NAME);
			printResultSet(rs);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int createPlayer(Player player) {
		
		int result = -1;
		
		try (Connection connection = DriverManager.getConnection(url, user, password)) {
			
			PreparedStatement statement = connection.prepareStatement(CREATE_PLAYER_STATEMENT);
			statement.setString(1, player.getToken());
			statement.setString(2, player.getName());
			statement.setString(3, player.getGameToken());
			result = statement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public int saveGameResult(Game game) {
		
		int result = -1;
		
		try (Connection connection = DriverManager.getConnection(url, user, password)) {
			
			PreparedStatement statement = connection.prepareStatement(CREATE_GAME_STATEMENT);
			statement.setString(1, game.getToken());
			statement.setString(2, game.getWinner() != null ? game.getWinner().getName() : null);
			result = statement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	private void executeStatement(String sql) {
		
		try (Connection connection = DriverManager.getConnection(url, user, password)) {
			connection.createStatement().execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void printResultSet(ResultSet resultSet) throws SQLException {
		
		int columns = resultSet.getMetaData().getColumnCount();
		for (int i = 0; i < columns; i++) {
			System.out.print(resultSet.getMetaData().getColumnName(i + 1) + " | ");
		}
		
		System.out.println("\n--------------------------");
		
		while (resultSet.next()) {
			List<String> row = new ArrayList<>();
			
			for (int i = 0; i < columns; i++) {
				row.add(String.valueOf(resultSet.getObject(i+1)));
			}
			
			String rowPrint = row.stream().collect(Collectors.joining(" | "));
			
			System.out.println(rowPrint);
		}

	}
}
