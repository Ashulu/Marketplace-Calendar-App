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
                        int returning = 0;
                        String dbPassword = null;
                        String dbType = null;

                        String input = reader.readLine();
                        String[] inputList = input.split(",");
                        String query = String.format("SELECT password FROM accounts WHERE email == %s", inputList[0]);
                        ResultSet result = statement.executeQuery(query);
                        if (result.next()) {
                            dbPassword = result.getString("password");
                            if (dbPassword.equals(inputList[1])) {
                                dbType = result.getString("type");
                                if (dbType.equals("customer")) { // type customer
                                    returning = 1;
                                } else { // type seller
                                    returning = 2;
                                }
                            } else { // password is incorrect
                                returning = 0;
                            }
                        } else { //email does not exist
                            returning = -1;
                        }




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
}
