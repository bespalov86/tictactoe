package com.games.tictactoe.restservice.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.games.tictactoe.model.Game;

public class GamesListRs extends BaseResponse {

	private List<GameRs> games;
	
	public List<GameRs> getGames() {
		return games;
	}

	// TODO use something library like Dozer
	public void initGames(List<Game> games) {
		
		if (games != null) {
			this.games = new ArrayList<>();
			
			games.stream().filter(Objects::nonNull)
			.forEach(game -> {
				GameRs gameRs = new GameRs();

				gameRs.gameToken	= game.getToken();
				gameRs.owner		= game.getOwner().getName();
				gameRs.opponent		= game.getOpponent() != null ? game.getOpponent().getName() : "";
				gameRs.size			= game.getSize();
				gameRs.gameDuration	= game.getDuration();
				gameRs.gameResult	= game.getResult().getDescription();
				gameRs.state		= game.getState().toString().toLowerCase();

				this.games.add(gameRs);
			});
		}
	}

	public class GameRs {
		String gameToken;
		String owner;
		String opponent;
		int size;
		long gameDuration;
		String gameResult;
		String state;

		// getters need for JSON response
		public String getGameToken() {
			return gameToken;
		}
		public String getOwner() {
			return owner;
		}
		public String getOpponent() {
			return opponent;
		}
		public int getSize() {
			return size;
		}
		public long getGameDuration() {
			return gameDuration;
		}
		public String getGameResult() {
			return gameResult;
		}
		public String getState() {
			return state;
		}
	}
}
