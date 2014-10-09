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
    private int checkPointID;
    private Date dateRegistered;
    private InvoiceDTO invoice;
    private ProjectSiteDTO projectSite;
    private ProjectSiteStaffDTO projectSiteStaff;

    public SiteCheckPointDTO() {
    }

    public SiteCheckPointDTO(Integer siteCheckPointID) {
        this.siteCheckPointID = siteCheckPointID;
    }

    public SiteCheckPointDTO(Integer siteCheckPointID, int checkPointID, Date dateRegistered) {
        this.siteCheckPointID = siteCheckPointID;
        this.checkPointID = checkPointID;
        this.dateRegistered = dateRegistered;
    }

    public Integer getSiteCheckPointID() {
        return siteCheckPointID;
    }

    public void setSiteCheckPointID(Integer siteCheckPointID) {
        this.siteCheckPointID = siteCheckPointID;
    }

    public int getCheckPointID() {
        return checkPointID;
    }

    public void setCheckPointID(int checkPointID) {
        this.checkPointID = checkPointID;
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
