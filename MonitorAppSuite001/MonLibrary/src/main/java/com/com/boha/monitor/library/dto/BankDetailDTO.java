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
public class BankDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer bankDetailID;
    private String accountNumber;
    private String branchCode;
    private BankDTO bank;
    private Integer companyID;

    public BankDetailDTO() {
    }

    public Integer getBankDetailID() {
        return bankDetailID;
    }

    public void setBankDetailID(Integer bankDetailID) {
        this.bankDetailID = bankDetailID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public BankDTO getBank() {
        return bank;
    }

    public void setBank(BankDTO bank) {
        this.bank = bank;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bankDetailID != null ? bankDetailID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BankDetailDTO)) {
            return false;
        }
        BankDetailDTO other = (BankDetailDTO) object;
        if ((this.bankDetailID == null && other.bankDetailID != null) || (this.bankDetailID != null && !this.bankDetailID.equals(other.bankDetailID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.BankDetail[ bankDetailID=" + bankDetailID + " ]";
    }
    
}
