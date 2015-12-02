/**
 * ============================================================================================================
 * File Name       	: ClientTimeOutException.java
 * Author          	: Apex Team @ Endeavour Software Technologies pvt. ltd., Bangalore.
 * Version         	: 1.0.0
 * Copyright       	: Endeavour
 * Description     	:This class handle Network client time out exception.
 * History         	:
 * ============================================================================================================
 *  Sr. No.	    Date		          Name				 Description
 * ============================================================================================================
 *  1.	   	    15.10.2014            Apex Team            Initial Version.
 *  2.                                Apex Team            Check SVN.
 * ============================================================================================================
 */
package com.hackathon.smartlogger.gateway.exception;

public class ClientTimeOutException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs the exception with
	 * @param exception
	 */
	public ClientTimeOutException(final Exception exception) {
		super(exception);
	}

	@Override
	public String getMessage() {

		return "Request Timeout";
	}

}
