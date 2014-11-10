/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.com.boha.monitor.library.dto;

import java.io.Serializable;

/**
 *
 * @author aubreyM
 */
public class ProjectDiaryRecordDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer projectDiaryRecordID;
    private long diaryDate;
    private ProjectStatusTypeDTO projectStatusType;

    public ProjectDiaryRecordDTO() {
    }



    public Integer getProjectDiaryRecordID() {
        return projectDiaryRecordID;
    }

    public void setProjectDiaryRecordID(Integer projectDiaryRecordID) {
        this.projectDiaryRecordID = projectDiaryRecordID;
    }

    public long getDiaryDate() {
        return diaryDate;
    }

    public void setDiaryDate(long diaryDate) {
        this.diaryDate = diaryDate;
    }

    

    public ProjectStatusTypeDTO getProjectStatusType() {
        return projectStatusType;
    }

    public void setProjectStatusType(ProjectStatusTypeDTO projectStatusType) {
        this.projectStatusType = projectStatusType;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectDiaryRecordID != null ? projectDiaryRecordID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjectDiaryRecordDTO)) {
            return false;
        }
        ProjectDiaryRecordDTO other = (ProjectDiaryRecordDTO) object;
        if ((this.projectDiaryRecordID == null && other.projectDiaryRecordID != null) || (this.projectDiaryRecordID != null && !this.projectDiaryRecordID.equals(other.projectDiaryRecordID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.ProjectDiaryRecord[ projectDiaryRecordID=" + projectDiaryRecordID + " ]";
    }
    
}
