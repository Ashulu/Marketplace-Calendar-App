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
                        String approvedNumber = approveRequest(reader, statement, writer);
                        writer.write(approvedNumber);
                        writer.println();
                        writer.flush();
                        break;
                    case "createStore":
                        int updated = createStore(reader, statement);
                        writer.write(String.valueOf(updated));
                        writer.println();
                        writer.flush();
                        break;
                    case "createCalendar":
                        int createdCalendar = createCalendar(reader, statement);
                        writer.write(String.valueOf(createdCalendar));
                        writer.println();
                        writer.flush();
                        break;
                    case "editCalendarName":
                        writer.write(showCalendars(statement));
                        writer.println();
                        writer.flush();
                        int editName = editCalendarName(reader, statement);
                        writer.write(String.valueOf(editName));
                        writer.println();
                        writer.flush();
                        break;
                    case "editCalendarDescription":
                        writer.write(showCalendars(statement));
                        writer.println();
                        writer.flush();
                        int editDescription = editCalendarDescription(reader, statement);
                        writer.write(String.valueOf(editDescription));
                        writer.println();
                        writer.flush();
                        break;
                    case "editCalendarAddWindow":
                        writer.write(showCalendars(statement));
                        writer.println();
                        writer.flush();

                        writer.write(String.valueOf(editCalendarAddWindow(reader, statement)));
                        writer.println();
                        writer.flush();
                        break;
                    case "edit CalendarRemoveWindow":
                        writer.write(showCalendars(statement));
                        writer.println();
                        writer.flush();
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
            String query = String.format("SELECT calendarName, appointmentTitle, startTime, endTime, maxAttendees, " +
                "currentBookings WHERE calendarName == '%s'", calendarNames.get(i)[0]);
            ResultSet windowResult = statement.executeQuery(query);
            while (windowResult.next()) {
                String[] windowArray = new String[6];
                windowArray[0] = windowResult.getString("calendarName");
                windowArray[1] = windowResult.getString("appointmentTitle");
                windowArray[2] = windowResult.getString("startTime");
                windowArray[3] = windowResult.getString("endTime");
                windowArray[4] = windowResult.getString("maxAttendees");
                windowArray[5] = windowResult.getString("currentBookings");
                windowArrayList.add(windowArray);
            }
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

    public String approveRequest(BufferedReader reader, Statement statement, PrintWriter writer) throws SQLException,
        IOException {
        ArrayList<String[]> requests = new ArrayList<>();
        String requestsQueryStatement = String.format("SELECT customerEmail, storeName, calendarName, startTime, " +
            "endTime, booking FROM appointments WHERE (sellerEmail == '%s' AND isApproved == 0)", clientEmail);
        ResultSet requestsQuery = statement.executeQuery(requestsQueryStatement);
        while (requestsQuery.next()) {
            String[] requestArray = new String[6];
            requestArray[0] = requestsQuery.getString("customerEmail");
            requestArray[1] = requestsQuery.getString("storeName");
            requestArray[2] = requestsQuery.getString("calendarName");
            requestArray[3] = requestsQuery.getString("startTime");
            requestArray[4] = requestsQuery.getString("endTime");
            requestArray[5] = requestsQuery.getString("booking");
            requests.add(requestArray);
        }
        writer.write(arraylistToString(requests));
        writer.println();
        writer.flush();

        String input = reader.readLine();
        String[] inputList = input.split(",");
        String inputCustomer = inputList[0];
        String inputStore = inputList[1];
        String inputCalendar = inputList[2];
        String inputStart = inputList[3];
        String inputBooking = inputList[4];
        String updateStatement = String.format("UPDATE appointments SET isApproved = 1, isRequest = 0, timeStamp = " +
            "strftime('s', now) WHERE (customerEmail = '%s' AND storeName = '%s' AND calendarName = '%s' AND " +
            "startTime = '%s' and booking = '%s'", inputCustomer, inputStore, inputCalendar, inputStart,
            inputBooking);
        int changes = statement.executeUpdate(updateStatement);
        return String.valueOf(changes);
    }

    public int createStore(BufferedReader reader, Statement statement) throws IOException, SQLException {
        String input = reader.readLine();
        String existQueryStatement = String.format("SELECT COUNT(storeName) AS count FROM stores WHERE storeName == " +
            "'%s'", input);
        ResultSet existQuery = statement.executeQuery(existQueryStatement);
        existQuery.next();
        int existing = existQuery.getInt("count");
        if (existing < 1) {
            String updateQueryStatement = String.format("INSERT INTO stores (sellerEmail, storeName) VALUES ('%s', " +
                "'%s'", clientEmail, input);
            return statement.executeUpdate(updateQueryStatement);
        } else {
            return 0;
        }
    }

    public int createCalendar(BufferedReader reader, Statement statement) throws IOException, SQLException {
        String input = reader.readLine();
        String[] inputList = input.split(",");
        String inputStore = inputList[0];
        String inputCalendar = inputList[1];
        String inputDescription = inputList[2];

        String calendarUpdateStatement = String.format("INSERT INTO calendars (storeName, calendarName, " +
            "calendarDescription) VALUES ('%s','%s','%s')", inputStore, inputCalendar, inputDescription);
        return statement.executeUpdate(calendarUpdateStatement);
    }

    public int editCalendarName(BufferedReader reader, Statement statement) throws IOException, SQLException {
        String input = reader.readLine();
        String[] inputList = input.split(",");
        String inputStore = inputList[0];
        String inputOld = inputList[1];
        String inputNew = inputList[2];

        String updateStatement = String.format("UPDATE calendars SET calendarName = '%s' WHERE (storeName == '%s' AND" +
            "calendarName == '%s'", inputNew, inputStore, inputOld);
        return statement.executeUpdate(updateStatement);
    }

    public int editCalendarDescription(BufferedReader reader, Statement statement) throws IOException, SQLException {
        String input = reader.readLine();
        String[] inputList = input.split(",");
        String inputStore = inputList[0];
        String inputCalendar = inputList[1];
        String inputDescription = inputList[2];

        String updateStatement = String.format("UPDATE calendars SET description = '%s' WHERE (storeName == '%s' AND " +
            "calendarName == '%s'", inputDescription, inputStore, inputCalendar);
        return statement.executeUpdate(updateStatement);
    }

    public int editCalendarAddWindow(BufferedReader reader, Statement statement) throws IOException, SQLException {
        String input = reader.readLine();
        String[] inputList = input.split(",");
        String inputStore = inputList[0];
        String inputCalendar = inputList[1];
        String inputTitle = inputList[2];
        String inputStart = inputList[3];
        String inputEnd = inputList[4];
        String inputMax = inputList[5];

        ArrayList<String[]> windowTimes = new ArrayList<>();
        ResultSet windowQuery = statement.executeQuery("SELECT startTime, endTime FROM windows ORDER BY startTime");
        while (windowQuery.next()) {
            String[] windowArray = new String[2];
            windowArray[0] = windowQuery.getString("startTime");
            windowArray[1] = windowQuery.getString("endTime");
            windowTimes.add(windowArray);
        }
        boolean overlap = false;
        for (int i = 0; i < windowTimes.size(); i++) {
            if ((inputStart.compareTo(windowTimes.get(i)[0]) >= 0) && (inputStart.compareTo(windowTimes.get(i)[1]) < 0))
            {
                overlap = true;
            }
        }

        if (!overlap) {
            String addWindowStatement = String.format("INSERT INTO windows (storeName, calendarName, " +
                "appointmentTitle, startTime, endTime, maxAttendees, currentBookings) VALUES ('%s', '%s', '%s', '%s'," +
                " '%s', '%s', '%s'", inputStore, inputCalendar, inputTitle, inputStart, inputEnd, inputMax, 0);
            return statement.executeUpdate(addWindowStatement);
        } else {
            return -1;
        }

    }

    public String showCalendars(Statement statement) throws SQLException {
        ArrayList<String[]> calendars = new ArrayList<>();
        ResultSet calendarsQuery = statement.executeQuery("SELECT * FROM calendars");
        while (calendarsQuery.next()) {
            String[] calendarArray = new String[3];
            calendarArray[0] = calendarsQuery.getString("storeName");
            calendarArray[1] = calendarsQuery.getString("calendarName");
            calendarArray[2] = calendarsQuery.getString("calendarDescription");
            calendars.add(calendarArray);
        }
        return arraylistToString(calendars);
    }
}
