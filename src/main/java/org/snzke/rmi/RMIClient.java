package org.snzke.rmi;

import org.snzke.socket.util.SocketUtils;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by snzke on 2017/3/2.
 */
public class RMIClient {
    public static void main(String [] args){
        try {
            // 获取127.0.0.1 端口1710中的Registry
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1710);
            // 创建BusinessDemo代理类，并寻找BusinessDemo对象
            // 如服务器上没有对应的对象，则抛出NotBoundException
            // 如通信出现错误，则抛出RemoteException
            Business business = (Business) registry.lookup("BusinessDemo");
            System.out.println(SocketUtils.getLogTime() + "客户端收到消息：" + business.echo("你好啊"));
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
