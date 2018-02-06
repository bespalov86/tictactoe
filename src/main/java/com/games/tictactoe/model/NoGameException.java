package com.games.tictactoe.model;

public class NoGameException extends Exception {

	private static final long serialVersionUID = 3741632831824613693L;

	public NoGameException(String message) {
		super(message);
	}
}
