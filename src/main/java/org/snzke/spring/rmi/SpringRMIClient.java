package org.snzke.spring.rmi;

import org.snzke.socket.util.SocketUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by snzke on 2017/3/2.
 */
public class SpringRMIClient {
    public static void main(String [] args){
        ApplicationContext ac = new ClassPathXmlApplicationContext("spring-rmi-client.xml");
        Business business = ac.getBean(Business.class);
        System.out.println(SocketUtils.getLogTime() + "客户端接收返回消息：" + business.echo("你好啊"));
    }
}
