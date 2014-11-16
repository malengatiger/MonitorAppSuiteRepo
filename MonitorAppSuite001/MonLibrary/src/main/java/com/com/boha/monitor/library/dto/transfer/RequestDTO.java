/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.com.boha.monitor.library.dto.transfer;


import com.com.boha.monitor.library.dto.BankDTO;
import com.com.boha.monitor.library.dto.BankDetailDTO;
import com.com.boha.monitor.library.dto.BeneficiaryDTO;
import com.com.boha.monitor.library.dto.CheckPointDTO;
import com.com.boha.monitor.library.dto.CityDTO;
import com.com.boha.monitor.library.dto.ClientDTO;
import com.com.boha.monitor.library.dto.CompanyDTO;
import com.com.boha.monitor.library.dto.CompanyStaffDTO;
import com.com.boha.monitor.library.dto.ContractorClaimDTO;
import com.com.boha.monitor.library.dto.ContractorClaimSiteDTO;
import com.com.boha.monitor.library.dto.EngineerDTO;
import com.com.boha.monitor.library.dto.GcmDeviceDTO;
import com.com.boha.monitor.library.dto.InvoiceDTO;
import com.com.boha.monitor.library.dto.InvoiceItemDTO;
import com.com.boha.monitor.library.dto.ProjectDTO;
import com.com.boha.monitor.library.dto.ProjectDiaryRecordDTO;
import com.com.boha.monitor.library.dto.ProjectSiteDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskDTO;
import com.com.boha.monitor.library.dto.ProjectSiteTaskStatusDTO;
import com.com.boha.monitor.library.dto.ProjectStatusTypeDTO;
import com.com.boha.monitor.library.dto.TaskDTO;
import com.com.boha.monitor.library.dto.TaskStatusDTO;
import com.com.boha.monitor.library.dto.TownshipDTO;

import java.io.Serializable;

/**
 * @author aubreyM
 */
public class RequestDTO implements Serializable {
    public RequestDTO(Integer requestType) {
        this.requestType = requestType;
    }

    public RequestDTO() {
    }

    private Integer requestType, companyID, companyStaffID, projectID,
            projectSiteID, projectSiteTaskID,loginType,
            countryID, contractorClaimID, invoiceID,
            beneficiaryID, engineerID;
    private String email, pin, gcmRegistrationID;
    private Double latitude, longitude;
    private CompanyDTO company;
    private CompanyStaffDTO companyStaff;
    private ProjectDTO project;
    private EngineerDTO engineer;
    private CityDTO city;
    private TownshipDTO township;
    private ProjectSiteDTO projectSite;
    private GcmDeviceDTO gcmDevice;
    private BankDTO bank;
    private BankDetailDTO bankDetail;
    private ContractorClaimDTO contractorClaim;
    private ContractorClaimSiteDTO contractorClaimSite;
    private InvoiceDTO invoice;
    private InvoiceItemDTO invoiceItem;

    private TaskDTO task;
    private CheckPointDTO checkPoint;
    private TaskStatusDTO taskStatus;
    private ClientDTO client;

    private ProjectSiteTaskDTO projectSiteTask;
    private ProjectDiaryRecordDTO projectDiaryRecord;
    private ProjectSiteTaskStatusDTO projectSiteTaskStatus;
    private ProjectStatusTypeDTO projectStatusType;
    private BeneficiaryDTO beneficiary;
    //register actors
    public static final int
            REGISTER_COMPANY = 1,
            REGISTER_COMPANY_STAFF = 2,
            REGISTER_PROJECT = 3,
            REGISTER_PROJECT_SITE = 4,
            REGISTER_PROJECT_SITE_STAFF = 5,
            REGISTER_CLIENT = 6,
            REGISTER_BENEFICIARY = 7,
            REGISTER_ENGINEER = 8;
    //add stuff
    public static final int
            ADD_PROJECT_SITE_TASK = 11,
            ADD_PROJECT_DIARY_RECORD = 12,
            ADD_PROJECT_SITE_TASK_STATUS = 13,
            ADD_PROJECT_STATUS_TYPE = 14,
            ADD_DEVICE = 17,
            CONNECT_BENEFICIARY_TO_SITE = 18,
            CONNECT_ENGINEER_TO_PROJECT = 19;
    //get stuff
    public static final int
            GET_PROJECT_DATA = 101,
            GET_PROJECT_SITE_DATA = 102,
            GET_SITE_IMAGE_FILENAMES = 103,
            GET_TASK_IMAGE_FILENAMES = 104,
            GET_COMPANY_STAFF = 105,
            GET_TASK_STATUS_LIST = 106,
            GET_COMPANY_STAFF_TYPE_LIST = 107,
            GET_COMPANY_DATA = 108,
            GET_COUNTRY_LIST = 109,
            GET_PROJECT_IMAGES = 110,
            GET_ALL_PROJECT_IMAGES = 113,
            GET_SITE_IMAGES = 111,
            GET_TASK_IMAGES = 112;
    //login's
    public static final int
            LOGIN = 200,
            SEND_GCM_REGISTRATION = 204;
    //lookups
    public static final int
            ADD_COMPANY_TASK = 301,
            ADD_COMPANY_TASK_STATUS = 302,
            ADD_COMPANY_PROJECT_STATUS_TYPE = 303,
            ADD_COMPANY_CHECKPOINT = 304,
            ADD_CITY = 305,
            ADD_TOWNSHIP = 306,
            ADD_SITE_TASK = 307,
            ADD_BANK_DETAILS = 308,
            ADD_BANK = 309;

    //updates
    public static final int
            UPDATE_COMPANY_TASK = 401,
            UPDATE_COMPANY_TASK_STATUS = 402,
            UPDATE_COMPANY_PROJECT_STATUS_TYPE = 403,
            UPDATE_COMPANY_CHECKPOINT = 404,
            UPDATE_PROJECT = 405,
            UPDATE_PROJECT_SITE = 406;
    //invoice * claim
    public static final int
            ADD_INVOICE = 501,
            ADD_INVOICE_ITEM = 502,
            REMOVE_INVOICE_ITEM = 503,
            CREATE_INVOICE_PDF = 504,
            REMOVE_INVOICE = 505,
            REMOVE_CONTRACTOR_CLAIM = 507,
            REMOVE_CONTRACTOR_CLAIM_SITE = 508,
            GET_PROJECT_INVOICES = 506,
            ADD_CONTRACTOR_CLAIM = 511,
            GENERATE_CONTRACTOR_CLAIM_PDF = 513,
            GENERATE_INVOICE_PDF = 514,
            ADD_CONTRACTOR_CLAIM_SITE = 512;
    //reports
    public static final int
            REPORT_PROJECT = 601,
            REPORT_SITE = 602,
            GET_PROJECT_STATUS_LIST = 603,
            GET_PROJECT_SITE_STATUS_LIST = 604;


    public static final String COMPANY_DIR = "company";
    public static final String COMPANY_STAFF_DIR = "companyStaff";
    public static final String PROJECT_DIR = "project";
    public static final String PROJECT_SITE_DIR = "projectsite";
    public static final String TASK_DIR = "task";

    //


    public EngineerDTO getEngineer() {
        return engineer;
    }

    public void setEngineer(EngineerDTO engineer) {
        this.engineer = engineer;
    }

    public Integer getBeneficiaryID() {
        return beneficiaryID;
    }

    public void setBeneficiaryID(Integer beneficiaryID) {
        this.beneficiaryID = beneficiaryID;
    }

    public Integer getEngineerID() {
        return engineerID;
    }

    public void setEngineerID(Integer engineerID) {
        this.engineerID = engineerID;
    }

    public Integer getContractorClaimID() {
        return contractorClaimID;
    }

    public void setContractorClaimID(Integer contractorClaimID) {
        this.contractorClaimID = contractorClaimID;
    }

    public Integer getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }

    public Integer getRequestType() {
        return requestType;
    }

    public void setRequestType(Integer requestType) {
        this.requestType = requestType;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Integer getCompanyStaffID() {
        return companyStaffID;
    }

    public void setCompanyStaffID(Integer companyStaffID) {
        this.companyStaffID = companyStaffID;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Integer getProjectSiteID() {
        return projectSiteID;
    }

    public void setProjectSiteID(Integer projectSiteID) {
        this.projectSiteID = projectSiteID;
    }

    public Integer getProjectSiteTaskID() {
        return projectSiteTaskID;
    }

    public void setProjectSiteTaskID(Integer projectSiteTaskID) {
        this.projectSiteTaskID = projectSiteTaskID;
    }

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    public Integer getCountryID() {
        return countryID;
    }

    public void setCountryID(Integer countryID) {
        this.countryID = countryID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getGcmRegistrationID() {
        return gcmRegistrationID;
    }

    public void setGcmRegistrationID(String gcmRegistrationID) {
        this.gcmRegistrationID = gcmRegistrationID;
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

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    public CompanyStaffDTO getCompanyStaff() {
        return companyStaff;
    }

    public void setCompanyStaff(CompanyStaffDTO companyStaff) {
        this.companyStaff = companyStaff;
    }

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    public TownshipDTO getTownship() {
        return township;
    }

    public void setTownship(TownshipDTO township) {
        this.township = township;
    }

    public ProjectSiteDTO getProjectSite() {
        return projectSite;
    }

    public void setProjectSite(ProjectSiteDTO projectSite) {
        this.projectSite = projectSite;
    }

    public GcmDeviceDTO getGcmDevice() {
        return gcmDevice;
    }

    public void setGcmDevice(GcmDeviceDTO gcmDevice) {
        this.gcmDevice = gcmDevice;
    }

    public BankDTO getBank() {
        return bank;
    }

    public void setBank(BankDTO bank) {
        this.bank = bank;
    }

    public BankDetailDTO getBankDetail() {
        return bankDetail;
    }

    public void setBankDetail(BankDetailDTO bankDetail) {
        this.bankDetail = bankDetail;
    }

    public ContractorClaimDTO getContractorClaim() {
        return contractorClaim;
    }

    public void setContractorClaim(ContractorClaimDTO contractorClaim) {
        this.contractorClaim = contractorClaim;
    }

    public ContractorClaimSiteDTO getContractorClaimSite() {
        return contractorClaimSite;
    }

    public void setContractorClaimSite(ContractorClaimSiteDTO contractorClaimSite) {
        this.contractorClaimSite = contractorClaimSite;
    }

    public InvoiceDTO getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceDTO invoice) {
        this.invoice = invoice;
    }

    public InvoiceItemDTO getInvoiceItem() {
        return invoiceItem;
    }

    public void setInvoiceItem(InvoiceItemDTO invoiceItem) {
        this.invoiceItem = invoiceItem;
    }

    public TaskDTO getTask() {
        return task;
    }

    public void setTask(TaskDTO task) {
        this.task = task;
    }

    public CheckPointDTO getCheckPoint() {
        return checkPoint;
    }

    public void setCheckPoint(CheckPointDTO checkPoint) {
        this.checkPoint = checkPoint;
    }

    public TaskStatusDTO getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatusDTO taskStatus) {
        this.taskStatus = taskStatus;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public ProjectSiteTaskDTO getProjectSiteTask() {
        return projectSiteTask;
    }

    public void setProjectSiteTask(ProjectSiteTaskDTO projectSiteTask) {
        this.projectSiteTask = projectSiteTask;
    }

    public ProjectDiaryRecordDTO getProjectDiaryRecord() {
        return projectDiaryRecord;
    }

    public void setProjectDiaryRecord(ProjectDiaryRecordDTO projectDiaryRecord) {
        this.projectDiaryRecord = projectDiaryRecord;
    }

    public ProjectSiteTaskStatusDTO getProjectSiteTaskStatus() {
        return projectSiteTaskStatus;
    }

    public void setProjectSiteTaskStatus(ProjectSiteTaskStatusDTO projectSiteTaskStatus) {
        this.projectSiteTaskStatus = projectSiteTaskStatus;
    }

    public ProjectStatusTypeDTO getProjectStatusType() {
        return projectStatusType;
    }

    public void setProjectStatusType(ProjectStatusTypeDTO projectStatusType) {
        this.projectStatusType = projectStatusType;
    }

    public BeneficiaryDTO getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(BeneficiaryDTO beneficiary) {
        this.beneficiary = beneficiary;
    }
}
