import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server.java
 *
 * This class provides the main method for our server, creating new thread for each socket accepted.
 *
 * @author Gunyoung Park, Ashish Chenna, Ian Lam, Sanjana Gadaginmath
 *
 * @version
 */

public class Server {

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;

        try {
            serverSocket = new ServerSocket(5555);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            new ServerThread(socket).start();
        }
    }
}

