import java.io.IOException;
import java.net.*;

/**
 * Created by snzke on 2017/3/1.
 */
public class UDP_IP_BIO_ServerAndClient {
    public void init(){
        // 由于UDP/IP是无连接的，如果希望双向通信，就必须启动一个监听端口，承担服务器的职责
        // 如不能绑定到指定端口，则抛出SocketException
        try {
            // 模拟服务器Socket
            DatagramSocket serverSocket = new DatagramSocket(9300);
            byte[] buffer = new byte[65507];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            DatagramSocket socket = new DatagramSocket();
            String sendData = "你好啊";
            byte[] data = sendData.getBytes();
            // 准备需要发送的数据
            DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 9300);
            // 阻塞发送packet到指定的服务器和端口，当出现网络IO异常时抛出IOException
            // 当连不上目标地址和端口时，抛出PortUnreachableException
            socket.send(packet);
            System.out.println("客户端发送数据：" + new String(packet.getData(), "UTF-8"));
            // 阻塞并同步读取流信息，如接收到的流信息比packet长度长，则删除更长的信息
            // 可通过调用DatagramPacket.setSoTimeout(以毫秒为单位的超时时间)来设置读取流的超时时间
//            socket.setSoTimeout(60000);
            serverSocket.receive(receivePacket);
            System.out.println("服务端接收到数据：" + new String(receivePacket.getData(), "UTF-8"));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String [] args){
        new UDP_IP_BIO_ServerAndClient().init();
    }
}
