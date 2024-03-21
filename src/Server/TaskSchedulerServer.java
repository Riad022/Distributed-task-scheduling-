package Server;



import java.util.concurrent.*;

public class TaskSchedulerServer {
    private static final int NUM_WORKER_THREADS = 8;
    private final ExecutorService threadPool; //
    private final TaskQueue taskQueue; // task queue

    public TaskSchedulerServer() {
        //add number of threads in the thread pool
        threadPool = Executors.newFixedThreadPool(NUM_WORKER_THREADS);
        //start the task queue
        taskQueue = new TaskQueue();
    }

    public void start() throws InterruptedException {
        // Start worker threads to process tasks from the queue
        for (int i = 0; i < NUM_WORKER_THREADS; i++) {
            // add threads to the thread pool
            threadPool.submit(new WorkerThread(taskQueue));//worker threads
        }

    }

    public TaskQueue getTaskQueue() {
        return taskQueue;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }
}
