package Server;

public class WorkerThread implements Runnable {
    private final TaskQueue taskQueue;

    public WorkerThread(TaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Worker thread num "+Thread.currentThread().threadId() + " is ready...");
                Task task = taskQueue.take();
                System.out.println("thread number "+Thread.currentThread().threadId() +" has taken task number "+task.getId()+" from the queue");

                TaskResult taskResult=task.execute();
                System.out.println("thread number "+Thread.currentThread().threadId() +" has executed his task with id: "+taskResult.getTaskId());
                TaskSchedulerImp.taskResults.add(taskResult);

                System.out.println("thread number "+Thread.currentThread().threadId() +" has added task number "+taskResult.getTaskId() + " to the task results list");


            } catch (InterruptedException e) {
                break;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}