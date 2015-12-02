package com.hackathon.smartlogger.gateway.response.reponseparser;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.hackathon.smartlogger.DefectDTO;
import com.hackathon.smartlogger.gateway.exception.ResponseParseException;
import com.hackathon.smartlogger.gateway.response.Response;
import com.hackathon.smartlogger.gateway.util.ResponseConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonResponseParser {

	private static final String TAG = "ResponseParser";

	public static DefectDTO parseGenericResponse(
			Response responseData) throws ResponseParseException {

		DefectDTO responseDTO;
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
			JSONObject obj = responseData.getResponseObject();

			responseDTO = new DefectDTO();
			if (obj.has(ResponseConstants.KEY_ACTION_ERRORS)) {
				responseDTO = gson.fromJson(obj.toString(), DefectDTO.class);
			} else {
				// responseDTO.setSuccess(responseDTO.isSuccess());
			}

			Log.d(TAG, "ResponseDTO: " + responseDTO);
		} catch (JsonSyntaxException e) {
			throw new ResponseParseException(e);
		}
		return responseDTO;
	}

	public static DefectDTO parseResponse(Response responseData,
			Class<?> dtoClass) throws ResponseParseException {
		
		DefectDTO responseDTO;
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
			JSONObject obj = responseData.getResponseObject();

			responseDTO = new DefectDTO();
			if (obj.has(ResponseConstants.KEY_ACTION_ERRORS)) {
				responseDTO = gson.fromJson(obj.toString(), DefectDTO.class);
			} else {
				responseDTO.setResponseObj(gson.fromJson(obj.toString(), dtoClass));
			}

			Log.d(TAG, "ResponseDTO: " + responseDTO);
			
		} catch (JsonSyntaxException e) {
			throw new ResponseParseException(e);
		}
		return responseDTO;
	}

	public static DefectDTO parseResponse(Response responseData,
			Class<?> dtoClass, String jsonKey) throws ResponseParseException {

		DefectDTO responseDTO;
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
			JSONObject obj = responseData.getResponseObject();

			responseDTO = new DefectDTO();
			if (obj.has(ResponseConstants.KEY_ACTION_ERRORS)) {
				responseDTO = gson.fromJson(obj.toString(), DefectDTO.class);
			} else {
				responseDTO.setResponseObj(gson.fromJson(obj.getJSONObject(jsonKey)
						.toString(), dtoClass));
			}

			Log.d(TAG, "ResponseDTO: " + responseDTO);
		} catch (JsonSyntaxException e) {
			throw new ResponseParseException(e);
		} catch (JSONException e) {
			throw new ResponseParseException(e);
		}
		return responseDTO;
	}

	public static DefectDTO parseListResponse(
			Response responseData, Class<?> dtoArray, String jsonKey) throws ResponseParseException {

		DefectDTO responseDTO;
		try {
			GsonBuilder gsonBuilder = new GsonBuilder();
			final Gson gson = gsonBuilder.create();
			final JSONObject obj = responseData.getResponseObject();

			responseDTO = new DefectDTO();
			if (obj.has(ResponseConstants.KEY_ACTION_ERRORS)) {
				responseDTO = gson.fromJson(obj.toString(), DefectDTO.class);
			} else {
				responseDTO.setResponseObj(gson.fromJson(obj.getJSONArray(jsonKey)
						.toString(), dtoArray));
			}
			Log.d(TAG, "ResponseDTO: " + responseDTO.getResponseObj());
		} catch (JsonSyntaxException e) {
			throw new ResponseParseException(e);
		} catch (JSONException e) {
			throw new ResponseParseException(e);
		}
		return responseDTO;
	}

}
