import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Created by snzke on 2017/3/1.
 */
public class TCP_IP_NIO_Client {
    public void init(){
        try {
            SocketChannel socketChannel = SocketChannel.open();
            // 设置为非阻塞模式
            socketChannel.configureBlocking(false);
            // 对于非阻塞模式，立刻返回false，表示连接正在建立中
            socketChannel.connect(new InetSocketAddress("127.0.0.1",9100));
            Selector selector = Selector.open();
            // 向Channel注册Selector和连接事件
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            SelectionKey selectionKey = SocketUtils.handleSelector(selector);

            int wSize = socketChannel.write(ByteBuffer.wrap("你好".getBytes()));
            // 如未写入，则继续注册感兴趣的写入事件
            if (wSize == 0) {
                selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
            }
            while(true){
                SocketUtils.handleSelector(selector);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String [] args){
        new TCP_IP_NIO_Client().init();
    }
}
