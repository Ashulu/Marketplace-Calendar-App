import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    Socket socket;

    public ServerThread(Socket client) {
        this.socket = client;
    }

    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        String clientInput = null;

        while (true) {
            try {
                clientInput = reader.readLine();
                switch (clientInput) {
                    case "c":
                        break;
                    case "s":
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
