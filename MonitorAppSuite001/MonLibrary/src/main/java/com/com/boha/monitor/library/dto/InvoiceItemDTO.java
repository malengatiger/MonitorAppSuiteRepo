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
public class InvoiceItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer invoiceItemID;
    private Integer quantity;
    private Double unitPrice, nettPrice, tax;
    private Integer invoiceID;
    private ProjectSiteTaskDTO task;
    private ProjectSiteDTO projectSite;

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getNettPrice() {
        return nettPrice;
    }

    public void setNettPrice(Double nettPrice) {
        this.nettPrice = nettPrice;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public ProjectSiteTaskDTO getTask() {
        return task;
    }

    public void setTask(ProjectSiteTaskDTO task) {
        this.task = task;
    }

   

    public Integer getInvoiceItemID() {
        return invoiceItemID;
    }

    public void setInvoiceItemID(Integer invoiceItemID) {
        this.invoiceItemID = invoiceItemID;
    }

    public Integer getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }

    public ProjectSiteDTO getProjectSite() {
        return projectSite;
    }

    public void setProjectSite(ProjectSiteDTO projectSite) {
        this.projectSite = projectSite;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

   

    
}
