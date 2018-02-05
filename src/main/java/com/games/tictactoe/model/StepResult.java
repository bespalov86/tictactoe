package com.games.tictactoe.model;

public class StepResult {
	
	private final boolean isSuccessful;
	private final String description;
	
	public StepResult(boolean isSuccessful, String description) {
		this.isSuccessful = isSuccessful;
		this.description = description;
	}

	public boolean isSuccessful() {
		return isSuccessful;
	}

	public String getDescription() {
		return description;
	}
	
	public static StepResult createSuccessful(String description) {
		return new StepResult(true, description);
	}
	
	public static StepResult createUnsuccessful(String description) {
		return new StepResult(false, description);
	}

}
