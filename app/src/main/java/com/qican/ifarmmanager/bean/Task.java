/**
 * 控制任务
 */
package com.qican.ifarmmanager.bean;

import java.io.Serializable;

public class Task implements Serializable {

    // V4
    String sysTypeCode;

    // V3
    String canNo;

    // 服务器需要的格式 v2
    String farmId;
    String farmName;
    String systemDistrict;
    String systemNo;
    String systemId;
    String controlType;
    String controlOperation;
    String commandCategory;
    String command;
    String executionTime;
    String startExecutionTime;
    String controlDeviceId;
    String taskState;
    String remainExecutionTime;
    String systemType;
    String functionName;
    String addResultTime;


    // 服务器需要的格式 v1
    String collectorId;
    String level;

    String controlArea;
    String fertilizationCan;
    String waitTime;
    String stopTime;
    String controllerLogId;
    String taskTime;

    String type;
    String response;


    public String getSysTypeCode() {
        return sysTypeCode;
    }

    public void setSysTypeCode(String sysTypeCode) {
        this.sysTypeCode = sysTypeCode;
    }

    public String getCanNo() {
        return canNo;
    }

    public void setCanNo(String canNo) {
        this.canNo = canNo;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getAddResultTime() {
        return addResultTime;
    }

    public void setAddResultTime(String addResultTime) {
        this.addResultTime = addResultTime;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getRemainExecutionTime() {
        return remainExecutionTime;
    }

    public void setRemainExecutionTime(String remainExecutionTime) {
        this.remainExecutionTime = remainExecutionTime;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getControlDeviceId() {
        return controlDeviceId;
    }

    public void setControlDeviceId(String controlDeviceId) {
        this.controlDeviceId = controlDeviceId;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public String getSystemDistrict() {
        return systemDistrict;
    }

    public void setSystemDistrict(String systemDistrict) {
        this.systemDistrict = systemDistrict;
    }

    public String getSystemNo() {
        return systemNo;
    }

    public void setSystemNo(String systemNo) {
        this.systemNo = systemNo;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getControlOperation() {
        return controlOperation;
    }

    public void setControlOperation(String controlOperation) {
        this.controlOperation = controlOperation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getControllerLogId() {
        return controllerLogId;
    }

    public void setControllerLogId(String controllerLogId) {
        this.controllerLogId = controllerLogId;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getCollectorId() {
        return collectorId;
    }

    public void setCollectorId(String collectorId) {
        this.collectorId = collectorId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCommandCategory() {
        return commandCategory;
    }

    public void setCommandCategory(String commandCategory) {
        this.commandCategory = commandCategory;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getControlArea() {
        return controlArea;
    }

    public void setControlArea(String controlArea) {
        this.controlArea = controlArea;
    }

    public String getControlType() {
        return controlType;
    }

    public void setControlType(String controlType) {
        this.controlType = controlType;
    }

    public String getFertilizationCan() {
        return fertilizationCan;
    }

    public void setFertilizationCan(String fertilizationCan) {
        this.fertilizationCan = fertilizationCan;
    }

    public String getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(String waitTime) {
        this.waitTime = waitTime;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public String getStartExecutionTime() {
        return startExecutionTime;
    }

    public void setStartExecutionTime(String startExecutionTime) {
        this.startExecutionTime = startExecutionTime;
    }

    public enum TaskStatus {
        Running, Waiting, Completed, Blocking
    }

    int id;
    String area;
    String name;
    String startTime;
    String duration;
    TaskStatus status;

    boolean isTank1;
    boolean isTank2;
    boolean isTank3;

    boolean isA, isB, isC, isD, isE;

    /**
     * 以二进制编码形式设置任务状态描述
     *
     * @param num 例子 [ 010 ] [ 11010 ] ，低5位由低到高 分别是A->E,高3位由低到高 分别是 Tank1 -> Tank3
     */
    public void setStatusByNum(int num) {
        for (int i = 0; i < 8; i++) {
            int flag = get(num, i);
            switch (i) {
                case 0:
                    setA(getFlagStatus(flag));
                    break;
                case 1:
                    setB(getFlagStatus(flag));
                    break;
                case 2:
                    setC(getFlagStatus(flag));
                    break;
                case 3:
                    setD(getFlagStatus(flag));
                    break;
                case 4:
                    setE(getFlagStatus(flag));
                    break;

                case 5:
                    setTank1(getFlagStatus(flag));
                    break;
                case 6:
                    setTank2(getFlagStatus(flag));
                    break;
                case 7:
                    setTank3(getFlagStatus(flag));
                    break;
            }
        }

    }

    private boolean getFlagStatus(int flag) {
        return flag == 1 ? true : false;
    }

    public boolean isTank1() {
        return isTank1;
    }

    public void setTank1(boolean tank1) {
        isTank1 = tank1;
    }

    public boolean isTank2() {
        return isTank2;
    }

    public void setTank2(boolean tank2) {
        isTank2 = tank2;
    }

    public boolean isTank3() {
        return isTank3;
    }

    public void setTank3(boolean tank3) {
        isTank3 = tank3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void clearArea() {
        setA(false);
        setB(false);
        setC(false);
        setD(false);
        setE(false);
    }

    public static class Builder {
        private Task task;

        public Builder() {
            task = new Task();
        }

        public Builder setId(int id) {
            task.setId(id);
            return this;
        }

        public Builder setStatusByNum(int num) {
            task.setStatusByNum(num);
            return this;
        }

        public Builder setName(String name) {
            task.setName(name);
            return this;
        }

        public Builder setArea(String area) {
            task.setArea(area);
            return this;
        }

        public Builder setStartTime(String startTime) {
            task.setStartTime(startTime);
            return this;
        }

        public Builder setStopTime(String stopTime) {
            task.setStopTime(stopTime);
            return this;
        }

        public Builder setDuration(String time) {
            task.setDuration(time);
            return this;
        }

        public Builder setStatus(TaskStatus status) {
            task.setStatus(status);
            return this;
        }

        public Builder setFarmId(String farmId) {
            task.setFarmId(farmId);
            return this;
        }

        public Builder setCollectorId(String collectorId) {
            task.setCollectorId(collectorId);
            return this;
        }

        public Builder setControlArea(String area) {
            task.setControlArea(area);
            return this;
        }

        public Builder setFertilizationCan(String can) {
            task.setFertilizationCan(can);
            return this;
        }

        public Builder setExecutionTime(String time) {
            task.setExecutionTime(time);
            return this;
        }

        public Builder setStartExecutionTime(String time) {
            task.setStartExecutionTime(time);
            return this;
        }

        public Builder setTaskTime(String time) {
            task.setTaskTime(time);
            return this;
        }

        public Builder setControlType(String type) {
            task.setControlType(type);
            return this;
        }

        public Task build() {
            return task;
        }
    }

    public String getOperationDesc() {
        String type = "";
        if (this.isTank1())
            type = type + "1 ";
        if (this.isTank2())
            type = type + "2 ";
        if (this.isTank3())
            type = type + "3 ";
        if (type.equals("")) {
            type = "灌溉";
        } else {
            type = "施肥[ 罐 " + type + "]";
        }
        return type;
    }

    /**
     * @param num:要获取二进制值的数
     * @param index:倒数第一位为0，依次类推
     */
    public static int get(int num, int index) {
        return (num & (0x1 << index)) >> index;
    }

    public boolean isA() {
        return isA;
    }

    public void setA(boolean a) {
        isA = a;
    }

    public boolean isB() {
        return isB;
    }

    public void setB(boolean b) {
        isB = b;
    }

    public boolean isC() {
        return isC;
    }

    public void setC(boolean c) {
        isC = c;
    }

    public boolean isD() {
        return isD;
    }

    public void setD(boolean d) {
        isD = d;
    }

    public boolean isE() {
        return isE;
    }

    public void setE(boolean e) {
        isE = e;
    }

    @Override
    public String toString() {
        return "Task{" +
                "farmId='" + farmId + '\'' +
                ", collectorId='" + collectorId + '\'' +
                ", controlArea='" + controlArea + '\'' +
                ", controlType='" + controlType + '\'' +
                ", fertilizationCan='" + fertilizationCan + '\'' +
                ", waitTime='" + waitTime + '\'' +
                ", executionTime='" + executionTime + '\'' +
                ", startExecutionTime='" + startExecutionTime + '\'' +
                ", stopTime='" + stopTime + '\'' +
                ", controllerLogId='" + controllerLogId + '\'' +
                ", taskTime='" + taskTime + '\'' +
                ", area='" + area + '\'' +
                ", name='" + name + '\'' +
                ", startTime='" + startTime + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", response=" + response +
                '}';
    }
}
