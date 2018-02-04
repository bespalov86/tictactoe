package com.games.tictactoe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.games.tictactoe.model.Game;
import com.games.tictactoe.service.GamesManager;

@ComponentScan
@EnableAutoConfiguration
public class App 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
    	
//        GamesManager manager = new GamesManager();
//        Game game = manager.createNewGame("userName", 3);

//        System.out.println( "Game: " + game.getToken() );
//        System.out.println( "Owner: " + game.getOwner().getName() +
//        		", token: " + game.getOwner().getAccessToken());
    }
}
