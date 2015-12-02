/**
 * ============================================================================================================
 * File Name       	: ConnectionConstants.java
 * Author          	: Apex Team @ Endeavour Software Technologies pvt. ltd., Bangalore.
 * Version         	: 1.0.0
 * Copyright       	: Endeavour
 * Description     	: This class stores constants related to Connection/Network Module.
 * History         	:
 * ============================================================================================================
 * Sr. No.	    Date		          Name				 Description
 * ============================================================================================================
 * 1.	   	    15.10.2014           Apex Team            Initial Version.
 * 2.                               Apex Team            Check SVN.
 * ============================================================================================================
 */
package com.hackathon.smartlogger;

public class ConnectionConstants {

    /**
     * Default timeout should be set to 60 secs
     */
    public static final int CONN_TIME_OUT = 60;
    /**
     * Default timeout for read timeout is 60 secs
     */
    public static final int READ_TIME_OUT = 60;

    /**
     * Request content type
     */
    public static final String CONTENTTYPE = "application/x-www-form-urlencoded";

    public static final String CONTENTTYPE_JSON = "application/json";
    public static final String CONTENTTYPE_XML = "text/xml";

    public static final String ENCODEINGFORMAT = "UTF-8";
    public static final String SOAP_ACTION_URL_GET_RATE = "urn:insert_query#insert_query";

    public static final String REQUEST_PROPERTY_CONTENT_TYPE = "Content-Type";
    public static final String REQUEST_PROPERTY_CONTENT_TYPE_VALUE = "text/xml;charset=UTF-8";
    public static final String REQUEST_PROPERTY_AUTHORIZATION = "Authorization";
    public static final String REQUEST_PROPERTY_CONNECTION = "Connection";
    public static final String REQUEST_PROPERTY_VALUE_CONNECTION = "Keep-Alive";
    public static final String REQUEST_PROPERTY_SOAP_ACTION = "SOAPAction";
    public static final String REQUEST_PROPERTY_FILE_TRNSFER_CONTENT_LENGTH = "Content-Length";

    public static final String SERVER_URL =
            "http://192.168.1.13:8086/api/TicketInformation/SaveTicketInformation";

    public static final int CONNECTION_TIME_OUT = 60000;

}
