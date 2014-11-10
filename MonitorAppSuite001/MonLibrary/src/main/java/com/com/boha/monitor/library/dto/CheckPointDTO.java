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
public class CheckPointDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer checkPointID;
    private String checkPointName, description;
    private Integer companyID;

    public CheckPointDTO(Integer checkPointID) {
        this.checkPointID = checkPointID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCheckPointID() {
        return checkPointID;
    }

    public void setCheckPointID(Integer checkPointID) {
        this.checkPointID = checkPointID;
    }

    public String getCheckPointName() {
        return checkPointName;
    }

    public void setCheckPointName(String checkPointName) {
        this.checkPointName = checkPointName;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

  
    
}
