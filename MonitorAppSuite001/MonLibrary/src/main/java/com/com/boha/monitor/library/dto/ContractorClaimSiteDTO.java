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
public class ContractorClaimSiteDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer contractorClaimSiteID;
    private Integer contractorClaimID;
    private ProjectSiteDTO projectSite;

    public ContractorClaimSiteDTO() {
    }


    public Integer getContractorClaimSiteID() {
        return contractorClaimSiteID;
    }

    public void setContractorClaimSiteID(Integer contractorClaimSiteID) {
        this.contractorClaimSiteID = contractorClaimSiteID;
    }

    public Integer getContractorClaimID() {
        return contractorClaimID;
    }

    public void setContractorClaimID(Integer contractorClaimID) {
        this.contractorClaimID = contractorClaimID;
    }

  
    public ProjectSiteDTO getProjectSite() {
        return projectSite;
    }

    public void setProjectSite(ProjectSiteDTO projectSite) {
        this.projectSite = projectSite;
    }

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contractorClaimSiteID != null ? contractorClaimSiteID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContractorClaimSiteDTO)) {
            return false;
        }
        ContractorClaimSiteDTO other = (ContractorClaimSiteDTO) object;
        if ((this.contractorClaimSiteID == null && other.contractorClaimSiteID != null) || (this.contractorClaimSiteID != null && !this.contractorClaimSiteID.equals(other.contractorClaimSiteID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.ContractorClaimSite[ contractorClaimSiteID=" + contractorClaimSiteID + " ]";
    }
    
}
