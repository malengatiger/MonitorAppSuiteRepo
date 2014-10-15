/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.com.boha.monitor.library.dto.transfer;

import java.util.Date;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class PhotoUploadDTO {

    public interface PhotoUploadedListener {
        public void onPhotoUploaded();
        public void onPhotoUploadFailed();
    }
    public static final int SITE_IMAGE = 1, TASK_IMAGE = 2, PROJECT_IMAGE = 3, STAFF_IMAGE = 4;
    private int companyID, projectID, projectSiteID, projectSiteTaskID, pictureType, companyStaffID;
    private List<String> tags;
    private String imageFilePath, thumbFilePath;

    private Date dateThumbUploaded, dateFullPictureUploaded, dateTaken;

    public int getCompanyStaffID() {
        return companyStaffID;
    }

    public void setCompanyStaffID(int companyStaffID) {
        this.companyStaffID = companyStaffID;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getThumbFilePath() {
        return thumbFilePath;
    }

    public void setThumbFilePath(String thumbFilePath) {
        this.thumbFilePath = thumbFilePath;
    }

    public Date getDateThumbUploaded() {
        return dateThumbUploaded;
    }

    public void setDateThumbUploaded(Date dateThumbUploaded) {
        this.dateThumbUploaded = dateThumbUploaded;
    }

    public Date getDateFullPictureUploaded() {
        return dateFullPictureUploaded;
    }

    public void setDateFullPictureUploaded(Date dateFullPictureUploaded) {
        this.dateFullPictureUploaded = dateFullPictureUploaded;
    }

    public Date getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Date dateTaken) {
        this.dateTaken = dateTaken;
    }

    public int getPictureType() {
        return pictureType;
    }

    public void setPictureType(int pictureType) {
        this.pictureType = pictureType;
    }

    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public int getProjectSiteID() {
        return projectSiteID;
    }

    public void setProjectSiteID(int projectSiteID) {
        this.projectSiteID = projectSiteID;
    }

    public int getProjectSiteTaskID() {
        return projectSiteTaskID;
    }

    public void setProjectSiteTaskID(int projectSiteTaskID) {
        this.projectSiteTaskID = projectSiteTaskID;
    }

    

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
   

}
