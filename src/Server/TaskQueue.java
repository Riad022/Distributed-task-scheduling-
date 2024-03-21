package Server;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskQueue {
    private final BlockingQueue<Task> queue;

    public TaskQueue() {
        queue = new LinkedBlockingQueue<>();
    }

    public void add(Task task) throws InterruptedException {
       // System.out.println("task number "+task.getId()+" has been added to the BlockingQueue");
        queue.put(task);
    }

    public Task take() throws InterruptedException {
        Task task = queue.take();
       // System.out.println("task number "+task.getId()+" has been taken from the BlockingQueue");
        return task;
    }


}