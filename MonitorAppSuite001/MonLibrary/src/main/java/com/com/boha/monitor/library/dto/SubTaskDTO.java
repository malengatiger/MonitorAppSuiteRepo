package com.com.boha.monitor.library.dto;

import java.io.Serializable;

/**
 *
 * @author aubreyM
 */
public class SubTaskDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer subTaskID;
    private String subTaskName;
    private Integer subTaskNumber;
    private Integer taskID;

    public SubTaskDTO() {
    }


    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public Integer getSubTaskID() {
        return subTaskID;
    }

    public void setSubTaskID(Integer subTaskID) {
        this.subTaskID = subTaskID;
    }

    public String getSubTaskName() {
        return subTaskName;
    }

    public void setSubTaskName(String subTaskName) {
        this.subTaskName = subTaskName;
    }

    public Integer getSubTaskNumber() {
        return subTaskNumber;
    }

    public void setSubTaskNumber(Integer subTaskNumber) {
        this.subTaskNumber = subTaskNumber;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (subTaskID != null ? subTaskID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SubTaskDTO)) {
            return false;
        }
        SubTaskDTO other = (SubTaskDTO) object;
        if ((this.subTaskID == null && other.subTaskID != null) || (this.subTaskID != null && !this.subTaskID.equals(other.subTaskID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.monitor.data.SubTask[ subTaskID=" + subTaskID + " ]";
    }

}

