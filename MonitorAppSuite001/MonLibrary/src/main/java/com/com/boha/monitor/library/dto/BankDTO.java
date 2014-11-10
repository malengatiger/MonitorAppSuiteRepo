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
public class BankDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer bankID;
    private String bankName;
    
    public BankDTO() {
    }

    public BankDTO(Integer bankID, String bankName) {
        this.bankID = bankID;
        this.bankName = bankName;
    }

    public Integer getBankID() {
        return bankID;
    }

    public void setBankID(Integer bankID) {
        this.bankID = bankID;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bankID != null ? bankID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BankDTO)) {
            return false;
        }
        BankDTO other = (BankDTO) object;
        if ((this.bankID == null && other.bankID != null) || (this.bankID != null && !this.bankID.equals(other.bankID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.Bank[ bankID=" + bankID + " ]";
    }
    
}
