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
public class CompanyClientDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer companyClientID;
    private Date dateRegistered;
    private List<InvoiceDTO> invoiceList;
    private ClientDTO client;
    private CompanyDTO company;

    public CompanyClientDTO() {
    }

    public CompanyClientDTO(Integer companyClientID) {
        this.companyClientID = companyClientID;
    }

    public Integer getCompanyClientID() {
        return companyClientID;
    }

    public void setCompanyClientID(Integer companyClientID) {
        this.companyClientID = companyClientID;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public List<InvoiceDTO> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(List<InvoiceDTO> invoiceList) {
        this.invoiceList = invoiceList;
    }


    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

   
    
}
