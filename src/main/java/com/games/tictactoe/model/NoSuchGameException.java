package com.games.tictactoe.model;

public class NoSuchGameException extends Exception {

	private static final long serialVersionUID = 3741632831824613693L;

	public NoSuchGameException(String message) {
		super(message);
	}
}
