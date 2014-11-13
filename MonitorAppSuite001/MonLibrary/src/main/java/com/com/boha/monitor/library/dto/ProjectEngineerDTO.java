package com.com.boha.monitor.library.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author aubreyM
 */
public class ProjectEngineerDTO implements Serializable {
    private Integer engineerID;
    private static final long serialVersionUID = 1L;
    private Integer projectEngineerID;
    private Integer activeFlag;
    private Integer projectID;
    private List<ContractorClaimDTO> contractorClaimList;

    public ProjectEngineerDTO() {
    }

    public ProjectEngineerDTO(Integer projectEngineerID, int engineerID) {
        this.projectEngineerID = projectEngineerID;
    }

    public Integer getProjectEngineerID() {
        return projectEngineerID;
    }

    public void setProjectEngineerID(Integer projectEngineerID) {
        this.projectEngineerID = projectEngineerID;
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

    public List<ContractorClaimDTO> getContractorClaimList() {
        return contractorClaimList;
    }

    public void setContractorClaimList(List<ContractorClaimDTO> contractorClaimList) {
        this.contractorClaimList = contractorClaimList;
    }


    public Integer getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(Integer activeFlag) {
        this.activeFlag = activeFlag;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (projectEngineerID != null ? projectEngineerID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjectEngineerDTO)) {
            return false;
        }
        ProjectEngineerDTO other = (ProjectEngineerDTO) object;
        if ((this.projectEngineerID == null && other.projectEngineerID != null) || (this.projectEngineerID != null && !this.projectEngineerID.equals(other.projectEngineerID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.ProjectEngineer[ projectEngineerID=" + projectEngineerID + " ]";
    }


}

