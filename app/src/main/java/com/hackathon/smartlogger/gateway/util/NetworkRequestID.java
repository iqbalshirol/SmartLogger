package com.hackathon.smartlogger.gateway.util;

public enum NetworkRequestID {
	
	
	 REQUEST_CALLBACK_REQ_ID((byte)1),
	 REQUEST_REGISTER_USER_REQ_ID((byte)2),
	 REQUEST_ALREADY_REGISTER_USER_REQ_ID((byte)3),
	 REQUEST_USER_VARIFY_CODE_REQ_ID((byte)4),
	 REQUEST_GET_RATE((byte)5),
	 REQUEST_SHARE_APP((byte)6),
	 REQUEST_SYNC_REQ((byte)7),
	 REQUEST_ACK_SYNC((byte)8),
	 REQUEST_EDIT_PROFILE((byte)9);
	 
	 private final byte value;
	 private NetworkRequestID(byte id) {
		 value = id;
	}
	public byte getValue() { return value; }

}
