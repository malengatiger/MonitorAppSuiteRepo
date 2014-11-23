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
public class ContractorClaimDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer contractorClaimID, projectEngineerID,
            engineerID, projectID, taskID, siteCount;
    private String claimNumber, projectName, engineerName, taskName;
    private Date claimDate;
    private List<ContractorClaimSiteDTO> contractorClaimSiteList;

    public Integer getSiteCount() {
        return siteCount;
    }

    public void setSiteCount(Integer siteCount) {
        this.siteCount = siteCount;
    }

    public ContractorClaimDTO() {
    }

    public Integer getProjectEngineerID() {
        return projectEngineerID;
    }

    public void setProjectEngineerID(Integer projectEngineerID) {
        this.projectEngineerID = projectEngineerID;
    }

    public Integer getContractorClaimID() {
        return contractorClaimID;
    }

    public void setContractorClaimID(Integer contractorClaimID) {
        this.contractorClaimID = contractorClaimID;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public Date getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }

    public List<ContractorClaimSiteDTO> getContractorClaimSiteList() {
        return contractorClaimSiteList;
    }

    public void setContractorClaimSiteList(List<ContractorClaimSiteDTO> contractorClaimSiteList) {
        this.contractorClaimSiteList = contractorClaimSiteList;
    }

    public Integer getEngineerID() {
        return engineerID;
    }

    public void setEngineerID(Integer engineerID) {
        this.engineerID = engineerID;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getEngineerName() {
        return engineerName;
    }

    public void setEngineerName(String engineerName) {
        this.engineerName = engineerName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contractorClaimID != null ? contractorClaimID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContractorClaimDTO)) {
            return false;
        }
        ContractorClaimDTO other = (ContractorClaimDTO) object;
        if ((this.contractorClaimID == null && other.contractorClaimID != null) || (this.contractorClaimID != null && !this.contractorClaimID.equals(other.contractorClaimID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.ContractorClaim[ contractorClaimID=" + contractorClaimID + " ]";
    }
    
}
