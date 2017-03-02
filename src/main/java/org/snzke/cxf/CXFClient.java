package org.snzke.cxf;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.snzke.socket.util.SocketUtils;

/**
 * Created by snzke on 2017/3/2.
 */
public class CXFClient {
    public static void main(String [] args){
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(Business.class);
        factory.setAddress("http://localhost:9527/business");
        Business business = (Business) factory.create();
        System.out.println(SocketUtils.getLogTime() + "客户端收到消息：" + business.echo("你好啊"));
    }
}
