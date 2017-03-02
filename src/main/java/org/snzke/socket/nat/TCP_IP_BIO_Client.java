package org.snzke.socket.nat;

import org.snzke.socket.util.SocketUtils;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by snzke on 2017/2/28.
 */
public class TCP_IP_BIO_Client {
    public void init(){
        try {
            // 创建连接，如果域名解析不了会抛出UnknownHostException，当连接不上时会抛出IOException。
            Socket socket = new Socket("127.0.0.1", 9100);
            // 设置连接超时时间，60秒
//            socket.connect(new InetSocketAddress("127.0.0.1", 9000), 6000);
            // 创建向服务器写入流的PrintWriter
//            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
//            // 向服务器发送字符串信息，要注意的是，此处即使写入失败也不会跑出异常信息，并且一直会阻塞到写入操作系统或网络IO出现异常为止。
//            printWriter.println("你好啊，垃圾");
            SocketUtils.readAndSendMsg(socket, "高兴你妹啊", 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String [] args){
        new TCP_IP_BIO_Client().init();
    }
}
