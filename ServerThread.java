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

        Connection connection = null;
        Statement statement = null;
        try {
            connection = DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println("Error Connecting to Database");
            e.printStackTrace();
        }

        String clientInput = null;

        while (true) {
            try {
                statement = connection.createStatement();
                clientInput = reader.readLine();

                String[] inputList = clientInput.split(" ");
                String command = inputList[0];
                int execute = 0;
//                execute = 1 if select, execute = 2 if update or insert, execute = 3 if delete, execute = 0 to quit
                boolean isAppointment = clientInput.toLowerCase().contains("appointments");
                switch (command) {
                    case "SELECT":
                        execute = 2;
                        break;
                    case "UPDATE":
                    case "INSERT":
                        execute = 1;
                        break;
                    case "DELETE":
                        execute = 3;
                        break;
                    case "QUIT":
                    default:
                        break;
                }

                switch (execute) {
//                execute = 1 if select, execute = 2 if update or insert, execute = 3 if delete, execute = 0 to quit
                    case 1:
                        ResultSet result = null;
                        if (isAppointment) {
                            PreparedStatement preparedStatement = connection.prepareStatement(clientInput);
                            preparedStatement.setString(1, "datetime(timestamp, 'unixepoch') as timestamp");
                            result = preparedStatement.executeQuery();
                        } else {
                            result = statement.executeQuery(clientInput);
                        }


                        StringBuilder builder = new StringBuilder();
                        int columnCount = result.getMetaData().getColumnCount();
                        while (result.next()) {
                            for (int i = 0; i < columnCount;) {
                                builder.append(result.getString(i + 1));
                                if (++i < columnCount) {
                                    builder.append(",");
                                }
                            }
                            builder.append("|");
                        }
                        builder.deleteCharAt(builder.length() - 1);
                        String resultString = builder.toString();
                        writer.write(resultString);
                        writer.flush();
                        break;

                    case 2:
                        int updateResult;
                        if (isAppointment) {
                            PreparedStatement preparedStatement = connection.prepareStatement(clientInput);
                            preparedStatement.setString(1, "timestamp");
                            preparedStatement.setString(2, "strftime('%s', now)");
                            updateResult = preparedStatement.executeUpdate();
                        } else {
                            updateResult = statement.executeUpdate(clientInput);
                        }
                        writer.write(updateResult);
//                            if updateResult = 1, update was successful
                        break;

                    case 3:
                        int deletions = statement.executeUpdate(clientInput);
                        writer.write(deletions);
//                        write how items were removed from db
                        break;

                    default:
                        socket.close();
                        return;
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Could not pass data to / receive data from client");
                return;
            } catch (SQLException e) {
                System.out.println("Error when executing statment. Statement was: " + clientInput);
                e.printStackTrace();
                return;
            }
        }
    }
}
