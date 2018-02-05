package com.games.tictactoe.model;

public class IncorrectStepException extends Exception {

	private static final long serialVersionUID = 1505777181866249432L;

	public IncorrectStepException(String message) {
		super(message);
	}
}
