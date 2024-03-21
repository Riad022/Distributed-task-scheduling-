package Server;


import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TaskSchedulerInterface extends Remote {
     int submitTask ( Task task ) throws RemoteException;
     TaskResult getResult (int taskId ) throws RemoteException;

}
