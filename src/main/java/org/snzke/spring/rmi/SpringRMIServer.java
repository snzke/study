package org.snzke.spring.rmi;

import org.snzke.socket.util.SocketUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by snzke on 2017/3/2.
 */
public class SpringRMIServer {
    public static void main(String [] args){
        new ClassPathXmlApplicationContext("spring-rmi-server.xml");
        System.out.println(SocketUtils.getLogTime() + "SpringRMIServer 已启动...");
    }
}
