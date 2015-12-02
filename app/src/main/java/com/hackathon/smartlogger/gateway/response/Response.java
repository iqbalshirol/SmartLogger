package com.hackathon.smartlogger.gateway.response;

import org.json.JSONObject;

public class Response {

	private int responseCode;

	private String responseMessage;

	private JSONObject responseObject;

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(final int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(final String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public JSONObject getResponseObject() {
		return responseObject;
	}

	public void setResponseObject(final JSONObject responseObject) {
		this.responseObject = responseObject;
	}
}
