import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Created by snzke on 2017/3/1.
 */
public class TCP_IP_NIO_Server {
    public void init(){
        try{
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ServerSocket serverSocket = ssc.socket();
            serverSocket.bind(new InetSocketAddress(9200));
            ssc.configureBlocking(false);
            Selector selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            while(true){
                SocketUtils.handleSelector(selector);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String [] args){
        new TCP_IP_NIO_Server().init();
    }
}
