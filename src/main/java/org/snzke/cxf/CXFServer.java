package org.snzke.cxf;

import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

/**
 * Created by snzke on 2017/3/2.
 */
public class CXFServer {
    public static void main(String [] args){
        Business business = new BusinessImpl();
        JaxWsServerFactoryBean serverFactory = new JaxWsServerFactoryBean();
        serverFactory.setServiceClass(Business.class);
        serverFactory.setAddress("http://localhost:9527/business");
        serverFactory.setServiceBean(business);
        serverFactory.create();
    }
}
