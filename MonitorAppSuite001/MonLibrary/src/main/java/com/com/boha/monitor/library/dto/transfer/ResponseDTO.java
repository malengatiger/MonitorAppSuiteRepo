/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.com.boha.monitor.library.dto.transfer;

import com.com.boha.monitor.library.dto.BankDTO;
import com.com.boha.monitor.library.dto.BankDetailDTO;
import com.com.boha.monitor.library.dto.BeneficiaryDTO;
import com.com.boha.monitor.library.dto.CityDTO;
import com.com.boha.monitor.library.dto.ClientDTO;
import com.com.boha.monitor.library.dto.CompanyDTO;
import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.com.boha.monitor.library.dto.ContractorClaimDTO;
import com.com.boha.monitor.library.dto.ContractorClaimSiteDTO;
import com.com.boha.monitor.library.dto.CountryDTO;
import com.com.boha.monitor.library.dto.EngineerDTO;
import com.com.boha.monitor.library.dto.ErrorStoreAndroidDTO;
import com.com.boha.monitor.library.dto.ErrorStoreDTO;
import com.com.boha.monitor.library.dto.HappyLetterDTO;
import com.com.boha.monitor.library.dto.InvoiceDTO;
import com.com.boha.monitor.library.dto.InvoiceItemDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectDiaryRecordDTO;
import com.com.boha.monitor.library.dto.ProjectEngineerDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskStatusDTO;
import com.com.boha.monitor.library.dto.ProjectStatusTypeDTO;
import com.com.boha.monitor.library.dto.ProvinceDTO;
import com.com.boha.monitor.library.dto.SubTaskDTO;
import com.com.boha.monitor.library.dto.SubTaskStatusDTO;
import com.com.boha.monitor.library.dto.TaskDTO;
import com.com.boha.monitor.library.dto.TaskStatusDTO;
import com.com.boha.monitor.library.dto.TownshipDTO;
import com.com.boha.monitor.library.util.PhotoCache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author aubreyM
 */
public class ResponseDTO implements Serializable {

    private double elapsedRequestTimeInSeconds;
    private PhotoCache photoCache;
    private Date lastCacheDate;
    private Integer statusCode,statusCountInPeriod;
    private String message, sessionID, GCMRegistrationID,fileString;
    private List<String> taskImageFileNameList;
    private List<BankDTO> bankList;
    private List<SubTaskStatusDTO> subTaskStatusList;
    private List<SubTaskDTO> subTaskList;
    private List<String> siteImageFileNameList;
    private List<PhotoUploadDTO> photoUploadList = new ArrayList<>();
    private List<TaskStatusDTO> taskStatusList = new ArrayList<>();
    private List<ProjectStatusTypeDTO> projectStatusTypeList = new ArrayList<>();
    private List<ProjectSiteDTO> projectSiteList = new ArrayList<>();
    private List<ProjectDTO> projectList = new ArrayList<>();
    private List<CompanyStaffDTO> companyStaffList = new ArrayList<>();
    private List<ProjectDiaryRecordDTO> projectDiaryRecordList = new ArrayList<>();
    private List<ProjectSiteTaskDTO> projectSiteTaskList = new ArrayList<>();
    private List<ProjectSiteTaskStatusDTO> projectSiteTaskStatusList = new ArrayList<>();
    private List<ErrorStoreDTO> errorStoreList = new ArrayList<>();
    private List<ErrorStoreAndroidDTO> errorStoreAndroidList = new ArrayList<>();
    private List<InvoiceDTO> invoiceList = new ArrayList<>();
    private List<BeneficiaryDTO> beneficiaryList = new ArrayList<>();
    private List<ProvinceDTO> provinceList = new ArrayList<>();
    private List<HappyLetterDTO> happyLetterList = new ArrayList<>();
    private List<ClientDTO> clientList = new ArrayList<>();
    private List<TaskDTO> taskList = new ArrayList<>();
    private List<CityDTO> cityList = new ArrayList<>();
    private List<TownshipDTO> townshipList = new ArrayList<>();
    private List<CountryDTO> countryList = new ArrayList<>();
    private List<ContractorClaimDTO> contractorClaimList = new ArrayList<>();
    private List<ContractorClaimSiteDTO> contractorClaimSiteList = new ArrayList<>();
    private List<InvoiceItemDTO> invoiceItemList = new ArrayList<>();;
    private List<BankDetailDTO> bankDetailList = new ArrayList<>();;
    private List<EngineerDTO> engineerList = new ArrayList<>();;
    private List<ProjectEngineerDTO> projectEngineerList = new ArrayList<>();;
    //
    private CompanyDTO company;
    private CompanyStaffDTO companyStaff;

    public double getElapsedRequestTimeInSeconds() {
        return elapsedRequestTimeInSeconds;
    }

    public void setElapsedRequestTimeInSeconds(double elapsedRequestTimeInSeconds) {
        this.elapsedRequestTimeInSeconds = elapsedRequestTimeInSeconds;
    }

    public List<SubTaskStatusDTO> getSubTaskStatusList() {
        return subTaskStatusList;
    }

    public void setSubTaskStatusList(List<SubTaskStatusDTO> subTaskStatusList) {
        this.subTaskStatusList = subTaskStatusList;
    }

    public Integer getStatusCountInPeriod() {
        return statusCountInPeriod;
    }

    public void setStatusCountInPeriod(Integer statusCountInPeriod) {
        this.statusCountInPeriod = statusCountInPeriod;
    }

    public List<PhotoUploadDTO> getPhotoUploadList() {
        return photoUploadList;
    }

    public void setPhotoUploadList(List<PhotoUploadDTO> photoUploadList) {
        this.photoUploadList = photoUploadList;
    }

    public Date getLastCacheDate() {
        return lastCacheDate;
    }

    public void setLastCacheDate(Date lastCacheDate) {
        this.lastCacheDate = lastCacheDate;
    }

    public List<EngineerDTO> getEngineerList() {
        return engineerList;
    }

    public void setEngineerList(List<EngineerDTO> engineerList) {
        this.engineerList = engineerList;
    }

    public List<SubTaskDTO> getSubTaskList() {
        return subTaskList;
    }

    public void setSubTaskList(List<SubTaskDTO> subTaskList) {
        this.subTaskList = subTaskList;
    }

    public List<ProjectEngineerDTO> getProjectEngineerList() {
        return projectEngineerList;
    }

    public void setProjectEngineerList(List<ProjectEngineerDTO> projectEngineerList) {
        this.projectEngineerList = projectEngineerList;
    }

    public PhotoCache getPhotoCache() {
        return photoCache;
    }

    public void setPhotoCache(PhotoCache photoCache) {
        this.photoCache = photoCache;
    }

    public List<CountryDTO> getCountryList() {
        return countryList;
    }

    public void setCountryList(List<CountryDTO> countryList) {
        this.countryList = countryList;
    }

    public List<CityDTO> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityDTO> cityList) {
        this.cityList = cityList;
    }

    public List<TownshipDTO> getTownshipList() {
        return townshipList;
    }

    public void setTownshipList(List<TownshipDTO> townshipList) {
        this.townshipList = townshipList;
    }

    public List<TaskDTO> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskDTO> taskList) {
        this.taskList = taskList;
    }


    public List<ClientDTO> getClientList() {
        return clientList;
    }

    public void setClientList(List<ClientDTO> clientList) {
        this.clientList = clientList;
    }

    public List<InvoiceDTO> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(List<InvoiceDTO> invoiceList) {
        this.invoiceList = invoiceList;
    }

    public List<BeneficiaryDTO> getBeneficiaryList() {
        return beneficiaryList;
    }

    public void setBeneficiaryList(List<BeneficiaryDTO> beneficiaryList) {
        this.beneficiaryList = beneficiaryList;
    }

    public List<ProvinceDTO> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<ProvinceDTO> provinceList) {
        this.provinceList = provinceList;
    }

    public List<HappyLetterDTO> getHappyLetterList() {
        return happyLetterList;
    }

    public void setHappyLetterList(List<HappyLetterDTO> happyLetterList) {
        this.happyLetterList = happyLetterList;
    }



    public CompanyStaffDTO getCompanyStaff() {
        return companyStaff;
    }

    public void setCompanyStaff(CompanyStaffDTO companyStaff) {
        this.companyStaff = companyStaff;
    }

    public String getGCMRegistrationID() {
        return GCMRegistrationID;
    }

    public void setGCMRegistrationID(String GCMRegistrationID) {
        this.GCMRegistrationID = GCMRegistrationID;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getTaskImageFileNameList() {
        return taskImageFileNameList;
    }

    public void setTaskImageFileNameList(List<String> taskImageFileNameList) {
        this.taskImageFileNameList = taskImageFileNameList;
    }

    public List<String> getSiteImageFileNameList() {
        return siteImageFileNameList;
    }

    public void setSiteImageFileNameList(List<String> siteImageFileNameList) {
        this.siteImageFileNameList = siteImageFileNameList;
    }

    public String getFileString() {
        return fileString;
    }

    public void setFileString(String fileString) {
        this.fileString = fileString;
    }

    public List<ErrorStoreDTO> getErrorStoreList() {
        return errorStoreList;
    }

    public void setErrorStoreList(List<ErrorStoreDTO> errorStoreList) {
        this.errorStoreList = errorStoreList;
    }

    public List<ErrorStoreAndroidDTO> getErrorStoreAndroidList() {
        return errorStoreAndroidList;
    }

    public void setErrorStoreAndroidList(List<ErrorStoreAndroidDTO> errorStoreAndroidList) {
        this.errorStoreAndroidList = errorStoreAndroidList;
    }

    public List<TaskStatusDTO> getTaskStatusList() {
        return taskStatusList;
    }

    public void setTaskStatusList(List<TaskStatusDTO> taskStatusList) {
        this.taskStatusList = taskStatusList;
    }

    public List<ProjectStatusTypeDTO> getProjectStatusTypeList() {
        return projectStatusTypeList;
    }

    public void setProjectStatusTypeList(List<ProjectStatusTypeDTO> projectStatusTypeList) {
        this.projectStatusTypeList = projectStatusTypeList;
    }

    public List<ProjectSiteDTO> getProjectSiteList() {
        return projectSiteList;
    }

    public void setProjectSiteList(List<ProjectSiteDTO> projectSiteList) {
        this.projectSiteList = projectSiteList;
    }

    public List<ProjectDTO> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<ProjectDTO> projectList) {
        this.projectList = projectList;
    }

    public List<CompanyStaffDTO> getCompanyStaffList() {
        return companyStaffList;
    }

    public void setCompanyStaffList(List<CompanyStaffDTO> companyStaffList) {
        this.companyStaffList = companyStaffList;
    }

    public List<BankDTO> getBankList() {
        return bankList;
    }

    public void setBankList(List<BankDTO> bankList) {
        this.bankList = bankList;
    }

    public List<ContractorClaimDTO> getContractorClaimList() {
        return contractorClaimList;
    }

    public void setContractorClaimList(List<ContractorClaimDTO> contractorClaimList) {
        this.contractorClaimList = contractorClaimList;
    }

    public List<ContractorClaimSiteDTO> getContractorClaimSiteList() {
        return contractorClaimSiteList;
    }

    public void setContractorClaimSiteList(List<ContractorClaimSiteDTO> contractorClaimSiteList) {
        this.contractorClaimSiteList = contractorClaimSiteList;
    }

    public List<InvoiceItemDTO> getInvoiceItemList() {
        return invoiceItemList;
    }

    public void setInvoiceItemList(List<InvoiceItemDTO> invoiceItemList) {
        this.invoiceItemList = invoiceItemList;
    }

    public List<BankDetailDTO> getBankDetailList() {
        return bankDetailList;
    }

    public void setBankDetailList(List<BankDetailDTO> bankDetailList) {
        this.bankDetailList = bankDetailList;
    }

    public List<ProjectDiaryRecordDTO> getProjectDiaryRecordList() {
        return projectDiaryRecordList;
    }

    public void setProjectDiaryRecordList(List<ProjectDiaryRecordDTO> projectDiaryRecordList) {
        this.projectDiaryRecordList = projectDiaryRecordList;
    }

    public List<ProjectSiteTaskDTO> getProjectSiteTaskList() {
        return projectSiteTaskList;
    }

    public void setProjectSiteTaskList(List<ProjectSiteTaskDTO> projectSiteTaskList) {
        this.projectSiteTaskList = projectSiteTaskList;
    }

    public List<ProjectSiteTaskStatusDTO> getProjectSiteTaskStatusList() {
        return projectSiteTaskStatusList;
    }

    public void setProjectSiteTaskStatusList(List<ProjectSiteTaskStatusDTO> projectSiteTaskStatusList) {
        this.projectSiteTaskStatusList = projectSiteTaskStatusList;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

}
