/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.com.boha.monitor.library.dto;


import com.com.boha.monitor.library.dto.transfer.PhotoUploadDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class ProjectSiteDTO implements Serializable {

    public static final int ACTION_ADD = 1, ACTION_UPDATE = 2, ACTION_DELETE = 3;
    private static final long serialVersionUID = 1L;
    private Integer projectSiteID;
    private String projectSiteName, standErfNumber, projectName;
    private Double latitude;
    private Double longitude;
    private Integer activeFlag;
    private Integer statusCount;
    private ProjectSiteTaskStatusDTO lastTaskStatus;
    private Float accuracy;
    private boolean selected;
    private BeneficiaryDTO beneficiary;
    private HappyLetterDTO happyLetter;
    private List<ProjectSiteTaskDTO> projectSiteTaskList = new ArrayList<>();
    private Integer projectID;
    private List<PhotoUploadDTO> photoUploadList = new ArrayList<>();

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ProjectSiteDTO() {
    }

    public Integer getStatusCount() {
        return statusCount;
    }

    public void setStatusCount(Integer statusCount) {
        this.statusCount = statusCount;
    }

    public ProjectSiteTaskStatusDTO getLastTaskStatus() {
        return lastTaskStatus;
    }

    public void setLastTaskStatus(ProjectSiteTaskStatusDTO lastTaskStatus) {
        this.lastTaskStatus = lastTaskStatus;
    }

    public BeneficiaryDTO getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(BeneficiaryDTO beneficiary) {
        this.beneficiary = beneficiary;
    }

    public HappyLetterDTO getHappyLetter() {
        return happyLetter;
    }

    public void setHappyLetter(HappyLetterDTO happyLetter) {
        this.happyLetter = happyLetter;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Float accuracy) {
        this.accuracy = accuracy;
    }

    
    public String getStandErfNumber() {
        return standErfNumber;
    }

    public void setStandErfNumber(String standErfNumber) {
        this.standErfNumber = standErfNumber;
    }

    public List<PhotoUploadDTO> getPhotoUploadList() {
        return photoUploadList;
    }

    public void setPhotoUploadList(List<PhotoUploadDTO> photoUploadList) {
        this.photoUploadList = photoUploadList;
    }

    public Integer getProjectSiteID() {
        return projectSiteID;
    }

    public void setProjectSiteID(Integer projectSiteID) {
        this.projectSiteID = projectSiteID;
    }

    public String getProjectSiteName() {
        return projectSiteName;
    }

    public void setProjectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
    }

  
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Integer activeFlag) {
        this.activeFlag = activeFlag;
    }

    public List<ProjectSiteTaskDTO> getProjectSiteTaskList() {
        return projectSiteTaskList;
    }

    public void setProjectSiteTaskList(List<ProjectSiteTaskDTO> projectSiteTaskList) {
        this.projectSiteTaskList = projectSiteTaskList;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }


}
