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
public class BeneficiaryDTO implements Serializable, Comparable<BeneficiaryDTO> {

    private static final long serialVersionUID = 1L;
    private Integer beneficiaryID, projectID;
    private String firstName;
    private String lastName;
    private String middleName;
    private String iDNumber;
    private String email;
    private String cellphone;
    private Double amountAuthorized, amountPaid;
    private Date dateRegistered, phbDate;
    private CompanyDTO company;
    private TownshipDTO township;

    public static final int ACTION_ADD = 313, ACTION_UPDATE = 314,
            ACTION_DELETE = 315;

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Integer getBeneficiaryID() {
        return beneficiaryID;
    }

    public Double getAmountAuthorized() {
        return amountAuthorized;
    }

    public void setAmountAuthorized(Double amountAuthorized) {
        this.amountAuthorized = amountAuthorized;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public void setBeneficiaryID(Integer beneficiaryID) {
        this.beneficiaryID = beneficiaryID;
    }

    public Date getPhbDate() {
        return phbDate;
    }

    public void setPhbDate(Date phbDate) {
        this.phbDate = phbDate;
    }


    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getIDNumber() {
        return iDNumber;
    }

    public void setIDNumber(String iDNumber) {
        this.iDNumber = iDNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public String getiDNumber() {
        return iDNumber;
    }

    public void setiDNumber(String iDNumber) {
        this.iDNumber = iDNumber;
    }

    public TownshipDTO getTownship() {
        return township;
    }

    public void setTownship(TownshipDTO township) {
        this.township = township;
    }

    /**
     * Compares this object to the specified object to determine their relative
     * order.
     *
     * @param another the object to compare to this instance.
     * @return a negative integer if this instance is less than {@code another};
     * a positive integer if this instance is greater than
     * {@code another}; 0 if this instance has the same order as
     * {@code another}.
     * @throws ClassCastException if {@code another} cannot be converted into something
     *                            comparable to {@code this} instance.
     */
    @Override
    public int compareTo(BeneficiaryDTO another) {
        return this.getFullName().compareTo(another.getFullName());
    }
}
