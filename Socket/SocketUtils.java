import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Set;

/**
 * Created by snzke on 2017/2/28.
 */
public class SocketUtils {

    public static void readAndSendMsg(Socket socket, String msg, long waitTime){
        try {
            System.out.println(socket + "休眠[" + waitTime + "]毫秒");
            Thread.sleep(waitTime);

            // 创建读取服务端返回流的BufferedReader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 阻塞读取服务端的返回信息，一下代码会阻塞到服务端返回信息或网络IO出现异常为止。
            // 如果希望在超过一段时间后就不阻塞了，那么要在创建Socket对象后调用socket.setSoTimeout(以毫秒为单位的超时时间)
            System.out.println(socket + "读取到内容：" + bufferedReader.readLine());

            // 创建向服务器写入流的PrintWriter
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            // 向服务器发送字符串信息，要注意的是，此处即使写入失败也不会跑出异常信息，并且一直会阻塞到写入操作系统或网络IO出现异常为止。
            printWriter.println(msg);

            // 继续读取消息并发送随机字符
            readAndSendMsg(socket, getRandomString((int) (Math.random() * 10)), (long) (Math.random() * 5000));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取随机字符串
     * @param length
     * @return
     */
    private static String getRandomString(int length){
        if(length < 1){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append((char) new Random().nextInt());
        }
        return sb.toString();
    }

    /**
     * 处理信道选择器
     * @param selector
     * @return
     * @throws IOException
     */
    public static SelectionKey handleSelector(Selector selector) throws IOException {
        // 阻塞至连接IO事件发生，或达到超时时间
        // 如果希望一直等待，可调用无参select方法
        // 如果希望不阻塞直接返回目前是否有连接IO事件，可调用selectNow方法
        int keys = selector.select();
        // 如果keys大于0，说明有连接IO事件发生
        SelectionKey selectionKey = null;
        if(keys > 0){
            Set<SelectionKey> keySet = selector.keys();
            for (SelectionKey key : keySet) {
                // 对于发生连接的事件
                if(key.isConnectable()){
                    SocketChannel sc = (SocketChannel) key.channel();
                    sc.configureBlocking(false);
                    // 注册读取事件到Selector中
                    // 一般不直接注册写事件，因在发送缓冲区未满的情况下，一直是可写状态
                    // 因此如果注册了写入事件，而又不写入数据，很容易造成CPU满载的现象
                    selectionKey = sc.register(selector, SelectionKey.OP_READ);
                    System.out.println(getLogTime() + sc + " 初始化连接...");
                    System.out.println(getLogTime() + sc + " 注册读取事件");
                    sc.finishConnect();
                }
                // 有流可读取
                else if(key.isReadable()){
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    SocketChannel sc = (SocketChannel) key.channel();
                    int readBytes = 0;
                    int result = 0;
                    try {
                        try {
                            while ((result = sc.read(byteBuffer)) > 0) {
                                readBytes += result;
                            }
                        } finally {
                            byteBuffer.flip();
                        }
                        System.out.println(getLogTime() + sc + " 读取到数据：" + byteBufferToString(byteBuffer));
                        System.out.println(getLogTime() + sc + " 发送文本：你好啊");
                        sc.write(ByteBuffer.wrap("你好啊".getBytes()));
                    } finally {
                        if(null != byteBuffer){
                            byteBuffer.clear();
                        }
                    }
                    System.out.println(getLogTime() + sc + " 注册读取事件");
                    sc.register(selector, SelectionKey.OP_READ);
                }
                // 可写入流
                else if(key.isWritable()) {
                    int a = SelectionKey.OP_WRITE;
                    int b = ~SelectionKey.OP_WRITE;
                    int c = key.interestOps();
                    int d = key.interestOps() & (~SelectionKey.OP_WRITE);
                    System.out.println("SelectionKey.OP_WRITE = " + a + ", ~SelectionKey.OP_WRITE = " + b + ", key.interestOps() = " + c + ", key.interestOps() & (~SelectionKey.OP_WRITE); = " + d);
                    // 取消对OP_WRITE事件的注册
                    key.interestOps(d);
                    SocketChannel sc = (SocketChannel) key.channel();
                    // 此步为阻塞操作，直到写入操作系统发送缓冲区或网络IO出现异常，返回的为成功写入的字节数
                    // 当操作系统的发送缓冲区已满，此处会返回0
                    int writtenedSize = sc.write(ByteBuffer.wrap("你好啊".getBytes()));
                    System.out.println(getLogTime() + sc + " 写入文本：你好啊");
                    // 如未写入，则继续注册感兴趣的写入事件
                    if (writtenedSize == 0) {
                        System.out.println(getLogTime() + sc + " 注册写入事件");
                        key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                    }
                }
                // 接收请求事件
                else if(key.isAcceptable()){
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel sc = ssc.accept();
                    if(sc == null){
                        continue;
                    }
                    sc.configureBlocking(false);
                    // 注册读取事件，读取客户端发送的数据
                    sc.register(selector, SelectionKey.OP_READ);
                    System.out.println(getLogTime() + sc + " 接收客户端连接...");
                    System.out.println(getLogTime() + sc + " 注册读取事件");
                }
            }
        }
        return selectionKey;
    }

    public static String byteBufferToString(ByteBuffer buffer) {
        Charset charset;
        CharsetDecoder decoder;
        CharBuffer charBuffer;
        try {
            charset = Charset.forName("UTF-8");
            decoder = charset.newDecoder();
            charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
            return charBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ");
    public static String getLogTime(){
        return sdf.format(new Date());
    }

    public static void main(String [] args){
        System.out.println(SelectionKey.OP_ACCEPT);
        System.out.println(SelectionKey.OP_CONNECT);
        System.out.println(SelectionKey.OP_READ);
        System.out.println(SelectionKey.OP_WRITE);

        System.out.println(~SelectionKey.OP_WRITE);
    }
}
