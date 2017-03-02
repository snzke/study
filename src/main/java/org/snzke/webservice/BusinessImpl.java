package org.snzke.webservice;

import org.snzke.socket.util.SocketUtils;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.rmi.RemoteException;

/**
 * Created by snzke on 2017/3/2.
 */
@WebService(name = "Business", serviceName = "BusinessService", targetNamespace = "http://webservice.snzke.org/client")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class BusinessImpl implements Business {
    @Override
    public String echo(String message) throws RemoteException {
        if("quit".equalsIgnoreCase(message)){
            System.out.println(SocketUtils.getLogTime() + "服务即将关闭...");
            System.exit(0);
        }
        System.out.println(SocketUtils.getLogTime() + "接收到消息：" + message);
        return "服务器返回：" + message;
    }
}
