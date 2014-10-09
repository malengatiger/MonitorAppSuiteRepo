/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.com.boha.monitor.library.dto;

import com.boha.monitor.data.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author aubreyM
 */
public class HappyLetterDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer happyLetterID;
    private Date letterDate;
    private Beneficiary beneficiary;

    public HappyLetterDTO() {
    }

    public HappyLetterDTO(Integer happyLetterID) {
        this.happyLetterID = happyLetterID;
    }

    public HappyLetterDTO(Integer happyLetterID, Date letterDate) {
        this.happyLetterID = happyLetterID;
        this.letterDate = letterDate;
    }

    public Integer getHappyLetterID() {
        return happyLetterID;
    }

    public void setHappyLetterID(Integer happyLetterID) {
        this.happyLetterID = happyLetterID;
    }

    public Date getLetterDate() {
        return letterDate;
    }

    public void setLetterDate(Date letterDate) {
        this.letterDate = letterDate;
    }

    public Beneficiary getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(Beneficiary beneficiary) {
        this.beneficiary = beneficiary;
    }


}
