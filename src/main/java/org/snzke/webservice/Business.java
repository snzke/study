package org.snzke.webservice;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by snzke on 2017/3/2.
 */
public interface Business extends Remote {
    String echo(String message)throws RemoteException;
}
