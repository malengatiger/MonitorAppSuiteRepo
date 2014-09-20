/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.com.boha.monitor.library.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class TaskStatusDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer taskStatusID;
    private String taskStatusName;
    private List<ProjectSiteTaskStatusDTO> projectSiteTaskStatusList;

    public TaskStatusDTO() {
    }

    public TaskStatusDTO(Integer taskStatusID, String taskStatusName) {
        this.taskStatusID = taskStatusID;
        this.taskStatusName = taskStatusName;
    }

    public Integer getTaskStatusID() {
        return taskStatusID;
    }

    public void setTaskStatusID(Integer taskStatusID) {
        this.taskStatusID = taskStatusID;
    }

    public String getTaskStatusName() {
        return taskStatusName;
    }

    public void setTaskStatusName(String taskStatusName) {
        this.taskStatusName = taskStatusName;
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
        hash += (taskStatusID != null ? taskStatusID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaskStatusDTO)) {
            return false;
        }
        TaskStatusDTO other = (TaskStatusDTO) object;
        if ((this.taskStatusID == null && other.taskStatusID != null) || (this.taskStatusID != null && !this.taskStatusID.equals(other.taskStatusID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.TaskStatus[ taskStatusID=" + taskStatusID + " ]";
    }
    
}
