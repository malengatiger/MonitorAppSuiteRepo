/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.com.boha.monitor.library.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class ProjectSiteTaskStatusDTO implements Serializable, Comparable<ProjectSiteTaskStatusDTO> {
    private static final long serialVersionUID = 1L;
    private Integer projectSiteTaskStatusID;
    private Date dateUpdated;
    private Date statusDate;
    private TaskStatusDTO taskStatus;
    private TaskDTO task;
    private Integer projectSiteTaskID;
    private Integer companyStaffID;
    private String projectSiteName, projectName, staffName;
    private List<SubTaskStatusDTO> subTaskStatusList;

    public ProjectSiteTaskStatusDTO() {
    }

    public List<SubTaskStatusDTO> getSubTaskStatusList() {
        return subTaskStatusList;
    }

    public void setSubTaskStatusList(List<SubTaskStatusDTO> subTaskStatusList) {
        this.subTaskStatusList = subTaskStatusList;
    }

    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }

    
    public Integer getCompanyStaffID() {
        return companyStaffID;
    }

    public void setCompanyStaffID(Integer companyStaffID) {
        this.companyStaffID = companyStaffID;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public String getProjectSiteName() {
        return projectSiteName;
    }

    public void setProjectSiteName(String projectSiteName) {
        this.projectSiteName = projectSiteName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

   

    public Integer getProjectSiteTaskStatusID() {
        return projectSiteTaskStatusID;
    }

    public void setProjectSiteTaskStatusID(Integer projectSiteTaskStatusID) {
        this.projectSiteTaskStatusID = projectSiteTaskStatusID;
    }

    public TaskStatusDTO getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatusDTO taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Integer getProjectSiteTaskID() {
        return projectSiteTaskID;
    }

    public void setProjectSiteTaskID(Integer projectSiteTaskID) {
        this.projectSiteTaskID = projectSiteTaskID;
    }

 
    @Override
    public String toString() {
        return "com.boha.monitor.data.ProjectSiteTaskStatus[ projectSiteTaskStatusID=" + projectSiteTaskStatusID + " ]";
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
    public int compareTo(ProjectSiteTaskStatusDTO another) {
        if (this.statusDate.before(another.statusDate)) {
            return -1;
        }
        if (this.statusDate.after(another.statusDate)) {
            return 1;
        }
        return 0;
    }
}
