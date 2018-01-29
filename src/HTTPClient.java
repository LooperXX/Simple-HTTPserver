import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

/* HTTPClient *
*
*  This class complete the function of HTTPClient.
*  @author LooperXX
*/
/* http://www.yiibai.com/java/io/帮助了解到了更多的方法 */

public class HTTPClient {
    private static String path_Client = "C:\\Users\\HP\\Desktop\\计网实验二\\HTTPClient";

    public static void main(String[] args) {
        Socket client;
        Scanner scanner = new Scanner(System.in);
        Boolean quit = false;
        while (!quit) {
            try {
                String fileName;
                String link;
                int port;
                System.out.println("Please input HTTP link:");
                link = scanner.nextLine();
                StringTokenizer address = new StringTokenizer(link, "/");
                String host = address.nextToken();/*分离出所需地址*/
                link = link.replace(host + "/", "");
                System.out.println("Please input Port:");
                port = scanner.nextInt();
                client = new Socket(host, port);/*连接到服务器*/
                fileName = address.hasMoreTokens() ? link : "index.html"; /*默认为index.html*/

                PrintStream writer = new PrintStream(client.getOutputStream());
                InputStream in = client.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                writer.println("GET /" + fileName + " HTTP/1.1");
                writer.println("Host:" + host);
                writer.println("Connection:keep-alive");
                writer.println();
                writer.flush();/*发送请求头*/
                String firstLineOfResponse = reader.readLine();//HTTP/1.1 200 ok
                if (!firstLineOfResponse.split(" ")[1].equals("200")) {
                    StringBuffer result = new StringBuffer();
                    String line;
                    result.append(firstLineOfResponse);
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    System.out.println(result);
                    System.out.println("Tranfer Failed\n");
                } else {
                    String secondLineOfResponse = reader.readLine(); //Content-Type:text/html
                    String thirdLineOfResponse = reader.readLine(); //Content-Length:
                    String fourthLineOfResponse = reader.readLine(); //blank line
                /*文件传输*/
                    File file = new File(path_Client + "/" + fileName);
                    byte[] buffer = new byte[1024];
                    OutputStream fout = new FileOutputStream(file);
                    int length = in.read(buffer);
                    while (length != -1) {
                        fout.write(buffer, 0, length);
                        length = in.read(buffer);
                    }
                    fout.close();

                /*打印传输完成后打印信息*/
                    System.out.println(firstLineOfResponse);
                    System.out.println(secondLineOfResponse);
                    System.out.println(thirdLineOfResponse);
                    System.out.println(fourthLineOfResponse);

                    Boolean loop = true;

                    String s;
                    while (loop) {
                        System.out.println("Continue? Please input QUIT or YES.");
                        while ((s = scanner.nextLine()).equals("")) {

                        }
                        if (s.equals("YES")) {
                            loop = false;
                        } else if (s.equals("QUIT")) {
                            quit = true;
                            loop = false;
                        } else {
                            System.out.println("Wrong input! Please input again!");
                        }
                    }
                }
                in.close();/*关闭流、端口与BufferedReader*/
                reader.close();
                writer.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        scanner.close();
    }
}
