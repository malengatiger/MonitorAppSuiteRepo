package com.com.boha.monitor.library.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author aubreyM
 */
public class TaskPriceDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer taskPriceID;
    private Date startDate;
    String taskName;
    private Date endDate;
    private double price;
    private Integer taskID, projectID;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public Integer getTaskPriceID() {
        return taskPriceID;
    }

    public void setTaskPriceID(Integer taskPriceID) {
        this.taskPriceID = taskPriceID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taskPriceID != null ? taskPriceID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaskPriceDTO)) {
            return false;
        }
        TaskPriceDTO other = (TaskPriceDTO) object;
        if ((this.taskPriceID == null && other.taskPriceID != null) || (this.taskPriceID != null && !this.taskPriceID.equals(other.taskPriceID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.TaskPrice[ taskPriceID=" + taskPriceID + " ]";
    }

}
