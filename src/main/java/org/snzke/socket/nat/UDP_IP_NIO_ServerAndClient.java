package org.snzke.socket.nat;

import org.snzke.socket.util.SocketUtils;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

/**
 * Created by snzke on 2017/3/2.
 */
public class UDP_IP_NIO_ServerAndClient {
    public void init(){
        try {
            DatagramChannel receiveChannel = DatagramChannel.open();
            receiveChannel.configureBlocking(false);
            DatagramSocket socket = receiveChannel.socket();
            socket.bind(new InetSocketAddress(9400));

            Selector selector = Selector.open();
            receiveChannel.register(selector, SelectionKey.OP_READ);

            DatagramChannel sendChannel = DatagramChannel.open();
            sendChannel.configureBlocking(false);
            sendChannel.connect(new InetSocketAddress("127.0.0.1", 9400));
            // 阻塞写入流，如发送缓存区已满，则返回0，此时可通过注册SelectionKey.OP_WRITE事件
            // 以便在可写入时再进行写操作，方式和TCP/IP+NIO基本一致
            int result = sendChannel.write(ByteBuffer.wrap("你好啊".getBytes()));
            if(0 == result){
                sendChannel.register(selector, SelectionKey.OP_WRITE);
            }

            // 读取发送的数据
            int keys = selector.select();
            // 如果keys大于0，说明有连接IO事件发生
            if(keys > 0) {
                Set<SelectionKey> keySet = selector.keys();
                for (SelectionKey key : keySet) {
                    // 对于发生连接的事件
                    if (key.isReadable()) {
                        StringBuffer content = new StringBuffer();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        DatagramChannel channel = (DatagramChannel) key.channel();
                        channel.configureBlocking(false);
                        channel.receive(buffer);
                        buffer.flip();
                        while(buffer.hasRemaining()){
                            buffer.get(new byte[buffer.limit()]);
                            content.append(new String(buffer.array()));
                        }
                        buffer.clear();
                        System.out.println(SocketUtils.getLogTime() + "服务端接收到数据：" + content);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main(String [] args){
        new UDP_IP_NIO_ServerAndClient().init();
    }
}
