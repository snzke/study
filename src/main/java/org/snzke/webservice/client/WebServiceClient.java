package org.snzke.webservice.client;

/**
 * Created by snzke on 2017/3/2.
 */
public class WebServiceClient {
    public static void main(String [] args){
        BusinessService businessService = new BusinessService();
        Business business = businessService.getBusinessPort();
        business.echo("你好啊");
    }
}
