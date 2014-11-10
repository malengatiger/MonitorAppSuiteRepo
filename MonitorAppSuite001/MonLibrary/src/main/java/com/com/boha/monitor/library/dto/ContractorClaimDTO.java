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
    private Integer contractorClaimID;
    private String claimNumber;
    private Date claimDate;
    private List<ContractorClaimSiteDTO> contractorClaimSiteList;
    private EngineerDTO engineer;
    private ProjectDTO project;
    private TaskDTO task;

    public ContractorClaimDTO() {
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

    public EngineerDTO getEngineer() {
        return engineer;
    }

    public void setEngineer(EngineerDTO engineer) {
        this.engineer = engineer;
    }

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
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
