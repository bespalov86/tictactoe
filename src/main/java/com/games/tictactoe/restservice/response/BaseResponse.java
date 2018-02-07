package com.games.tictactoe.restservice.response;

abstract class BaseResponse {
	
	protected Status status;
	protected int code = Integer.MIN_VALUE;
	
	public void initSuccessfulParameters() {
		status = Status.Ok;
		code = 0;
	}
	
	public void initUnsuccessfulParameters() {
		status = Status.Error;
		code = -1;
	}
	
	public Status getStatus() {
		return status;
	}

	public int getCode() {
		return code;
	}

	public enum Status {
		Ok,
		Error
	}
}
