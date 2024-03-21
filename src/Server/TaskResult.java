package Server;

import java.io.Serializable;

public class TaskResult implements Serializable {
    private int taskId;
    private final Object result;

    public TaskResult(int taskId, Object result) {
        this.taskId = taskId;
        this.result = result;
    }

    public int getTaskId() {
        return taskId;
    }

    public Object getResult() {
        return result;

    }
}
