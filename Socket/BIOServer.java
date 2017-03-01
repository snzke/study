import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by snzke on 2017/2/28.
 */
public class BIOServer {
    public void init(){
        try {
            // 创建对本地指定端口的监听，如端口冲突则抛出SocketException，其他网络IO方面的异常则抛出IOException
            ServerSocket serverSocket = new ServerSocket(9000);
            // 设置等待连接的超时时间，60秒
//            serverSocket.setSoTimeout(60000);
            // 接受客户端建立连接的请求，并返回Socket对象，以便和客户端进行交互，交互的方式和客户端相同
            // 也是通过Socket.getInputStream和Socket.getOutputStream来进行读写操作，
            // 此方法会一直阻塞到有客户端发送建立连接的请求，如果希望此方法最多阻塞一定时间，则要在创建ServerSocket后调用其setSoTimeout(以毫秒为单位的超时时间)
            Socket socket = serverSocket.accept();

            // 创建读取服务端返回流的BufferedReader
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            // 阻塞读取服务端的返回信息，一下代码会阻塞到服务端返回信息或网络IO出现异常为止。
//            // 如果希望在超过一段时间后就不阻塞了，那么要在创建Socket对象后调用socket.setSoTimeout(以毫秒为单位的超时时间)
//            System.out.println(socket + "读取到内容：" + bufferedReader.readLine());

            // 创建向服务器写入流的PrintWriter
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            // 向服务器发送字符串信息，要注意的是，此处即使写入失败也不会跑出异常信息，并且一直会阻塞到写入操作系统或网络IO出现异常为止。
            printWriter.println("11111");
//
            TCP_IP_BIO.readAndSendMsg(socket, "我很高兴", 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String [] args){
        new BIOServer().init();
    }
}
