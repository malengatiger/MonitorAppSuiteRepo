/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.com.boha.monitor.library.dto;

import com.boha.monitor.data.Project;
import com.boha.monitor.data.ProjectSiteTask;
import com.boha.monitor.util.FileUtility;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aubreyM
 */
public class ProjectSiteTaskDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer projectSiteTaskID;
    private String taskName;
    private String taskDescription;
    private long dateRegistered;
    private Integer projectSiteID;
    private List<ProjectSiteTaskStatusDTO> projectSiteTaskStatusList = new ArrayList<>();
    private List<String> imageFileNameList;

    public ProjectSiteTaskDTO() {
    }

    public ProjectSiteTaskDTO(ProjectSiteTask a) {
        this.projectSiteTaskID = a.getProjectSiteTaskID();
        this.taskName = a.getTaskName();
        this.taskDescription = a.getTaskDescription();
        this.dateRegistered = a.getDateRegistered().getTime();
        this.projectSiteID = a.getProjectSite().getProjectSiteID();
        Project p = a.getProjectSite().getProject();
        try {
            this.imageFileNameList = FileUtility.getImageFilesTask(p.getCompany().getCompanyID(),
                    p.getProjectID(), a.getProjectSite().getProjectSiteID(), projectSiteTaskID);
        } catch (Exception ex) {
            Logger.getLogger(ProjectSiteTaskDTO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Integer getProjectSiteTaskID() {
        return projectSiteTaskID;
    }

    public void setProjectSiteTaskID(Integer projectSiteTaskID) {
        this.projectSiteTaskID = projectSiteTaskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public List<String> getImageFileNameList() {
        return imageFileNameList;
    }

    public void setImageFileNameList(List<String> imageFileNameList) {
        this.imageFileNameList = imageFileNameList;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
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

}
