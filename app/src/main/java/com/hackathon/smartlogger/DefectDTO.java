package com.hackathon.smartlogger;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ESTBLR\iqbal.shirol on 1/12/15.
 */
public class DefectDTO {
    @SerializedName("ProblemTitle")
    private String subject;
    @SerializedName("ProblemDesc")
    private String description;
    @SerializedName("Severity")
    private String severity;
    @SerializedName("AuthorEmail")
    private String authorEmail;

    @SerializedName("Priority")
    private String priority;

    @SerializedName("ImageFile")
    private String imageData;
    @SerializedName("ImageName")
    private String imageName;
    @SerializedName("Auther")
    private String author;

    @SerializedName("ProjectID")
    private String projectID;

    /*@SerializedName("ServiceResponseStatus")
    private int success;*/

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }



    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public Object getResponseObj() {
        return responseObj;
    }

    public void setResponseObj(Object responseObj) {
        this.responseObj = responseObj;
    }

    private Object responseObj;

    /*public int isSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }*/

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
}
