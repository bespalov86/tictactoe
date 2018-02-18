package com.games.tictactoe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan
@EnableAutoConfiguration
public class App 
{
    public static void main( String[] args ) {
    	ApplicationContext context = SpringApplication.run(App.class, args);
    }
    
}
