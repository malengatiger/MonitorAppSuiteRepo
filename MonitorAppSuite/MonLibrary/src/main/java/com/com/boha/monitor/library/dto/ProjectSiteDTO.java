/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.com.boha.monitor.library.dto;

import com.boha.monitor.data.Project;
import com.boha.monitor.data.ProjectSite;
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
public class ProjectSiteDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer projectSiteID;
    private String projectSiteName;
    private Double latitude;
    private Double longitude;
    private Integer activeFlag;
    private List<ProjectSiteTaskDTO> projectSiteTaskList = new ArrayList<>();
    private Integer projectID;
    private List<ProjectSiteStaffDTO> projectSiteStaffList = new ArrayList<>();
    private List<String> imageFileNameList = new ArrayList<>();

    public ProjectSiteDTO() {
    }

    public ProjectSiteDTO(ProjectSite a) {
        this.projectSiteID = a.getProjectSiteID();
        this.projectSiteName = a.getProjectSiteName();
        this.latitude = a.getLatitude();
        this.longitude = a.getLongitude();
        this.activeFlag = a.getActiveFlag();
        this.projectID = a.getProject().getProjectID();
        
        Project p = a.getProject();
        try {
            this.imageFileNameList = FileUtility.getImageFilesSite(p.getCompany().getCompanyID(),
                    p.getProjectID(), a.getProjectSiteID());
        } catch (Exception ex) {
            Logger.getLogger(ProjectSiteTaskDTO.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public List<String> getImageFileNameList() {
        return imageFileNameList;
    }

    public void setImageFileNameList(List<String> imageFileNameList) {
        this.imageFileNameList = imageFileNameList;
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

  
    public List<ProjectSiteStaffDTO> getProjectSiteStaffList() {
        return projectSiteStaffList;
    }

    public void setProjectSiteStaffList(List<ProjectSiteStaffDTO> projectSiteStaffList) {
        this.projectSiteStaffList = projectSiteStaffList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectSiteID != null ? projectSiteID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjectSiteDTO)) {
            return false;
        }
        ProjectSiteDTO other = (ProjectSiteDTO) object;
        if ((this.projectSiteID == null && other.projectSiteID != null) || (this.projectSiteID != null && !this.projectSiteID.equals(other.projectSiteID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.ProjectSite[ projectSiteID=" + projectSiteID + " ]";
    }
    
}
