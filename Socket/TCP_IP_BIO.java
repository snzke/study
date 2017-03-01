import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

/**
 * Created by snzke on 2017/2/28.
 */
public class TCP_IP_BIO {

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
    public static String getRandomString(int length){
        if(length < 1){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append((char) new Random().nextInt());
        }
        return sb.toString();
    }
}
