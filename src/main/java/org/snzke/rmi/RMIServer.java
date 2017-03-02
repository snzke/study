package org.snzke.rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by snzke on 2017/3/2.
 */
public class RMIServer {
    public static void main(String [] args){
        Business business = new BusinessImpl();
        try {
            UnicastRemoteObject.exportObject(business, 7100);
            Registry registry = LocateRegistry.createRegistry(1710);
            registry.bind("BusinessDemo", business);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }

    }
}
