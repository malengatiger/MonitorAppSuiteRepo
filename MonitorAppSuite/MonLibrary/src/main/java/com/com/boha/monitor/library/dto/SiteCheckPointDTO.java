/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.com.boha.monitor.library.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author aubreyM
 */
public class SiteCheckPointDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer siteCheckPointID;
    private CheckPointDTO checkPoint;
    private Date dateRegistered;
    private InvoiceDTO invoice;
    private ProjectSiteDTO projectSite;
    private ProjectSiteStaffDTO projectSiteStaff;


    public Integer getSiteCheckPointID() {
        return siteCheckPointID;
    }

    public void setSiteCheckPointID(Integer siteCheckPointID) {
        this.siteCheckPointID = siteCheckPointID;
    }

    public CheckPointDTO getCheckPoint() {
        return checkPoint;
    }

    public void setCheckPoint(CheckPointDTO checkPoint) {
        this.checkPoint = checkPoint;
    }


    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public InvoiceDTO getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceDTO invoice) {
        this.invoice = invoice;
    }

    public ProjectSiteDTO getProjectSite() {
        return projectSite;
    }

    public void setProjectSite(ProjectSiteDTO projectSite) {
        this.projectSite = projectSite;
    }

    public ProjectSiteStaffDTO getProjectSiteStaff() {
        return projectSiteStaff;
    }

    public void setProjectSiteStaff(ProjectSiteStaffDTO projectSiteStaff) {
        this.projectSiteStaff = projectSiteStaff;
    }

   
    
}
