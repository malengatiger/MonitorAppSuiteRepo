/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.com.boha.monitor.library.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class ProjectSiteStaffDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer projectSiteStaffID, activeFlag;
    private Date dateRegistered;
    private List<ProjectDiaryRecordDTO> projectDiaryRecordList = new ArrayList<>();
    private List<ProjectSiteTaskStatusDTO> projectSiteTaskStatusList = new ArrayList<>();
    private Integer projectSiteID;
    private CompanyStaffDTO companyStaff;
    private String pin;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    
    public Integer getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Integer activeFlag) {
        this.activeFlag = activeFlag;
    }



    public CompanyStaffDTO getCompanyStaff() {
        return companyStaff;
    }

    public void setCompanyStaff(CompanyStaffDTO companyStaff) {
        this.companyStaff = companyStaff;
    }


    public Integer getProjectSiteStaffID() {
        return projectSiteStaffID;
    }

    public void setProjectSiteStaffID(Integer projectSiteStaffID) {
        this.projectSiteStaffID = projectSiteStaffID;
    }

    public List<ProjectDiaryRecordDTO> getProjectDiaryRecordList() {
        return projectDiaryRecordList;
    }

    public void setProjectDiaryRecordList(List<ProjectDiaryRecordDTO> projectDiaryRecordList) {
        this.projectDiaryRecordList = projectDiaryRecordList;
    }

    public List<ProjectSiteTaskStatusDTO> getProjectSiteTaskStatusList() {
        return projectSiteTaskStatusList;
    }

    public void setProjectSiteTaskStatusList(List<ProjectSiteTaskStatusDTO> projectSiteTaskStatusList) {
        this.projectSiteTaskStatusList = projectSiteTaskStatusList;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public Integer getProjectSiteID() {
        return projectSiteID;
    }

    public void setProjectSiteID(Integer projectSiteID) {
        this.projectSiteID = projectSiteID;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectSiteStaffID != null ? projectSiteStaffID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjectSiteStaffDTO)) {
            return false;
        }
        ProjectSiteStaffDTO other = (ProjectSiteStaffDTO) object;
        if ((this.projectSiteStaffID == null && other.projectSiteStaffID != null) || (this.projectSiteStaffID != null && !this.projectSiteStaffID.equals(other.projectSiteStaffID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.ProjectSiteStaff[ projectSiteStaffID=" + projectSiteStaffID + " ]";
    }

}
