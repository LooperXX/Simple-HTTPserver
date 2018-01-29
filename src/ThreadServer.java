import java.io.*;
import java.net.Socket;

/* ThreadServer *
*
*  This class complete the function of HTTPServer.
*  @author LooperXX
*/
/* http://www.yiibai.com/java/io/帮助了解到了更多的方法 */

public class ThreadServer extends Thread {
    private Socket client;
    private BufferedReader reader;
    private PrintStream writer;
    private static String path_Server = "C:\\Users\\HP\\Desktop\\计网实验二\\HTTPServer";

    public ThreadServer(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        System.out.println(this.getId());/*线程号*/
        try {
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            writer = new PrintStream(client.getOutputStream());
            String firstLineOfRequest = reader.readLine(); /*得到客户端发送的第一行数据并显示*/
            System.out.println("firstLineOfRequest:" + firstLineOfRequest);
            String uri = firstLineOfRequest.split(" ")[1]; /*分离出uri*/
            System.out.println("fileName:" + uri.substring(1, uri.length()));
            File file = new File(path_Server + uri);
            if (file.isDirectory()) {/*拒绝对文件夹的访问*/
                writer.println("HTTP/1.1 403 Forbidden");
                writer.println("Content-Type:text/plain");
                writer.println("Content-Length:19");
                writer.println();
                writer.println("Service Not Support");
            } else if (file.exists()) { /*根据客户端请求文件的后缀名，发送响应类型*/
                writer.println("HTTP/1.1 200 OK");
                if (uri.endsWith(".html")) {
                    writer.println("Content-Type:text/html");
                } else if (uri.endsWith(".jpg")) { /*图像*/
                    writer.println("Content-type:image/jpeg");
                } else if (uri.endsWith(".jpeg")) {
                    writer.println("Content-type:image/jpeg");
                } else if (uri.endsWith(".bmp")) {
                    writer.println("Content-type:application/x-bmp");
                } else if (uri.endsWith(".png")) {
                    writer.println("Content-type:application/x-png");
                } else if (uri.endsWith(".gif")) {
                    writer.println("Content-type:image/gif");
                } else if (uri.endsWith(".txt")) { /*文本*/
                    writer.println("Content-type:text/plain");
                } else if (uri.endsWith(".doc")) {
                    writer.println("Content-type:application/msword");
                } else if (uri.endsWith(".mp4")) { /*音视频*/
                    writer.println("Content-type:video/mpeg4");
                } else if (uri.endsWith(".mp3")) {
                    writer.println("Content-type:audio/mp3");
                } else if (uri.endsWith(".wmv")) {
                    writer.println("Content-type:video/x-ms-wmv");
                } else if (uri.endsWith(".css")) {
                    writer.println("Content-Type:text/css");
                } else if (uri.endsWith(".js")) {
                    writer.println("Content-Type:application/x-javascript");
                } else {
                    writer.println("Content-Type:application/octet-stream");
                }
                FileInputStream filein = new FileInputStream(file);
                OutputStream os = client.getOutputStream();
                System.out.println("[127.0.0.1" + ":" + client.getPort() + "]");
                System.out.println("request: " + path_Server + uri);
                writer.println("Content-Length:" + filein.available());
                writer.println();
                byte[] buffer = new byte[1024];
                int length = 0;
                length = filein.read(buffer);
                while (length != -1) {
                    os.write(buffer, 0, length);
                    length = filein.read(buffer);
                }
                os.flush();
                filein.close();
            } else {
                writer.println("HTTP/1.1 404 Not Found");
                writer.println("Content-Type:text/plain");
                writer.println("Content-Length:21");
                writer.println();
                writer.println("Can't find such files");
            }
            reader.close();
            writer.close();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
