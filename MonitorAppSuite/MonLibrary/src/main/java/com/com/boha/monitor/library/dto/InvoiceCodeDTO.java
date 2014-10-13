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
public class InvoiceCodeDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer invoiceCodeID;
    private String invoiceCodeName;
    private String invoiceCodeNumber;
    private List<InvoiceItemDTO> invoiceItemList;
    private CompanyDTO company;
    public Integer getInvoiceCodeID() {
        return invoiceCodeID;
    }

    public void setInvoiceCodeID(Integer invoiceCodeID) {
        this.invoiceCodeID = invoiceCodeID;
    }

    public String getInvoiceCodeName() {
        return invoiceCodeName;
    }

    public void setInvoiceCodeName(String invoiceCodeName) {
        this.invoiceCodeName = invoiceCodeName;
    }

    public String getInvoiceCodeNumber() {
        return invoiceCodeNumber;
    }

    public void setInvoiceCodeNumber(String invoiceCodeNumber) {
        this.invoiceCodeNumber = invoiceCodeNumber;
    }

    public List<InvoiceItemDTO> getInvoiceItemList() {
        return invoiceItemList;
    }

    public void setInvoiceItemList(List<InvoiceItemDTO> invoiceItemList) {
        this.invoiceItemList = invoiceItemList;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

   

  
}
