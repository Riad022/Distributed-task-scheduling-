package Server;

import java.io.Serializable;

public interface Task extends Runnable, Serializable {
    TaskResult execute();
    int getId();
}
