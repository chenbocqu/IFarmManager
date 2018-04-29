package com.qican.ifarmmanager.bean;

import java.io.Serializable;
import java.util.ArrayList;


public class TaskList implements Serializable {

    String userId;
    String taskTime;

    ArrayList<Task> tasks;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
}
