import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TestClient {
    public static void main (String[] args) throws UnknownHostException, IOException, ClassNotFoundException {

        System.out.println("Current IP set to: localhost");
        System.out.println("Current server port number set to: 5555");

        String ip = "localhost";
        String port = "5555";


        Socket socket = null;
        try {
            socket = new Socket(ip, Integer.parseInt(port));
            System.out.println("Connected to Server");
        } catch (Exception e) {
            System.out.println("Connection to Server failed");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());

        //TODO:
        //        1. create seller
        System.out.println("Testing seller account creation:");
        writer.println("createAccount");
        writer.flush();
        writer.println("seller,seller@test.com,testpassword");
        writer.flush();
        String sellerCreate = reader.readLine();
        if (sellerCreate.equals("1")) {
            System.out.println("Creation successful");
        } else {
            System.out.println("Creation failed");
        }

        //        2. login seller
        System.out.println("Testing log in as seller:");
        writer.println("login");
        writer.flush();
        writer.println("seller@test.com,testpassword");
        writer.flush();
        String sellerLogin = reader.readLine();
        if (sellerLogin.equals("2")) {
            System.out.println("Log in successful");
        } else  {
            System.out.println("Log in failed");
        }

        //        3. create store * 2
        System.out.println("Testing store creation:");
        writer.println("createStore");
        writer.flush();
        writer.println("storeOne");
        writer.flush();
        String storeOneCreation = reader.readLine();
        if (storeOneCreation.equals("1")) {
            System.out.println("StoreOne created");
        } else {
            System.out.println("StoreOne creation failed");
        }
        writer.println("createStore");
        writer.flush();
        writer.println("storeTwo");
        writer.flush();
        String storeTwoCreation = reader.readLine();
        if (storeTwoCreation.equals("1")) {
            System.out.println("StoreTwo created");
        } else {
            System.out.println("StoreTwo creation failed");
        }

        //        4. create calendar * 2 for store1;
        System.out.println("Testing calendar creation:");
        writer.println("createCalendar");
        writer.flush();
        writer.println("storeOne,calendarOne,testing purposes");
        writer.flush();
        String calendarOneCreation = reader.readLine();
        if (calendarOneCreation.equals("1")) {
            System.out.println("CalendarOne created");
        } else {
            System.out.println("CalendarOne creation failed");
        }
        writer.println("createCalendar");
        writer.flush();
        writer.println("storeOne,calendarTwo,duplicate test");
        writer.flush();
        String calendarTwoCreation = reader.readLine();
        if (calendarTwoCreation.equals("1")) {
            System.out.println("CalendarTwo created");
        } else {
            System.out.println("CalendarTwo creation failed");
        }

        //        5. edit calendar1 - name
        System.out.println("Testing calendar name change:");
        writer.println("editCalendar");
        writer.flush();
        writer.println("editCalendarName");
        writer.flush();
        writer.println("storeOne,calendarOne,newCalendarOne");
        writer.flush();
        reader.readLine();
        String calendarOneNameChange = reader.readLine();
        if (calendarOneNameChange.equals("1")) {
            System.out.println("CalendarOne name changed");
        } else {
            System.out.println("CalendarOne name change failed");
        }

        //        6. edit calendar2 - description
        System.out.println("Testing calendar description change:");
        writer.println("editCalendar");
        writer.flush();
        writer.println("editCalendarDescription");
        writer.flush();
        writer.println("storeOne,calendarTwo,Check for edit");
        writer.flush();
        reader.readLine();
        String descriptionEdit = reader.readLine();
        if (descriptionEdit.equals("1")) {
            System.out.println("Description of calendarTwo changed");
        } else {
            System.out.println("Description change failed");
        }

        //        7. create window * 2 for calendar1
        System.out.println("Testing adding windows for newCalendarOne");
        writer.println("editCalendar");
        writer.flush();
        writer.println("editCalendarAddWindow");
        writer.flush();
        writer.println("storeOne,newCalendarOne,windowOne,1100,1200,10");
        writer.flush();
        reader.readLine();
        String addingWindowOne = reader.readLine();
        if (addingWindowOne.equals("1")) {
            System.out.println("WindowOne addition successful");
        } else {
            System.out.println("WindowOne addition failed");
        }
        writer.println("editCalendar");
        writer.flush();
        writer.println("editCalendarAddWindow");
        writer.flush();
        writer.println("storeOne,newCalendarOne,windowTwo,1300,1400,20");
        writer.flush();
        reader.readLine();
        String addingWindowTwo = reader.readLine();
        if (addingWindowTwo.equals("1")) {
            System.out.println("WindowTwo addition successful");
        } else {
            System.out.println("WindowTwo addition failed");
        }

        //        8. remove window1 for calendar1
        System.out.println("Testing deleting window:");
        writer.println("editCalendar");
        writer.flush();
        writer.println("editCalendarRemoveWindow");
        writer.flush();
        writer.println("storeOne,newCalendarOne");
        writer.flush();
        reader.readLine();
        writer.println("1400");
        writer.flush();
        reader.readLine();
        String deletingWindow = reader.readLine();
        if (deletingWindow.equals("1")) {
            System.out.println("Removing window successful");
        } else {
            System.out.println("Removing window failed");
        }

        //        8.5 remove calendar2;
        System.out.println("Testing removing calendar:");
        writer.println("deleteCalendar");
        writer.flush();
        writer.println("storeOne,calendarTwo");
        writer.flush();
        reader.readLine();
        String removeCalendar = reader.readLine();
        if (removeCalendar.equals("1")) {
            System.out.println("Removing calendar successful");
        } else {
            System.out.println("Removing calendar failed");
        }

        //        9. import calendar for store2;
        System.out.println("Testing importing calendar");
        writer.println("importCalendar");
        writer.flush();
        reader.readLine();
        writer.println("storeTwo,import.csv");
        writer.flush();
        String importResult = reader.readLine();
        if (importResult.equals("1")) {
            System.out.println("Import successful");
        } else {
            System.out.println("Import failed");
        }
        
        //        10. create client1
        System.out.println("Testing customer account creation:");
        writer.println("createAccount");
        writer.flush();
        writer.println("customer,customer@test.com,testpassword");
        writer.flush();
        String customerCreate = reader.readLine();
        if (customerCreate.equals("1")) {
            System.out.println("Creation successful");
        } else {
            System.out.println("Creation failed");
        }

        //        11. login client1
        System.out.println("Testing log in as customer:");
        writer.println("login");
        writer.flush();
        writer.println("customer@test.com,testpassword");
        writer.flush();
        String customerLogin = reader.readLine();
        if (customerLogin.equals("1")) {
            System.out.println("Log in successful");
        } else  {
            System.out.println("Log in failed");
        }

        //        12. viewCalendar
        System.out.println("Testing viewing calendars:");
        writer.println("viewCalendar");
        writer.flush();
        String calendarList = reader.readLine();
        System.out.println("calendars: " + calendarList);
        String windowList = reader.readLine();
        System.out.println("windows: " + windowList);

        //        13. requestappointment1 for store1, calendar1, window 1
        System.out.println("Testing appointment requestOne:");
        writer.println("requestAppointment");
        writer.flush();
        reader.readLine();
        writer.println("storeOne,newCalendarOne");
        writer.flush();
        reader.readLine();
        writer.println("1100,1200,2");
        writer.flush();
        String requestResult = reader.readLine();
        if (requestResult.equals("1")) {
            System.out.println("Request successful");
        } else {
            System.out.println("Request failed");
        }
        writer.println("break");
        writer.flush();
        //        14. requestappointment2 for store1, calendar1, window 2

        System.out.println("Testing appointment requestTwo:");
        writer.println("requestAppointment");
        writer.flush();
        reader.readLine();
        writer.println("storeOne,newCalendarOne");
        writer.flush();
        reader.readLine();
        writer.println("1100,1200,20");
        writer.flush();
        String requestTwoResult = reader.readLine();
        if (requestTwoResult.equals("1")) {
            System.out.println("Request successful");
        } else {
            System.out.println("Request failed");
        }
        writer.println("break");
        writer.flush();

        //        15. requestappointment3 for calendar, store2
        System.out.println("Testing appointment requestThree:");
        writer.println("requestAppointment");
        writer.flush();
        reader.readLine();
        writer.println("storeTwo,calendarTitle");
        writer.flush();
        reader.readLine();
        writer.println("1400,1500,2");
        writer.flush();
        String requestThreeResult = reader.readLine();
        if (requestThreeResult.equals("1")) {
            System.out.println("Request successful");
        } else {
            System.out.println("Request failed");
        }
        writer.println("break");
        writer.flush();

        //        16. cancel request2
        System.out.println("Testing cancel request:");
        writer.println("cancelRequest");
        writer.flush();
        reader.readLine();
        writer.println("storeTwo,calendarTitle,1400");
        writer.flush();
        String cancelRequest = reader.readLine();
        if (cancelRequest.equals("1")) {
            System.out.println("Cancel successful");
        } else {
            System.out.println("Cancel failed");
        }
        writer.println("break");
        writer.flush();

        //        17. login seller
        System.out.println("Testing log in as seller:");
        writer.println("login");
        writer.flush();
        writer.println("seller@test.com,testpassword");
        writer.flush();
        String sellerLoginTwo = reader.readLine();
        if (sellerLoginTwo.equals("2")) {
            System.out.println("Log in successful");
        } else  {
            System.out.println("Log in failed");
        }


        //        19. approve everything
        System.out.println("Testing approve");
        writer.println("approveRequest");
        writer.flush();
        String requestList = reader.readLine();
        System.out.println("requestList: " + requestList);
        writer.println("confirm,customer@test.com,storeOne,newCalendarOne,1100,2");
        writer.flush();
        String approveResult= reader.readLine();
        if (approveResult.equals("1")) {
            System.out.println("Approve successful");
        } else {
            System.out.println("Approve failed");
        }

        //        20. viewApproved
        System.out.println("Testing showApproved");
        writer.println("showApproved");
        writer.flush();
        String approvedList = reader.readLine();
        System.out.println("approvedList: " + approvedList);

        //        25. show statistics
        System.out.println("Testing statistics");
        writer.println("showStatisticsCustomer");
        writer.flush();
        String stores = reader.readLine();
        System.out.println("stores: " + stores);
        String statistics = reader.readLine();
        System.out.println("statistics: " + statistics);

        // login customer
        System.out.println("Testing log in as customer:");
        writer.println("login");
        writer.flush();
        writer.println("customer@test.com,testpassword");
        writer.flush();
        String customerLoginTwo = reader.readLine();
        if (customerLoginTwo.equals("1")) {
            System.out.println("Log in successful");
        } else  {
            System.out.println("Log in failed");
        }

        //        28. view approved
        System.out.println("Testing viewing Approved");
        writer.println("viewApproved");
        writer.flush();
        String approved = reader.readLine();
        System.out.println("List of approved: " + approved);

        //        29. export
        System.out.println("Testing export");
        writer.println("exportApprovedRequests");
        writer.flush();
        writer.println("export.csv");
        writer.flush();
        String exportResult = reader.readLine();
        if (exportResult.equals("1")) {
            System.out.println("Export successful");
        } else {
            System.out.println("Export failed");
        }

        //        30. quit
        System.out.println("Testing quit");
        System.out.println("If the server does not print any error messages, then it closed correctly!");
        writer.println("quit");
        writer.flush();


        writer.close();
        reader.close();
    }
}
