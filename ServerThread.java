import java.io.*;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ServerThread extends Thread {

    private static String clientEmail = null;
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
                        String loginResult = login(reader, statement);
                        writer.write(loginResult);
                        writer.println();
                        writer.flush();
                        break;
                    case "createAccount":
                        String createAccountResult = createAccount(reader, statement);
                        writer.write(createAccountResult);
                        writer.println();
                        writer.flush();
                        break;
                    case "viewCalendar":
                        viewCalendar(statement, writer);
                        break;
                    case "requestAppointment":
                        requestAppointment(reader, writer, statement);
                        break;
                    case "cancelRequest":
                        cancelRequest(reader, statement, writer);
                        break;
                    case "viewApproved":
                        String approved = viewApproved(statement);
                        writer.write(approved);
                        writer.println();
                        writer.flush();
                        break;
                    case "showStatisticsCustomer":
                        showStatisticsCustomer(statement, writer);
                        break;
                    case "showStatisticsCustomerOrderByTotal":
                        showStatisticsCustomerOrderByTotal(statement, writer);
                        break;
                    case "showApproved":
                        String approvedSeller = showApproved(statement);
                        writer.write(approvedSeller);
                        writer.println();
                        writer.flush();
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
        String returning;

        System.out.println("logging in");
        String input = reader.readLine();
        System.out.println("input: " + input);
        String[] inputList = input.split(",");
        System.out.println("inputList made");
        String query = String.format("SELECT type, password FROM accounts WHERE email == '%s'", inputList[0]);
        System.out.println("query: " + query);
        ResultSet result = statement.executeQuery(query);
        if (result.next()) {
            String dbPassword = result.getString("password");
            System.out.println("dbPassword: " + dbPassword);
            if (dbPassword.equals(inputList[1])) {
                String dbType = result.getString("type");
                clientEmail = inputList[0];
                if (dbType.equals("customer")) { // type customer
                    returning = "1";
                } else { // type seller
                    returning = "2";
                }
            } else { //password did not match
                returning = "0";
            }
        } else { //email does not exist
            returning = "-1";
        }
        System.out.println("returning: " + returning);
        return returning;
    }

    public String createAccount(BufferedReader reader, Statement statement) throws IOException, SQLException {
        String returning = null;

        System.out.println("creating account");
        String input = reader.readLine();
        System.out.println("input: " + input);
        String[] inputList = input.split(",");
        System.out.println("inputList made");
        String inputType = inputList[0];
        String inputEmail = inputList[1];
        String inputPassword = inputList[2];

        String query = String.format("SELECT COUNT(email) AS count FROM accounts WHERE email == '%s'", inputEmail);
        System.out.println("querying");
        ResultSet result = statement.executeQuery(query);
        result.next();
        int count = result.getInt("count");
        System.out.println("count: " + count);
        if (count != 0) {
            System.out.println("Email exists!");
            returning = "0";
        } else {
            String update = String.format("INSERT INTO accounts (type, email, password) VALUES ('%s','%s','%s')",
                inputType, inputEmail, inputPassword);
            int updateResult = statement.executeUpdate(update);
            if (updateResult == 1) {
                System.out.println("Email added");
                returning = "1";
            } else {
                System.out.println("Nothing added");
                returning = "0";
            }
        }
        return returning;


    }

    public void viewCalendar(Statement statement, PrintWriter writer) throws SQLException {
        ArrayList<String[]> calendarNames = new ArrayList<>();
        ArrayList<String[]> windowArrayList = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery("SELECT calendarName, calendarDescription FROM calendars");
        while (resultSet.next()) {
            String[] calendarNameDescription = new String[2];
            calendarNameDescription[0] = resultSet.getString("calendarName");
            calendarNameDescription[1] = resultSet.getString("calendarDescription");
            calendarNames.add(calendarNameDescription);
        }
        for (int i = 0; i < calendarNames.size(); i++) {
            String query = String.format("SELECT appointmentTitle, startTime, endTime, maxAttendees, currentBookings " +
                "WHERE calendarName == '%s'", calendarNames.get(i)[0]);
            ResultSet windowResult = statement.executeQuery(query);
            String[] windowArray = new String[5];
            windowArray[0] = windowResult.getString("appointmentTitle");
            windowArray[1] = windowResult.getString("startTime");
            windowArray[2] = windowResult.getString("endTime");
            windowArray[3] = windowResult.getString("maxAttendees");
            windowArray[4] = windowResult.getString("currentBookings");
            windowArrayList.add(windowArray);
        }

        String firstOutput = arraylistToString(windowArrayList);
        writer.write(firstOutput);
        writer.println();
        writer.flush();

        String secondOutput = arraylistToString(calendarNames);
        writer.write(secondOutput);
        writer.println();
        writer.flush();
    }

    public void requestAppointment(BufferedReader reader, PrintWriter writer, Statement statement) throws SQLException,
        IOException {
        ResultSet calendarQuery = statement.executeQuery("SELECT * FROM calendars");
        ArrayList<String[]> calendarQueryResult = new ArrayList<>();
        while (calendarQuery.next()) {
            String[] calendarArray = new String[3];
            calendarArray[0] = calendarQuery.getString("storeName");
            calendarArray[1] = calendarQuery.getString("calendarName");
            calendarArray[2] = calendarQuery.getString("calendarDescription");
            calendarQueryResult.add(calendarArray);
        }
        String firstOutput = arraylistToString(calendarQueryResult);
        writer.write(firstOutput);
        writer.println();
        writer.flush();

        String firstInput = reader.readLine();
        String[] firstInputList = firstInput.split(",");
        String inputStore = firstInputList[0];
        String inputCalendar = firstInputList[1];
        String windowQueryStatement = String.format("SELECT startTime, endTime, maxAttendees, currentBookings FROM " +
            "windows WHERE (storeName == '%s' AND calendarName == '%s' AND currentBookings < maxAttendees",
            inputStore, inputCalendar);
        ResultSet windowQuery = statement.executeQuery(windowQueryStatement);
        ArrayList<String[]> windowQueryResult = new ArrayList<>();
        while (windowQuery.next()) {
            String[] windowArray = new String[4];
            windowArray[0] = windowQuery.getString("startTime");
            windowArray[1] = windowQuery.getString("endTime");
            windowArray[2] = windowQuery.getString("maxAttendees");
            windowArray[3] = windowQuery.getString("currentBookings");
            windowQueryResult.add(windowArray);
        }
        String secondOutput = arraylistToString(windowQueryResult);
        writer.write(secondOutput);
        writer.println();
        writer.flush();

        String secondInput = reader.readLine();
        String thirdOutput;
        String[] secondInputList = secondInput.split(",");
        String inputStartTime = secondInputList[0];
        String inputEndTime = secondInputList[1];
        int inputBooking = Integer.parseInt(secondInputList[3]);
        String bookingQueryStatement = String.format("SELECT maxAttendees, currentBookings FROM windows WHERE " +
            "(storeName == '%s' AND calendarName == '%s' AND startTime == '%s' AND endTime == '%s'", inputStore,
            inputCalendar, inputStartTime, inputEndTime);
        ResultSet bookingQuery = statement.executeQuery(bookingQueryStatement);
        bookingQuery.next();
        if (bookingQuery.getInt("maxAttendees") >= (bookingQuery.getInt("currentBookings") + inputBooking)) {
            String sellerQueryStatement = String.format("SELECT sellerName FROM stores WHERE storeName == '%s'",
                inputStore);
            ResultSet sellerQuery = statement.executeQuery(sellerQueryStatement);
            sellerQuery.next();
            String sellerEmail = sellerQuery.getString("sellerName");
            String updateStatement = String.format("INSERT INTO appointments (customerEmail, sellerEmail, storeName, " +
                "calendarName, startTime, endTime, booking, isApproved, isRequest, timeStamp) VALUES ('%s', '%s', '%s', " +
                "'%s', '%s', '%s', '%s', 0, 1, strftime('%%s', 'now'))", clientEmail, sellerEmail, inputStore,
                inputCalendar, inputStartTime, inputEndTime, inputBooking);
            int updates = statement.executeUpdate(updateStatement);
            thirdOutput = String.valueOf(updates);
        } else {
            thirdOutput = "0";
        }
        writer.write(thirdOutput);
        writer.println();
        writer.flush();
    }

    public String arraylistToString(ArrayList<String[]> arrayList) {
        if (arrayList.isEmpty()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < arrayList.size(); j++) {
            stringBuilder.append(Arrays.toString(arrayList.get(j)));
            stringBuilder.append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return String.valueOf(stringBuilder);
    }

    public void cancelRequest(BufferedReader reader, Statement statement, PrintWriter writer) throws IOException,
        SQLException {

        ArrayList<String[]> requests = new ArrayList<>();
        String requestQueryStatement = String.format("SELECT storeName, calendarName, startTime, endTime, booking " +
            "FROM appointments WHERE (customerEmail == '%s' AND isApproved == 0)", clientEmail);
        ResultSet requestQuery = statement.executeQuery(requestQueryStatement);
        while (requestQuery.next()) {
            String[] requestArray = new String[5];
            requestArray[0] = requestQuery.getString("storeName");
            requestArray[1] = requestQuery.getString("calendarName");
            requestArray[2] = requestQuery.getString("startTime");
            requestArray[3] = requestQuery.getString("endTime");
            requestArray[4] = requestQuery.getString("booking");
            requests.add(requestArray);
        }
        String firstOutput = arraylistToString(requests);
        writer.write(firstOutput);
        writer.println();
        writer.flush();

        String input = reader.readLine();
        String[] inputList = input.split(",");
        String inputStore = inputList[0];
        String inputCalendar = inputList[1];
        String inputStartTime = inputList[2];
        String deleteStatement = String.format("DELETE FROM appointments WHERE (storeName == '%s' AND calendarName ==" +
            " '%s' AND startTime == '%s'", inputStore, inputCalendar, inputStartTime);
        int count = statement.executeUpdate(deleteStatement);
        writer.write(String.valueOf(count));
        writer.println();
        writer.flush();
    }

    public String viewApproved(Statement statement) throws SQLException {
        ArrayList<String[]> approved = new ArrayList<>();
        String approvedQueryStatement = String.format("SELECT storeName, calendarName, startTime, endTime, booking, " +
            "datetime(timeStamp, 'unixepoch') as timeStamp FROM appointments WHERE (customerEmail == '%s' AND " +
            "isApproved == 1", clientEmail);
        ResultSet approvedQuery = statement.executeQuery(approvedQueryStatement);
        while (approvedQuery.next()) {
            String[] approvedAppointment = new String[6];
            approvedAppointment[0] = approvedQuery.getString("storeName");
            approvedAppointment[1] = approvedQuery.getString("calendarName");
            approvedAppointment[2] = approvedQuery.getString("startTime");
            approvedAppointment[3] = approvedQuery.getString("endTime");
            approvedAppointment[4] = approvedQuery.getString("booking");
            approvedAppointment[5] = approvedQuery.getString("timeStamp");
            approved.add(approvedAppointment);
        }
        return arraylistToString(approved);
    }

    public void showStatisticsCustomer(Statement statement, PrintWriter writer) throws SQLException{
        ArrayList<String[]> stores = new ArrayList<>();
        ResultSet storeQuery = statement.executeQuery("SELECT * FROM stores");
        while (storeQuery.next()) {
            String[] storeArray = new String[2];
            storeArray[0] = storeQuery.getString("sellerEmail");
            storeArray[1] = storeQuery.getString("storeName");
            stores.add(storeArray);
        }
        writer.write(arraylistToString(stores));
        writer.println();
        writer.flush();

        ArrayList<String[]> stats = new ArrayList<>();
        ResultSet statsQuery = statement.executeQuery("SELECT storeName, startTime, endTime, SUM(currentBookings) " +
            "AS totalCustomers, MAX(currentBookings) AS windowCustomers FROM windows GROUP BY storeName");
        while (statsQuery.next()) {
            String[] statsArray = new String[5];
            statsArray[0] = statsQuery.getString("storeName");
            statsArray[1] = statsQuery.getString("startTime");
            statsArray[2] = statsQuery.getString("endTime");
            statsArray[3] = statsQuery.getString("totalCustomers");
            statsArray[4] = statsQuery.getString("windowCustomers");
            stats.add(statsArray);
        }
        writer.write(arraylistToString(stats));
        writer.println();
        writer.flush();
    }

    public void showStatisticsCustomerOrderByTotal(Statement statement, PrintWriter writer) throws SQLException {
        ArrayList<String[]> stores = new ArrayList<>();
        ResultSet storeQuery = statement.executeQuery("SELECT * FROM stores");
        while (storeQuery.next()) {
            String[] storeArray = new String[2];
            storeArray[0] = storeQuery.getString("sellerEmail");
            storeArray[1] = storeQuery.getString("storeName");
            stores.add(storeArray);
        }
        writer.write(arraylistToString(stores));
        writer.println();
        writer.flush();

        ArrayList<String[]> stats = new ArrayList<>();
        ResultSet statsQuery = statement.executeQuery("SELECT storeName, startTime, endTime, SUM(currentBookings) " +
            "AS totalCustomers, MAX(currentBookings) AS windowCustomers FROM windows GROUP BY storeName ORDER BY totalCustomers DESC");
        while (statsQuery.next()) {
            String[] statsArray = new String[5];
            statsArray[0] = statsQuery.getString("storeName");
            statsArray[1] = statsQuery.getString("startTime");
            statsArray[2] = statsQuery.getString("endTime");
            statsArray[3] = statsQuery.getString("totalCustomers");
            statsArray[4] = statsQuery.getString("windowCustomers");
            stats.add(statsArray);
        }
        writer.write(arraylistToString(stats));
        writer.println();
        writer.flush();
    }

    public String showApproved(Statement statement) throws SQLException {
        ArrayList<String[]> approved = new ArrayList<>();
        String approvedQueryStatement = String.format("SELECT customerEmail, storeName, calendarName, startTime, " +
            "endTime, booking, datetime(timeStamp, 'unixepoch') as timeStamp FROM appointments WHERE (sellerEmail == " +
            "'%s' AND isApproved == 1", clientEmail);
        ResultSet approvedQuery = statement.executeQuery(approvedQueryStatement);
        while (approvedQuery.next()) {
            String[] approvedArray = new String[7];
            approvedArray[0] = approvedQuery.getString("customerEmail");
            approvedArray[1] = approvedQuery.getString("storeName");
            approvedArray[2] = approvedQuery.getString("calendarName");
            approvedArray[3] = approvedQuery.getString("startTime");
            approvedArray[4] = approvedQuery.getString("endTime");
            approvedArray[5] = approvedQuery.getString("booking");
            approvedArray[6] = approvedQuery.getString("timeStamp");
            approved.add(approvedArray);
        }
        return arraylistToString(approved);
    }
}
