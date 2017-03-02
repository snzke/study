package org.snzke.cxf;

import javax.jws.WebService;

/**
 * Created by snzke on 2017/3/2.
 */
@WebService
public interface Business{
    String echo(String message);
}
