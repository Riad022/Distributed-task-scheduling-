package Client;

import Server.Task;

public class TaskModel {

    private Task task;
    private int taskType;

    public TaskModel(Task task, int taskType) {
        this.task = task;
        this.taskType = taskType;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }
}
