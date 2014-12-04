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
public class ProjectSiteTaskDTO implements Serializable, Comparable<ProjectSiteTaskDTO> {

    private static final long serialVersionUID = 1L;
    private Integer projectSiteTaskID;
    private TaskDTO task;
    private long dateRegistered;
    private Integer projectSiteID, projectID;
    private List<ProjectSiteTaskStatusDTO> projectSiteTaskStatusList = new ArrayList<>();
    private List<PhotoUploadDTO> photoUploadList = new ArrayList<>();
    private String projectSiteName, projectName;

    public ProjectSiteTaskDTO() {
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectSiteName() {
        return projectSiteName;
    }

    public void setProjectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
    }

    public List<PhotoUploadDTO> getPhotoUploadList() {
        return photoUploadList;
    }

    public void setPhotoUploadList(List<PhotoUploadDTO> photoUploadList) {
        this.photoUploadList = photoUploadList;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Integer getProjectSiteTaskID() {
        return projectSiteTaskID;
    }

    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }

    public void setProjectSiteTaskID(Integer projectSiteTaskID) {
        this.projectSiteTaskID = projectSiteTaskID;
    }

    public long getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(long dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public Integer getProjectSiteID() {
        return projectSiteID;
    }

    public void setProjectSiteID(Integer projectSiteID) {
        this.projectSiteID = projectSiteID;
    }

    public List<ProjectSiteTaskStatusDTO> getProjectSiteTaskStatusList() {
        return projectSiteTaskStatusList;
    }

    public void setProjectSiteTaskStatusList(List<ProjectSiteTaskStatusDTO> projectSiteTaskStatusList) {
        this.projectSiteTaskStatusList = projectSiteTaskStatusList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectSiteTaskID != null ? projectSiteTaskID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjectSiteTaskDTO)) {
            return false;
        }
        ProjectSiteTaskDTO other = (ProjectSiteTaskDTO) object;
        if ((this.projectSiteTaskID == null && other.projectSiteTaskID != null) || (this.projectSiteTaskID != null && !this.projectSiteTaskID.equals(other.projectSiteTaskID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.ProjectSiteTask[ projectSiteTaskID=" + projectSiteTaskID + " ]";
    }

    /**
     * Compares this object to the specified object to determine their relative
     * order.
     *
     * @param another the object to compare to this instance.
     * @return a negative integer if this instance is less than {@code another};
     * a positive integer if this instance is greater than
     * {@code another}; 0 if this instance has the same order as
     * {@code another}.
     * @throws ClassCastException if {@code another} cannot be converted into something
     *                            comparable to {@code this} instance.
     */
    @Override
    public int compareTo(ProjectSiteTaskDTO another) {
        return 0;
    }
}
