package com.games.tictactoe;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.games.tictactoe.model.Game;
import com.games.tictactoe.model.NoGameException;
import com.games.tictactoe.model.StepResult;
import com.games.tictactoe.service.GamesManager;

@ComponentScan
@EnableAutoConfiguration
public class App 
{
    public static void main( String[] args )
    {
    	ApplicationContext context = SpringApplication.run(App.class, args);
    	
    	//runExampleWin(context);
    	
    	//runExampleService(context);
    }
    
    private static void runExampleService(ApplicationContext context) {
    	GamesManager manager = context.getBean(GamesManager.class);
    	
        Game game = null;
        try {
        	game = manager.createNewGame("user1", 3);
		} catch (NoGameException e1) {
		}
//*
        // games/new		
        String gameToken = game.getToken();
        String ownerToken = game.getOwner().getAccessToken();
    	System.out.println( "gameToken: " + gameToken );
    	System.out.println( "accessToken: " + ownerToken );
//*/    	
        
    }
    
    private static void runExampleWin(ApplicationContext context) {
    	GamesManager manager = context.getBean(GamesManager.class);
        Game game = null;
        try {
        	game = manager.createNewGame("user1", 3);
		} catch (NoGameException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        String gameToken = game.getToken();
        String ownerToken = game.getOwner().getAccessToken();

        String opponentToken = null;
        try {
        	opponentToken = manager.joinGame(gameToken, "user2");
		} catch (Exception e) {
		}
        
        printGameStatus(game);
        StepResult step1 = manager.doStep(1, 3, ownerToken);
        manager.doStep(1, 1, opponentToken);
        manager.doStep(2, 2, ownerToken);
        manager.doStep(2, 1, opponentToken);
        manager.doStep(3, 1, ownerToken);
        
        System.out.println( "after:" );
        printGameStatus(game);
    }
    
    private static void printGameStatus(Game game) {
    	for (int i = 0; i < game.getField().length; i++) {
    		System.out.println( Arrays.toString(game.getField()[i]) );
		}
    	
    }
}
