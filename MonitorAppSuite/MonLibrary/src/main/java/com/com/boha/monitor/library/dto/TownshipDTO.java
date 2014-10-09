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
public class TownshipDTO implements Serializable {

    private Double latitude;
    private Double longitude;
    private static final long serialVersionUID = 1L;
    private Integer townshipID;
    private String townshipName;
    private CityDTO city;
    private List<BeneficiaryDTO> beneficiaryList;

    public TownshipDTO() {
    }

    public TownshipDTO(Integer townshipID) {
        this.townshipID = townshipID;
    }

    public TownshipDTO(Integer townshipID, String townshipName, double latitude, double longitude) {
        this.townshipID = townshipID;
        this.townshipName = townshipName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getTownshipID() {
        return townshipID;
    }

    public void setTownshipID(Integer townshipID) {
        this.townshipID = townshipID;
    }

    public String getTownshipName() {
        return townshipName;
    }

    public void setTownshipName(String townshipName) {
        this.townshipName = townshipName;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    public List<BeneficiaryDTO> getBeneficiaryList() {
        return beneficiaryList;
    }

    public void setBeneficiaryList(List<BeneficiaryDTO> beneficiaryList) {
        this.beneficiaryList = beneficiaryList;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
