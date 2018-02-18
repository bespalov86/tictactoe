package com.games.tictactoe.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

	@Bean
	public GamesManager gamesManager() {
		return new GamesManager();
	}
}
