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
public class EngineerDTO implements Serializable {
    private Integer companyID;
    private static final long serialVersionUID = 1L;
    private Integer engineerID;
    private String engineerName;
    private String email;
    private String cellphone;
    private List<ContractorClaimDTO> contractorClaimList;
    public static final int
            ACTION_ADD = 411,
            ACTION_UPDATE = 412,
            ACTION_DELETE = 413;

    public EngineerDTO() {
    }


    public Integer getEngineerID() {
        return engineerID;
    }

    public void setEngineerID(Integer engineerID) {
        this.engineerID = engineerID;
    }

    public String getEngineerName() {
        return engineerName;
    }

    public void setEngineerName(String engineerName) {
        this.engineerName = engineerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public List<ContractorClaimDTO> getContractorClaimList() {
        return contractorClaimList;
    }

    public void setContractorClaimList(List<ContractorClaimDTO> contractorClaimList) {
        this.contractorClaimList = contractorClaimList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (engineerID != null ? engineerID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EngineerDTO)) {
            return false;
        }
        EngineerDTO other = (EngineerDTO) object;
        if ((this.engineerID == null && other.engineerID != null) || (this.engineerID != null && !this.engineerID.equals(other.engineerID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.Engineer[ engineerID=" + engineerID + " ]";
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

   
    
}
