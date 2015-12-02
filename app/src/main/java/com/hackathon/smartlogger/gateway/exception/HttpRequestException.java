/**
 * ============================================================================================================
 * File Name       	: HttpRequestException.java
 * Author          	: Apex Team @ Endeavour Software Technologies pvt. ltd., Bangalore.
 * Version         	: 1.0.0
 * Copyright       	: Endeavour
 * Description     	: Custom exception class that holds an {@link HttpResponse}.
 * This allows upstream code to receive an HTTP status code and
 * any content received as well as the underlying exception.
 * ============================================================================================================
 *  Sr. No.	    Date		          Name				 Description
 * ============================================================================================================
 *  1.	   	    15.10.2014            Apex Team            Initial Version.
 *  2.                                Apex Team            Check SVN.
 * ============================================================================================================
 */
package com.hackathon.smartlogger.gateway.exception;

public class HttpRequestException extends Exception {

	private static final long serialVersionUID = -2413629666163901633L;


	public HttpRequestException(final Exception exception) {
		super(exception);
	}

	@Override
	public String getMessage() {

		return "Server not responding";
	}
}