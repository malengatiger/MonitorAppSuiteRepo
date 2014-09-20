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
public class ProjectStatusTypeDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer projectStatusTypeID;
    private String projectStatusName;
    private List<ProjectDiaryRecordDTO> projectDiaryRecordList;
    private Integer companyID;

    public ProjectStatusTypeDTO() {
    }

    public ProjectStatusTypeDTO(Integer projectStatusTypeID, String projectStatusName) {
        this.projectStatusTypeID = projectStatusTypeID;
        this.projectStatusName = projectStatusName;
    }

    public Integer getProjectStatusTypeID() {
        return projectStatusTypeID;
    }

    public void setProjectStatusTypeID(Integer projectStatusTypeID) {
        this.projectStatusTypeID = projectStatusTypeID;
    }

    public String getProjectStatusName() {
        return projectStatusName;
    }

    public void setProjectStatusName(String projectStatusName) {
        this.projectStatusName = projectStatusName;
    }

    public List<ProjectDiaryRecordDTO> getProjectDiaryRecordList() {
        return projectDiaryRecordList;
    }

    public void setProjectDiaryRecordList(List<ProjectDiaryRecordDTO> projectDiaryRecordList) {
        this.projectDiaryRecordList = projectDiaryRecordList;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectStatusTypeID != null ? projectStatusTypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjectStatusTypeDTO)) {
            return false;
        }
        ProjectStatusTypeDTO other = (ProjectStatusTypeDTO) object;
        if ((this.projectStatusTypeID == null && other.projectStatusTypeID != null) || (this.projectStatusTypeID != null && !this.projectStatusTypeID.equals(other.projectStatusTypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.ProjectStatusType[ projectStatusTypeID=" + projectStatusTypeID + " ]";
    }

}
