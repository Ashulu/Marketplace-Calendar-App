import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TestClient {
    public static void main (String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);

        Socket socket = new Socket("localhost", 5555);

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());

        String action;
        while (true) {
            System.out.println("Action to Test:");
            action = scan.nextLine();

            writer.write(action);
            writer.println();
            writer.flush(); // ensure data is sent to the server
            System.out.printf("Sent to server:\n%s\n", action);

            if (action.equals("quit")) {
                break;
            }

            System.out.println("How many input rounds? ");
            String rounds = scan.nextLine();
            int round = Integer.parseInt(rounds);

            String response;
            for (int i = 0; i < round; i++) {
                System.out.println("input round " + (i + 1) +": ");
                response = scan.nextLine();
                writer.write(response);
                writer.println();
                writer.flush(); // ensure data is sent to the server
                System.out.printf("Sent to server:\n%s\n", response);
            }

            String serverResponse = reader.readLine();
            System.out.println("Received from server:\n" + serverResponse);
        }

        writer.close();
        reader.close();
    }
}
