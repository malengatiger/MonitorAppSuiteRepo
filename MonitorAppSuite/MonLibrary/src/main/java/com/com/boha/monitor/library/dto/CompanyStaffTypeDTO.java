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
public class CompanyStaffTypeDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer companyStaffTypeID;
    private String companyStaffTypeName;
    private List<CompanyStaffDTO> companyStaffList;

    public CompanyStaffTypeDTO() {
    }

  
    public Integer getCompanyStaffTypeID() {
        return companyStaffTypeID;
    }

    public void setCompanyStaffTypeID(Integer companyStaffTypeID) {
        this.companyStaffTypeID = companyStaffTypeID;
    }

    public String getCompanyStaffTypeName() {
        return companyStaffTypeName;
    }

    public void setCompanyStaffTypeName(String companyStaffTypeName) {
        this.companyStaffTypeName = companyStaffTypeName;
    }

    public List<CompanyStaffDTO> getCompanyStaffList() {
        return companyStaffList;
    }

    public void setCompanyStaffList(List<CompanyStaffDTO> companyStaffList) {
        this.companyStaffList = companyStaffList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (companyStaffTypeID != null ? companyStaffTypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompanyStaffTypeDTO)) {
            return false;
        }
        CompanyStaffTypeDTO other = (CompanyStaffTypeDTO) object;
        if ((this.companyStaffTypeID == null && other.companyStaffTypeID != null) || (this.companyStaffTypeID != null && !this.companyStaffTypeID.equals(other.companyStaffTypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.CompanyStaffType[ companyStaffTypeID=" + companyStaffTypeID + " ]";
    }
    
}
