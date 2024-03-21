package Server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Main {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException, InterruptedException {

        TaskSchedulerServer taskSchedulerServer = new TaskSchedulerServer();

        TaskSchedulerInterface taskScheduler= new TaskSchedulerImp(taskSchedulerServer);

        TaskSchedulerInterface skeleton=(TaskSchedulerInterface) UnicastRemoteObject.exportObject(taskScheduler, 0);

        Registry registry = LocateRegistry.getRegistry("127.0.0.1",2022);
        registry.bind("taskScheduler", skeleton );
        System.out.println("Server ready...");
        taskSchedulerServer.start();// start infinite loop
    }
}
