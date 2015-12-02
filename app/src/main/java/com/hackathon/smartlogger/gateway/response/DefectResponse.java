package com.hackathon.smartlogger.gateway.response;

/**
 * Created by venkatesh.kolla on 12/2/2015.
 */
public class DefectResponse {

    private String ServiceResponseStatus;
    private String Error;

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public String getServiceResponseStatus() {
        return ServiceResponseStatus;
    }

    public void setServiceResponseStatus(String serviceResponseStatus) {
        ServiceResponseStatus = serviceResponseStatus;
    }
}
