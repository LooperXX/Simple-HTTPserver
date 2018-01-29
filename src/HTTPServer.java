import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/* HTTPServer *
*
*  This class starts ServerSocket and monitors the client connection,
*       divides the sub-thread to handle the client request.
*  @author LooperXX
*/
/* http://www.yiibai.com/java/io/帮助了解到了更多的方法 */

public class HTTPServer {
    public static void main(String[] args) {
        /*启动 ServerSocket,监听到客户端连接，分配子线程处理客户端请求*/
        try {
            ServerSocket server = new ServerSocket(2333);
            Socket socket;
            while (true) {
                socket = server.accept();
                new ThreadServer(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
