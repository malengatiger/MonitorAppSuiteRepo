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
public class CompanyStaffDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer companyStaffID, activeFlag;
    private String firstName;
    private String lastName;
    private String email, companyName;
    private String cellphone, pin;
    private CompanyStaffTypeDTO companyStaffType;
    private Integer companyID;
    private List<ProjectSiteStaffDTO> projectSiteStaffList;
    private GcmDeviceDTO gcmDevice;

    public CompanyStaffDTO() {
    }



    public Integer getActiveFlag() {
        return activeFlag;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setActiveFlag(Integer activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Integer getCompanyStaffID() {
        return companyStaffID;
    }

    public void setCompanyStaffID(Integer companyStaffID) {
        this.companyStaffID = companyStaffID;
    }

    public GcmDeviceDTO getGcmDevice() {
        return gcmDevice;
    }

    public void setGcmDevice(GcmDeviceDTO gcmDevice) {
        this.gcmDevice = gcmDevice;
    }
    

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public CompanyStaffTypeDTO getCompanyStaffType() {
        return companyStaffType;
    }

    public void setCompanyStaffType(CompanyStaffTypeDTO companyStaffType) {
        this.companyStaffType = companyStaffType;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
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
        hash += (companyStaffID != null ? companyStaffID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompanyStaffDTO)) {
            return false;
        }
        CompanyStaffDTO other = (CompanyStaffDTO) object;
        if ((this.companyStaffID == null && other.companyStaffID != null) || (this.companyStaffID != null && !this.companyStaffID.equals(other.companyStaffID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.CompanyStaff[ companyStaffID=" + companyStaffID + " ]";
    }
    
}
