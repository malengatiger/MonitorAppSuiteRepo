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

public class InvoiceDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer invoiceID;
    private Date invoiceDate;
    private Date invoiceDueDate;
    private Date dateRegistered;
    private CompanyClientDTO companyClient;
    private List<InvoiceItemDTO> invoiceItemList;
    private List<SiteCheckPointDTO> siteCheckPointList;

    public InvoiceDTO() {
    }

    public InvoiceDTO(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }

    public InvoiceDTO(Integer invoiceID, Date invoiceDate) {
        this.invoiceID = invoiceID;
        this.invoiceDate = invoiceDate;
    }

    public Integer getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Date getInvoiceDueDate() {
        return invoiceDueDate;
    }

    public void setInvoiceDueDate(Date invoiceDueDate) {
        this.invoiceDueDate = invoiceDueDate;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public CompanyClientDTO getCompanyClient() {
        return companyClient;
    }

    public void setCompanyClient(CompanyClientDTO companyClient) {
        this.companyClient = companyClient;
    }

  

    public List<InvoiceItemDTO> getInvoiceItemList() {
        return invoiceItemList;
    }

    public void setInvoiceItemList(List<InvoiceItemDTO> invoiceItemList) {
        this.invoiceItemList = invoiceItemList;
    }

    public List<SiteCheckPointDTO> getSiteCheckPointList() {
        return siteCheckPointList;
    }

    public void setSiteCheckPointList(List<SiteCheckPointDTO> siteCheckPointList) {
        this.siteCheckPointList = siteCheckPointList;
    }

   
    
}
