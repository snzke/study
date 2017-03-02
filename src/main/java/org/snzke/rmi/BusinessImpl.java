package org.snzke.rmi;

import org.snzke.socket.util.SocketUtils;

import java.rmi.RemoteException;

/**
 * Created by snzke on 2017/3/2.
 */
public class BusinessImpl implements Business {
    @Override
    public String echo(String message) throws RemoteException {
        if("quit".equalsIgnoreCase(message)){
            System.out.println(SocketUtils.getLogTime() + "服务即将关闭...");
            System.exit(0);
        }
        System.out.println(SocketUtils.getLogTime() + "接收到消息：" + message);
        return "服务器当前时间：" + SocketUtils.getLogTime();
    }
}
