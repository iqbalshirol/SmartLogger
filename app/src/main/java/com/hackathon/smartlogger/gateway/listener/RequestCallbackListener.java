package com.hackathon.smartlogger.gateway.listener;

import com.hackathon.smartlogger.DefectDTO;

public interface RequestCallbackListener {

	/**
	 * Process the response once response is received from the Connection
	 * handler.
	 */
	void onResponseReceived(DefectDTO responseData);

	/**
	 * Process the response once errror occurred from the Connection handler.
	 */
	void onError(int code, String message);
}
