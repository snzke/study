package org.snzke.socket.mina;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.snzke.socket.util.SocketUtils;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by snzke on 2017/3/2.
 */
public class TCP_IP_NIO_Server {
    public void init() {
        final IoAcceptor acceptor = new NioSocketAcceptor();
        // 增加一个将发送对象进行序列化以及接收字节流进行反序列化的类到FilterChain
        acceptor.getFilterChain().addLast("StringSerialize", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));

        acceptor.getSessionConfig().setReadBufferSize( 2048 );
        acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );

        IoHandler handler = new IoHandlerAdapter(){
            @Override
            public void exceptionCaught(IoSession arg0, Throwable arg1)
                    throws Exception {
                arg1.printStackTrace();
            }

            @Override
            public void messageReceived(IoSession session, Object message) throws Exception {
                String str = message.toString();
                System.out.println(SocketUtils.getLogTime() + "接受到的消息:" + str);
                if(str.trim().equalsIgnoreCase("quit")) {
                    session.close(true);
                    return;
                }
                session.write("当前服务器时间：" + SocketUtils.getLogTime());
            }

            @Override
            public void messageSent(IoSession arg0, Object arg1) throws Exception {
                System.out.println(SocketUtils.getLogTime() + "发送信息:" + arg1.toString());
            }

            @Override
            public void sessionClosed(IoSession session) throws Exception {
                System.out.println(SocketUtils.getLogTime() + "IP:" + session.getRemoteAddress().toString() + "断开连接");
            }

            @Override
            public void sessionCreated(IoSession session) throws Exception {
                System.out.println(SocketUtils.getLogTime() + " IP:" + session.getRemoteAddress().toString() + "打开连接");
            }

            @Override
            public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
                System.out.println(SocketUtils.getLogTime() + "IDLE " + session.getIdleCount( status ));
            }

            @Override
            public void sessionOpened(IoSession arg0) throws Exception {
                System.out.println(SocketUtils.getLogTime() + "与 " + arg0.getRemoteAddress().toString() + " 建立连接...");
            }
        };
        acceptor.setHandler(handler);
        try {
            acceptor.bind(new InetSocketAddress(8100));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String [] args){
        new TCP_IP_NIO_Server().init();
    }
}
