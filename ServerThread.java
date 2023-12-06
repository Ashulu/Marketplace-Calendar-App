import java.io.*;
import java.net.Socket;
import java.sql.*;

public class ServerThread extends Thread {

    static final String DB_URL = "jdbc:sqlite:marketplacedb.db";
    Socket socket;

    public ServerThread(Socket client) {
        this.socket = client;
    }

    public void run() {
        System.out.println("connected");
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        boolean isSeller = false;

        Connection connection = null;
        Statement statement = null;
        try {
            connection = DriverManager.getConnection(DB_URL);
            System.out.println("Connected to Database");
        } catch (SQLException e) {
            System.out.println("Error Connecting to Database");
            e.printStackTrace();
        }

        String clientAction = null;

        while (true) {
            try {
                statement = connection.createStatement();
                clientAction = reader.readLine();

                switch (clientAction) {
                    case "login":
                        System.out.println("logging in");
                        String returning = login(reader, statement);
                        writer.write(returning);
                        writer.println();
                        writer.flush();
                        break;
                    case "createAccount":
                        break;
                    case "viewCalendar":
                        break;
                    case "requestAppointment":
                        break;
                    case "cancelRequest":
                        break;
                    case "viewApproved":
                        break;
                    case "showStatisticsCustomer":
                        break;
                    case "showStatisticsCustomerOrderByTotal":
                        break;
                    case "showApproved":
                        break;
                    case "approveRequest":
                        break;
                    case "createStore":
                        break;
                    case "createCalendar":
                        break;
                    case "editCalendar":
                        break;
                    case "deleteCalendar":
                        break;
                    case "statisticsSeller":
                        break;
                    case "importCalendar":
                        break;
                    case "exportApprovedRequests":
                        break;
                    case "quit":
                        System.out.println("closing");
                        socket.close();
                        return;
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Could not pass data to / receive data from client");
                return;
            } catch (SQLException e) {
                System.out.println("Error when executing statement. Statement was: " + clientAction);
                e.printStackTrace();
                return;
            }
        }
    }

    public String login(BufferedReader reader, Statement statement) throws IOException, SQLException {
        String returning = null;

        String input = reader.readLine();
        System.out.println("input: " + input);
        String[] inputList = input.split(",");
        System.out.println("inputList made");
        String query = String.format("SELECT password FROM accounts WHERE email == '%s'", inputList[0]);
        System.out.println("query: " + query);
        ResultSet result = statement.executeQuery(query);
        if (result.next()) {
            String dbPassword = result.getString("password");
            System.out.println(dbPassword);
            if (dbPassword.equals(inputList[1])) {
                String dbType = result.getString("type");
                if (dbType.equals("customer")) { // type customer
                    returning = "1";
                } else { // type seller
                    returning = "2";
                }
            }
        } else { //email does not exist
            returning = "-1";
        }
        System.out.println("returning: " + returning);
        return returning;
    }
}
