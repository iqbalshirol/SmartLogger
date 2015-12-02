package com.hackathon.smartlogger.gateway.exception;


public class ResponseParseException extends Exception {

	private static final long serialVersionUID = 2L;

	public ResponseParseException(){
		super("Internal Error");
	}
	
	public ResponseParseException(final String msg){
		super(msg);
	}
	
	public ResponseParseException(final Exception e){
		super(e);
	}

}
