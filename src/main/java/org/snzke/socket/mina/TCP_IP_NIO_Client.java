package org.snzke.socket.mina;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteTimeoutException;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.snzke.socket.util.SocketUtils;

import java.net.InetSocketAddress;

/**
 * Created by snzke on 2017/3/2.
 */
public class TCP_IP_NIO_Client {
    public void init() {
        // 创建一个线程池大小为CPU核数+1的SocketConnector对象
        SocketConnector connector = new NioSocketConnector(Runtime.getRuntime().availableProcessors() + 1);
        // 设置TCP NoDelay为true
        connector.getSessionConfig().setTcpNoDelay(true);
        // 增加一个将发送对象进行序列化以及接收字节流进行反序列化的类到FilterChain
        connector.getFilterChain().addLast("StringSerialize", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

        IoHandler handler = new IoHandlerAdapter(){
            @Override
            public void exceptionCaught(IoSession session, Throwable throwable)
                    throws Exception {
                if(throwable instanceof WriteTimeoutException){
                    System.out.println(SocketUtils.getLogTime() + "发送消息超时...");
                }
                throwable.printStackTrace();
            }

            /**
             *
             * @param session
             * @param message
             * @throws Exception
             */
            @Override
            public void messageReceived(IoSession session, Object message) throws Exception {
                System.out.println(SocketUtils.getLogTime() + "client接受信息:" + message.toString());
            }

            @Override
            public void messageSent(IoSession session, Object message) throws Exception {
                System.out.println(SocketUtils.getLogTime() + "client发送信息" + message.toString());
            }

            @Override
            public void sessionClosed(IoSession session) throws Exception {
                System.out.println(SocketUtils.getLogTime() + "client与:" + session.getRemoteAddress().toString() + "断开连接");
            }

            @Override
            public void sessionCreated(IoSession session) throws Exception {
                System.out.println(SocketUtils.getLogTime() + "client与:" + session.getRemoteAddress().toString() + "建立连接");
            }

            @Override
            public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
                System.out.println(SocketUtils.getLogTime() + "IDLE " + session.getIdleCount( status ));
            }

            @Override
            public void sessionOpened(IoSession session) throws Exception {
                System.out.println(SocketUtils.getLogTime() + "client 打开连接");
            }
        };
        connector.setHandler(handler);
        // 异步建立连接
        ConnectFuture connectFuture = connector.connect(new InetSocketAddress(8100));
        // 阻塞等待连接建立完毕，如须设置连接创建的超时时间，可调用SocketConnector.setConnectTimeoutMillis(以毫秒为单位的超时时间)
        connectFuture.awaitUninterruptibly();
        IoSession session = connectFuture.getSession();
        WriteFuture writeFuture = session.write("你好啊");
        writeFuture.addListener(future -> {
            // 判断是否写入失败
            if(!((WriteFuture)future).isWritten()){
                System.out.println(SocketUtils.getLogTime() + "消息发送失败...");
            }
        });
        if(session!=null){
            if(session.isConnected()){
                session.getCloseFuture().awaitUninterruptibly();
            }
            connector.dispose(true);
        }
    }
    public static void main(String [] args){
        new TCP_IP_NIO_Client().init();
    }
}
