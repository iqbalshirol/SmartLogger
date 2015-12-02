
package com.hackathon.smartlogger.gateway.request;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.hackathon.smartlogger.ConnectionConstants;
import com.hackathon.smartlogger.DefectDTO;
import com.hackathon.smartlogger.gateway.helper.RequestResponseHandler;
import com.hackathon.smartlogger.gateway.listener.NetworkResponseListener;
import com.hackathon.smartlogger.gateway.listener.RequestCallbackListener;
import com.hackathon.smartlogger.gateway.response.DefectResponse;
import com.hackathon.smartlogger.gateway.response.Response;
import com.hackathon.smartlogger.gateway.response.reponseparser.JsonResponseParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class DefectRequest extends AsyncTask<Object, Void, Void> implements NetworkResponseListener {

	private static final String TAG = "DefectRequest";



	private DefectDTO defectDTO;

	private final RequestCallbackListener uiListener;

	public DefectRequest(DefectDTO defectDTO,
						 RequestCallbackListener uiListener) {
		this.defectDTO=defectDTO;
		this.uiListener = uiListener;
	}

	@Override
	protected Void doInBackground(Object... params) {

		// Form the Login query

		RequestResponseHandler reqHanlder = new RequestResponseHandler();
		Gson gson = new Gson();
		String jsonObj = gson.toJson(defectDTO);
		reqHanlder.createRequestTask(
				ConnectionConstants.SERVER_URL , jsonObj,
				RequestResponseHandler.METHOD_POST, this);

		reqHanlder.processRequest();
		return null;
	}

	@Override
	public void onNetworkResponse(Response responseData) {

		Log.i(TAG, "onNetworkResponse");

		// Parse the response and send to UI Listener
		try {
			uiListener.onResponseReceived(JsonResponseParser.parseResponse(responseData, DefectResponse.class));

		} catch (Exception e) {
			Log.e(TAG, "Exception: " + e.getMessage());
			uiListener.onError(0,"Error");
		}
	}

	@Override
	public void onNetworkError(Response responseData) {
		Log.e(TAG, "onNetworkError");

		try {
			uiListener.onError(responseData.getResponseCode(), responseData.getResponseMessage());

		} catch (Exception e) {
			Log.e(TAG, "Exception: " + e.getMessage());
		}
	}
	public static String formLoginQuery(String user, String password) {
		String passwordURL;
		String userName;
		try {
			userName = URLEncoder.encode(user, ConnectionConstants.ENCODEINGFORMAT);
			passwordURL = URLEncoder.encode(password, ConnectionConstants.ENCODEINGFORMAT);
		} catch (UnsupportedEncodingException e) {
			passwordURL = "";
			userName = "";
		}
		return String.format("%s?%s=%s&%s=%s", "login", "UserNmae", userName, "Password", passwordURL);
	}
}
