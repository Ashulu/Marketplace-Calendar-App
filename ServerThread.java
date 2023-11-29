import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread extends Thread {
    Socket socket;

    public ServerThread(Socket client) {
        this.socket = client;
    }

    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
