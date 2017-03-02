package org.snzke.webservice;

import org.snzke.socket.util.SocketUtils;

import javax.xml.ws.Endpoint;

/**
 * Created by snzke on 2017/3/2.
 */
public class WebServiceServer {
    public static void main(String [] args){
        Endpoint.publish("http://127.0.0.1:7200/BusinessService", new BusinessImpl());
        System.out.println(SocketUtils.getLogTime() + "WebServer 服务已启动...");
    }
}
