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
public class TaskDTO implements Serializable, Comparable<TaskDTO> {
    private static final long serialVersionUID = 1L;
    private Integer taskID, companyID, taskNumber;
    private String taskName;
    private String description;
    private List<TaskPriceDTO> taskPriceList;
    private List<SubTaskDTO> subTaskList;

    public static final int ACTION_UPDATE = 2, ACTION_ADD = 1;

    public List<SubTaskDTO> getSubTaskList() {
        return subTaskList;
    }

    public void setSubTaskList(List<SubTaskDTO> subTaskList) {
        this.subTaskList = subTaskList;
    }

    public List<TaskPriceDTO> getTaskPriceList() {
        return taskPriceList;
    }

    public void setTaskPriceList(List<TaskPriceDTO> taskPriceList) {
        this.taskPriceList = taskPriceList;
    }
    public TaskDTO() {}
    public TaskDTO(Integer taskID, String taskName, String description) {
        this.taskID = taskID;
        this.taskName = taskName;
        this.description = description;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Integer getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(Integer taskNumber) {
        this.taskNumber = taskNumber;
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    public int compareTo(TaskDTO another) {
        return this.taskName.compareTo(another.taskName);
    }
}
