import java.io.*;
import java.net.*;
import java.util.Scanner;

public class TestClient {
    public static void main (String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        Scanner scan = new Scanner(System.in);

        System.out.println("Enter server IP");
        String ip = scan.nextLine();
        System.out.println("Enter server port number");
        String port =scan.nextLine();

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
        String addingWindowTwo = reader.readLine();
        if (addingWindowTwo.equals("1")) {
            System.out.println("WindowTwo addition successful");
        } else {
            System.out.println("WindowTwo addition failed");
        }

        //        8. remove window1 for calendar1
        System.out.println("Testing deleting calendar");
        writer.println("editCalendar");
        writer.flush();
        writer.println("editCalendarRemoveWindow");
        writer.flush();
        writer.println("storeOne,newCalendarOne");
        writer.flush();
        reader.readLine();
        writer.println("1400");
        String deletingWindow = reader.readLine();
        if (deletingWindow.equals("1")) {
            System.out.println("Removing window Successful");
        } else {
            System.out.println("Removing window failed");
        }

        //        8.5 remove calendar2;
        System.out.println("Testing removing calendar");
        writer.println("deleteCalendar");
        writer.flush();
        writer.println("storeOne,calendarTwo");
        String removeCalendar = reader.readLine();
        if (removeCalendar.equals("1")) {
            System.out.println("Removing calendar successful");
        } else {
            System.out.println("Removing calendar failed");
        }

        //        9. import calendar for store2;
        //        10. create client1
        //        11. login client1
        //        12. viewCalendar
        //        13. requestappointment1 for store1, calendar1, window 1
        //        14. requestappointment2 for store1, calendar1, window 2
        //        15. requestappointment3 for calendar, store2
        //        16. cancel request2
        //        17. create client2
        //        18. login client2
        //        19. requestappointment1 for store1, calendar1, window 1
        //        20. requestappointment2 for store1, calendar1, window 2
        //        21. requestappointment3 for store2, calendar, window 1
        //        22. login seller
        //        23. approverequest - expected 5 requests
        //        24. approve everything
        //        25. show statistics
        //        26. login client2
        //        27. show statistics
        //        28. view approved
        //        29. export
        //        30. quit


        writer.close();
        reader.close();
    }
}
