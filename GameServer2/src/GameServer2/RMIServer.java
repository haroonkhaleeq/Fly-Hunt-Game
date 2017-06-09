package GameServer2;

import GameInterface2.Constant;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class RMIServer {
	
	public static void main (String[] args) throws RemoteException, AlreadyBoundException {
		RemoteImple imple = new RemoteImple();
		Registry registry = LocateRegistry.createRegistry(Constant.RMI_PORT);
		registry.bind(Constant.RMI_ID, imple);
		System.out.println("server is running");
	}

	
	   
	   
}
