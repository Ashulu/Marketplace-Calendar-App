import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;

public class Client extends JComponent implements Runnable {

    JPanel loginPanel;
    JPanel createPanel;
    JPanel sellerMain;
    JPanel sellerSub;
    JPanel customerMain;
    JPanel customerSub;
    JButton login;
    JButton createAccount;
    JPanel jPanel;
    JFrame frame;
    static BufferedReader reader;
    static PrintWriter writer;


    public void creationPanel() {
        JLabel createLabel = new JLabel("Account creation");
        createPanel.add(createLabel);

        JTextField userField = new JTextField("Email", 30);
        createPanel.add(userField);

        JTextField passField = new JTextField("Password", 30);
        createPanel.add(passField);

        String[] choices = {"Customer", "Seller"};

        JComboBox<String> types = new JComboBox<>(choices);
        createPanel.add(types);
        String type = (String)types.getSelectedItem();

        JButton create = new JButton("Create Account");
        createPanel.add(create);

        JButton back = new JButton("Back");
        createPanel.add(back);



        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //send the user and pass to the server to add to the database
                boolean exists = false;
                if (exists) {
                    //server checks if the account already exists and writes true or false back to the client. Exists is then updated to that value

                    JOptionPane.showMessageDialog(frame.getContentPane(), "Account already Exists!");
                } else if (userField.getText().equals("Email") || passField.getText().equals("Password") ){
                    JOptionPane.showMessageDialog(frame.getContentPane(), "Enter an email and/or password");
                } else {
                    //goes back to the main page to then login with the newly created account
                    frame.remove(createPanel);
                    frame.add(loginPanel, BorderLayout.CENTER);
                    frame.pack();
                }
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(createPanel);
                frame.add(loginPanel, BorderLayout.CENTER);
                frame.pack();
            }
        });

    }

    public void run(){
        frame = new JFrame("Calendar");

        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginPage();

        frame.setVisible(true);
    }

    public void loginPage() {

        loginPanel = new JPanel();
        loginPanel.setBounds(0, 0, 30, frame.getHeight());
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        JLabel loginLabel = new JLabel("Login:");
        loginPanel.add(loginLabel);

        JTextField userField = new JTextField("Email", 30);
        loginPanel.add(userField);

        JTextField passField = new JTextField("Password", 30);
        loginPanel.add(passField);

        login = new JButton("Login");
        loginPanel.add(login);

        createAccount = new JButton("Create account");
        loginPanel.add(createAccount);

        frame.add(loginPanel, BorderLayout.CENTER);


        createAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createPanel = new JPanel();
                createPanel.setBounds(0, 0, 30, frame.getHeight());
                createPanel.setLayout(new BoxLayout(createPanel, BoxLayout.Y_AXIS));
                creationPanel();
                frame.remove(loginPanel);
                frame.add(createPanel, BorderLayout.CENTER);
                frame.pack();
            }
        });

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userField.getText().isEmpty() || passField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame.getContentPane(), "Invalid inputs");
                } else {
                    try {
                        writer.println("login");
                        writer.flush();

                        writer.println(userField.getText() + "," + passField.getText());
                        writer.flush();
                        int check = Integer.parseInt(reader.readLine());
                        switch (check){
                            case 1 -> {
                                //go to the customer panel
                                customerMain = new JPanel();
                                customerMain.setBounds(0, 0, 30, frame.getHeight());
                                customerMain.setLayout(new BoxLayout(customerMain, BoxLayout.Y_AXIS));
                                customer(reader, writer);
                                frame.remove(loginPanel);
                                frame.add(customerMain);
                                frame.pack();
                            }
                            case 2 -> {
                                //go to the seller panel
                                sellerMain = new JPanel();
                                sellerMain.setBounds(0, 0, 30, frame.getHeight());
                                sellerMain.setLayout(new BoxLayout(sellerMain, BoxLayout.Y_AXIS));
                                seller(reader, writer);
                                frame.remove(loginPanel);
                                frame.add(sellerMain, BorderLayout.CENTER);
                                frame.pack();
                            }
                            case 0 -> JOptionPane.showMessageDialog(frame.getContentPane(), "Password is wrong!");
                            case -1 -> JOptionPane.showMessageDialog(frame.getContentPane(), "Account not made yet!");
                        }
                    } catch (IOException a) {
                        System.out.println("This is the reason");
                        a.printStackTrace();
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        String host = JOptionPane.showInputDialog(null, "Enter the IP you want to connect to:", "Calendar System",
                JOptionPane.QUESTION_MESSAGE);
        try {
            Socket socket = new Socket(host,5555);
            JOptionPane.showMessageDialog(null, "Client connected");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());

            SwingUtilities.invokeLater(new Client());
        } catch (Exception e) {
            e.printStackTrace();
        }


        //SwingUtilities.invokeLater(new Client());

    }

    // ------------------------
    // START OF CUSTOMER GUI
    // ------------------------

    private void customer(BufferedReader br, PrintWriter pw) throws IOException {
        JLabel welcome = new JLabel("Welcome Customer! \nPlease Select what you would like to do:");
        customerMain.add(welcome);

        String[] customerOp = new String[]{
                // TODO: Add view calendars option
                "View Calendars",
                "Request an appointment",
                "Cancel an appointment",
                "View your approved appointments",
                "Show store statistics",
                "Logout",
                "Quit"
        };

        JComboBox<String> customerOptions = new JComboBox<>(customerOp);
        customerMain.add(customerOptions);

        JButton choice = new JButton("Select");
        customerMain.add(choice);

        choice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    customerSub = new JPanel();
                    customerSub.setBounds(0, 0, 30, frame.getHeight());
                    customerSub.setLayout(new BoxLayout(customerSub, BoxLayout.Y_AXIS));
                    switch (customerOptions.getSelectedIndex()){
                        case 0 -> {
                            c0(br, pw);
                        }
                        case 1 -> {
                            c1(br, pw);
                        }
                        case 2 -> {
                            c2(br, pw);
                        }
                        case 3 -> {
                            c3(br, pw);
                        }
                        case 4 -> {
                            c4(br, pw);
                        }
                        case 5 -> {
                            frame.remove(customerMain);
                            frame.add(loginPanel);
                            frame.pack();
                            return;
                        }
                        case 6 -> {
                            frame.remove(customerMain);
                            exit();
                        }
                    }
                    frame.remove(customerMain);
                    frame.add(customerSub);
                    frame.pack();
                } catch (Exception a) {
                    a.printStackTrace();
                }
            }
        });
    }


    private void c0(BufferedReader br, PrintWriter pw) throws IOException {
        pw.println("viewCalendars");

        pw.flush();
        customerBack(customerSub);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(customerSub);
                customerSub = new JPanel();
                customerSub.setLayout(new BoxLayout(customerSub, BoxLayout.Y_AXIS));
                c0(br,pw);
                frame.add(customerSub, BorderLayout.CENTER);
                frame.pack();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        customerSub.add(refresh);

        //get all store names from server and print out in dropdown to user
        //once user selects, get all calendars from that specific store and print out
        // user should be able to select based on a dropdown

        String windows = br.readLine();

        String calendarNames = br.readLine();
        String[] strCalChoices = calendarNames.substring(1, calendarNames.length()-1).split("],\\[");
        ArrayList<String> calNames = new ArrayList<String>();
        ArrayList<String> calDesc = new ArrayList<String>();
        for (int i = 0; i < strCalChoices.length; i++) {
            calNames.add(strCalChoices[i].split(",")[0]);
            calDesc.add(strCalChoices[i].split(",")[1]);
        }
        String[] calChoices = new String[calNames.size()];
        calChoices = calNames.toArray(calChoices);
        final String[] cals = calChoices;
        String[] calDescs = new String[calNames.size()];
        calDescs = calDesc.toArray(calDescs);
        final String[] calInfo = calDescs;

        JComboBox<String> calendarChoices = new JComboBox<String>(cals);
        customerSub.add(calendarChoices);
        JButton enter = new JButton("Enter");
        JTextField displayWindows = new JTextField(80);
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customerSub.remove(displayWindows);
                String choice = cals[calendarChoices.getSelectedIndex()];
                String[] appointmentWindowsData = windows.substring(1, windows.length()-1).split("],\\[");
                String result = "";
                for (int i = 0; i < appointmentWindowsData.length; i++) {
                    String[] data = appointmentWindowsData[i].split(",");
                    if (data[0].equals(choice)) {
                        String windowData = String.format("Appointment Title: %s \nTime: %s - %s \nMaximum Attendees: %s \nCurrent Bookings: %s \n",
                                data[1], data[2], data[3], data[4], data[5]);
                        result += windowData;
                    }
                }
                if (result.isEmpty()) {
                    displayWindows.setText("Calendar Description: " + calInfo[calendarChoices.getSelectedIndex()] +
                            "\n\nNo appointments were found!");
                } else {
                    displayWindows.setText("Calendar Description: " + calInfo[calendarChoices.getSelectedIndex()] +
                            "\n\n" + result);
                }
                customerSub.add(displayWindows);
            }
        });
        customerSub.add(enter);
    }

    private void c1(BufferedReader br, PrintWriter pw) throws IOException {
        pw.println("requestAppointment");
        pw.flush();
        customerBack(customerSub);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {

                frame.remove(customerSub);
                customerSub = new JPanel();
                customerSub.setLayout(new BoxLayout(customerSub, BoxLayout.Y_AXIS));
                c1(br,pw);
                frame.add(customerSub, BorderLayout.CENTER);
                frame.pack();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        jPanel.add(refresh);

        //creation of appointment

        String storeData = br.readLine();
        String[] calendars = storeData.substring(1, storeData.length()-1).split("],\\[");
        ArrayList<String> stores = new ArrayList<String>();
        ArrayList<String[]> calNames = new ArrayList<String[]>();
        for (int i = 0; i < calendars.length; i++) {
            String[] calInfo = calendars[i].split(",");
            stores.add(calInfo[0]);
        }
        for (int j = 0; j < stores.size(); j++) {
            ArrayList<String> storeCalendars = new ArrayList<String>();
            for (int k = 0; k < calendars.length; k++) {
                String[] calInfo = calendars[k].split(",");
                if (calendars[k].equals(stores.get(j))) {
                    storeCalendars.add(calInfo[1]);
                }
            }
            String[] storeCals = new String[storeCalendars.size()];
            storeCals = storeCalendars.toArray(storeCals);
            calNames.add(storeCals);
        }
        String[] storeChoices = new String[stores.size()];
        storeChoices = stores.toArray(storeChoices);
        final String[] allStores = storeChoices;
        final String[][] allCalendars = calNames.toArray(new String[calNames.size()][]);


        JComboBox<String> storeName = new JComboBox<String>(allStores);
        jPanel.add(storeName);
        JComboBox<String> calendarName = new JComboBox<String>(allCalendars[storeName.getSelectedIndex()]);
        jPanel.add(calendarName);

        JButton enter = new JButton("Enter");
        enter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = String.format("%s,%s",storeName.getSelectedItem(), calendarName.getSelectedItem());
                pw.write(command);
                pw.flush();
            }
        });
        jPanel.add(enter);

        String windows = br.readLine();
        String[] appointmentWindows = windows.substring(1, windows.length()-1).split("],\\[");
        String[] appointments = new String[appointmentWindows.length];
        String[] maxAttendees = new String[appointmentWindows.length];
        String[] currentBookings = new String[appointmentWindows.length];
        for (int i = 0; i < appointmentWindows.length; i++) {
            String[] data = appointmentWindows[i].split(",");
            appointments[i] = String.format("%s - %s", data[0], data[1]);
            maxAttendees[i] = data[2];
            currentBookings[i] = data[3];
        }

        JComboBox<String> chooseWindow = new JComboBox<String>(appointments);
        jPanel.add(chooseWindow);
        JTextField bookings = new JTextField("Enter number of bookings", 10);
        jPanel.add(bookings);
        JButton create = new JButton("Create");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String command = String.format("%s,%s,%s,%s",
                        String.valueOf(chooseWindow.getSelectedItem()).split(" - ")[0],
                        String.valueOf(calendarName.getSelectedItem()).split(" - ")[1],
                        maxAttendees[chooseWindow.getSelectedIndex()],
                        Integer.parseInt(currentBookings[chooseWindow.getSelectedIndex()]) +
                                Integer.parseInt(bookings.getText()));
                pw.write(command);
                pw.flush();
            }
        });

        JTextField result = new JTextField(10);
        int response = br.read();
        if (response == 0) {
            result.setText("Unable to create appointment.");
        } else {
            result.setText("Creation successful!");
        }

        jPanel.add(result);
        result.setVisible(true);
    }

    private void c2(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("cancelRequest");
        pw.flush();
        jPanel = new JPanel();
        frame.add(jPanel);
        customerBack(jPanel);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(jPanel);
                jPanel = new JPanel();
                jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
                c1(br,pw);
                frame.add(jPanel, BorderLayout.CENTER);
                frame.pack();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        jPanel.add(refresh);

        String clientRequests = br.readLine();
        String[] requests = clientRequests.substring(1, clientRequests.length()-1).split("],\\[");
        String result = "";
        for (int i = 0; i < requests.length; i++) {
            String[] data = requests[i].split(",");
            result += (i + 1) + ". \n" + "Store Name: " + data[0] + "\nCalendar Name: " + data[1] +
                    "\nTime Window: " + data[2] + " - " + data[3] + "\nBookings: " + data[4] + "\n\n";
        }

        JTextField allAppointments = new JTextField(result, 50);
        jPanel.add(allAppointments);
        String[] options = new String[requests.length];
        for (int i = 1; i <= requests.length; i++) {
            options[i-1] = String.valueOf(i);
        }
        JComboBox<String> appointment = new JComboBox<String>(options);
        jPanel.add(appointment);
        JButton delete = new JButton("Delete");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] choice = requests[appointment.getSelectedIndex()].split(",");
                String command = String.format("%s,%s,%s", choice[0], choice[1], choice[2]);
                pw.write(command);
                pw.flush();
            }
        });
        jPanel.add(delete);

        JTextField success = new JTextField();
        jPanel.add(success);

        int response = br.read();
        if (response == 0) {
            success.setText("Unable to delete appointment.");
        } else {
            success.setText("Deletion successful!");
        }

        success.setVisible(true);
        frame.pack();

    }

    private void c3(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("viewApproved");
        pw.flush();
        jPanel = new JPanel();
        frame.add(jPanel);
        customerBack(jPanel);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(jPanel);
                jPanel = new JPanel();
                jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
                c1(br,pw);
                frame.add(jPanel, BorderLayout.CENTER);
                frame.pack();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        jPanel.add(refresh);


        // code to view approved appointments
        String data = br.readLine();
        String[] appointments = data.substring(1, data.length()-1).split("],\\[");
        String result = "";
        for (int i = 0; i < appointments.length; i++) {
            String[] appt = appointments[i].split(",");
            result += (i + 1) + ". \n" + "Store Name: " + appt[0] + "\nCalendar Name: " + appt[1] +
                    "\nTime Window: " + appt[2] + " - " + appt[3] + "\nBookings: " + appt[4] + "\n\n";
        }

        JTextField displayAppointments = new JTextField(50);
        displayAppointments.setText(result);
        jPanel.add(displayAppointments);
        displayAppointments.setVisible(true);
        frame.pack();
    }
  
  
    private void c4(BufferedReader br, PrintWriter pw) throws IOException{
        JLabel askSort = new JLabel("Would you like to srt your statistics?");
        customerSub.add(askSort);

        JButton yes = new JButton("Yes");
        customerSub.add(yes);

        JButton no = new JButton("No");
        customerSub.add(no);

        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pw.println("showStatisticsCustomerOrderByTotal");
                pw.flush();

                try {
//                    String storeString = br.readLine();
//                    String stats = br.readLine();
//                    String[] split = stats.split("],\\[");
//                    split[0] = split[0].substring(2);
//                    split[split.length - 1] = split[split.length - 1].substring(2);
//
//                    for (int i = 0; i < split.length; i++) {
//                        StringBuilder line = new StringBuilder();
//                        String[] indWindow = split[i].split(",");
//                        line.append("Store Name: ").append(indWindow[0]).append("\n");
//                        line.append("\t Window: ").append(indWindow[1]).append("-").append(indWindow[2]).append("\n");
//                        line.append("\t Total store customers: ").append(indWindow[3]).append("\n");
//                        line.append("\t This window's customers: ").append(indWindow[4]).append("\n");
//
//                        JLabel window = new JLabel(line.toString());
//                        customerSub.add(window);
//                    }

                    showStats(br);

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pw.println("showStatisticsCustomer");
                pw.flush();

                try {
                    showStats(br);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        JButton refresh = new JButton("Refresh");
        customerSub.add(refresh);

        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    frame.remove(customerSub);
                    customerSub = new JPanel();
                    customerSub.setLayout(new BoxLayout(customerSub, BoxLayout.Y_AXIS));
                    c3(br, pw);
                    frame.add(customerSub);
                    frame.pack();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });


        customerBack(customerSub);

    }  

    //shows the stats for customers, regardless of whether its sorted or not
    public void showStats(BufferedReader br) throws IOException{
        String storeString = br.readLine();
        String stats = br.readLine();
        String[] split = stats.split("],\\[");
        split[0] = split[0].substring(2);
        split[split.length - 1] = split[split.length - 1].substring(2);

        for (int i = 0; i < split.length; i++) {
            StringBuilder line = new StringBuilder();
            String[] indWindow = split[i].split(",");
            line.append("Store Name: ").append(indWindow[0]).append("\n");
            line.append("\t Window: ").append(indWindow[1]).append("-").append(indWindow[2]).append("\n");
            line.append("\t Total store customers: ").append(indWindow[3]).append("\n");
            line.append("\t This window's customers: ").append(indWindow[4]).append("\n");

            JLabel window = new JLabel(line.toString());
            customerSub.add(window);
        }
    }


    // -------------------
    // START OF SELLER GUI
    // -------------------

    //Seller landing page
    private void seller(BufferedReader br, PrintWriter pw) throws IOException {
        JLabel welcome = new JLabel("Hello Seller! \nPlease Select what you would like to do.");
        sellerMain.add(welcome);

        String[] selOp = new String[] {
                "View Approved Appointments", "Appointment Requests",
                "Create Store", "Create Calendar",
                "Edit Calendar", "Delete Calendar",
                "Show Statistics", "Import Calendar",
                "Logout", "Quit"
        };
//        frame.add(jPanel);
        JComboBox<String> sellerOptions = new JComboBox<>(selOp);
        sellerMain.add(sellerOptions);

        JButton choice = new JButton("Select");
        sellerMain.add(choice);

        choice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sellerSub = new JPanel();
                    sellerSub.setBounds(0, 0, 30, frame.getHeight());
                    sellerSub.setLayout(new BoxLayout(sellerSub, BoxLayout.Y_AXIS));
                    switch (sellerOptions.getSelectedIndex()) {
                        case 0 -> {

                            s0(br, pw);

                        }
                        case 1 -> {

                            s1(br,pw);
                        }
                        case 2 -> {

                            s2(br,pw);

                        }
                        case 3 -> {

                            s3(br,pw);

                        }
                        case 4 -> {

                            s4(br,pw);

                        }
                        case 5 -> {

                            s5(br,pw);

                        }
                        case 6 -> {

                            s6(br,pw);

                        }
                        case 7 -> {

                            s7(br,pw);

                        }
                        //return to login
                        case 8 -> {
                            frame.remove(sellerMain);
                            frame.add(loginPanel);
                            frame.pack();
                            return;
                        }
                        //quit
                        case 9 -> {
                            frame.remove(sellerMain);
                            exit();
                            return;
                        }
                    }
                    frame.remove(sellerMain);
                    frame.add(sellerSub, BorderLayout.CENTER);
                    frame.pack();
                } catch(Exception a) {
                    a.printStackTrace();
                }
            }
        });
    }
    //0 "View Approved Appointments"
    private void s0(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("showApproved");
        pw.println();

        pw.flush();
        sellerBack(sellerSub);

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                sellerSub = new JPanel();
                sellerSub.setLayout(new BoxLayout(sellerSub, BoxLayout.Y_AXIS));
                s0(br,pw);
                frame.add(sellerSub, BorderLayout.CENTER);
                frame.pack();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sellerSub.add(refresh);
        JTextArea appointments = new JTextArea("",3, 20);

        //add processing a little later
        String approved = br.readLine();
        String result = "";

        if (approved.isEmpty()) {
            result = "No approved appointments.";
        } else {
            String[] appoints = approved.substring(1, approved.length() - 1).split("],\\[");
            for (int i = 0; i < appoints.length; i++) {
                result += appoints[i] + "\n";
            }
        }
        sellerSub.add(appointments);

        appointments.setText(result);
    }
    //1 "Appointment Requests"
    private void s1(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("approveRequest");
        pw.println();

        pw.flush();

        sellerBack(sellerSub);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                sellerSub = new JPanel();
                sellerSub.setLayout(new BoxLayout(sellerSub, BoxLayout.Y_AXIS));
                s1(br,pw);
                frame.add(sellerSub, BorderLayout.CENTER);
                frame.pack();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sellerSub.add(refresh);
        String temp = br.readLine();
        String[] requests = temp.substring(1,temp.length() - 1).split("],\\[");
        JComboBox<String> appointments = new JComboBox<String>(requests);
        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(e -> {
            String[] temp2 = ((String)appointments.getSelectedItem()).split(", ");
            String command = String.format("confirm,%s,%s,%s,%s,%s",
                    temp2[0],temp2[1],temp2[2],temp2[3],temp2[5]);
            pw.write(command);
            pw.println();
            System.out.println("wrote to confirm");
            System.out.println(command);
            pw.flush();
            int response;
            try {
                response = Integer.parseInt(br.readLine());
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
            if (response == 0) {
                JOptionPane.showMessageDialog(null, "Operation failed.",
                        "Seller Client", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Operation successful!",
                        "Seller Client", JOptionPane.INFORMATION_MESSAGE);
            }
            frame.remove(sellerSub);
            frame.add(sellerMain, BorderLayout.CENTER);
            frame.pack();
        });
        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> {
            String[] temp2 = ((String)appointments.getSelectedItem()).split(", ");
            String command = String.format("delete,%s,%s,%s,%s,%s",
                    temp2[0],temp2[1],temp2[2],temp2[3],temp2[5]);
            pw.write(command);
            pw.println();
            System.out.println("wrote to reject");
            System.out.println(command);
            pw.flush();
            int response;
            try {
                response = Integer.parseInt(br.readLine());
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
            if (response == 0) {
                JOptionPane.showMessageDialog(null, "Operation failed.",
                        "Seller Client", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Operation successful!",
                        "Seller Client", JOptionPane.INFORMATION_MESSAGE);
            }
            frame.remove(sellerSub);
            frame.add(sellerMain, BorderLayout.CENTER);
            frame.pack();
        });
        sellerSub.add(appointments);
        sellerSub.add(confirm);
        sellerSub.add(delete);
    }

    //2 "Create Store"
    private void s2(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("createStore");
        pw.println();
        pw.flush();
        frame.add(sellerSub);
        sellerBack(sellerSub);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                s0(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sellerSub.add(refresh);
        JTextField storeName = new JTextField("Example Name", 10);
        JTextField result = new JTextField();
        result.setVisible(false);
        JButton create = new JButton("Create");
        create.addActionListener(e -> {
            String command = String.format("%s",storeName.getText());
            pw.write(command);
            pw.println();

            pw.flush();
            int response;
            try {
                response = Integer.parseInt(br.readLine());
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
            if (response == 0) {
                JOptionPane.showMessageDialog(null, "Store creation failed.",
                        "Seller Client", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Store creation successful!",
                        "Seller Client", JOptionPane.INFORMATION_MESSAGE);
            }
            frame.remove(sellerSub);
            frame.add(sellerMain, BorderLayout.CENTER);
            frame.pack();
        });
        sellerSub.add(storeName);
        sellerSub.add(create);
        sellerSub.add(result);
        result.setVisible(true);
        frame.pack();
    }

    //3 "Create Calendar"
    private void s3(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("createCalendar");
        pw.println();

        pw.flush();
        frame.add(sellerSub);
        sellerBack(sellerSub);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                s3(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sellerSub.add(refresh);
        //creation of calendar portion
        JTextField storeName = new JTextField("Example Store", 10);
        JTextField calendarName = new JTextField("Example Calendar", 10);
        JTextField calendarDescription = new JTextField("Example Description", 20);
        JButton create = new JButton("Create");
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = String.format("%s,%s,%s",storeName.getText(),calendarName.getText(),
                        calendarDescription.getText());
                pw.write(command);
                pw.println();

                pw.flush();
                int response = 0;
                try {
                    response = Integer.parseInt(br.readLine());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                if (response == 0) {
                    JOptionPane.showMessageDialog(null, "Calendar creation failed.",
                            "Seller Client", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Calendar creation successful!",
                            "Seller Client", JOptionPane.INFORMATION_MESSAGE);
                }
                frame.remove(sellerSub);
                frame.add(sellerMain, BorderLayout.CENTER);
                frame.pack();
            }
        };
        create.addActionListener(actionListener);
        sellerSub.add(storeName);
        sellerSub.add(calendarName);
        sellerSub.add(calendarDescription);
        sellerSub.add(create);
        frame.pack();
    }

    //4 "Edit Calendar"
    //i need to check for reply of successful or not?
    private void s4(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("editCalendar");
        pw.println();

        pw.flush();
        frame.add(sellerSub);
        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(sellerSub);
                frame.add(sellerMain, BorderLayout.CENTER);
                frame.pack();
            }
        });
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                s4(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sellerSub.add(refresh);
        String[] options = { "Edit Name", "Edit Description", "Add Window", "Delete Window" };

        JComboBox<String> edit = new JComboBox<String>(options);

        sellerSub.add(edit);
        JButton select = new JButton("Select");
        sellerSub.add(select);
        select.addActionListener(ex -> {
            sellerSub.remove(back);
            sellerSub.remove(edit);
            sellerSub.remove(select);
            frame.pack();
            String temp = null;
            try {
                temp = br.readLine();
                System.out.println("calendars read");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            switch (edit.getSelectedIndex()) {
                case 0 -> {
                    pw.write("editCalendarName");
                    System.out.println("selection made");
                    pw.println();
                    pw.flush();
                    JButton submit = new JButton("Edit");
                    String[] calendars = temp.substring(1, temp.length() - 1).split("],\\[");
                    JComboBox<String> selection = new JComboBox<String>(calendars);
                    sellerSub.add(selection);
                    sellerSub.add(submit);
                    JTextField newName = new JTextField("New Name");
                    sellerSub.add(newName);
                    submit.addActionListener(e -> {

                        String[] temp2 = ((String)selection.getSelectedItem()).split(", ");
                        String storeName = temp2[0];
                        String calendarName = temp2[1];
                        String command = String.format("%s,%s,%s",storeName,calendarName,newName.getText());
                        pw.write(command);
                        pw.println();
                        System.out.println(command);
                        pw.flush();
                        int response;
                        try {
                            response = Integer.parseInt(br.readLine());
                        } catch (IOException exc) {
                            throw new RuntimeException(exc);
                        }
                        if (response == 0) {
                            JOptionPane.showMessageDialog(null, "Edit failed.",
                                    "Seller Client", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Edit successful!",
                                    "Seller Client", JOptionPane.INFORMATION_MESSAGE);
                        }
                        frame.remove(sellerSub);
                        frame.add(sellerMain, BorderLayout.CENTER);
                        frame.pack();
                    });
                    System.out.println("made it to the end");
                    frame.pack();
                }
                case 1 -> {
                    pw.write("editCalendarDescription");
                    pw.println();

                    pw.flush();
                    frame.add(sellerSub);
                    JButton submit = new JButton("Edit");

                    String[] calendars = temp.substring(1, temp.length() - 1).split("],\\[");
                    JComboBox<String> selection = new JComboBox<String>(calendars);
                    sellerSub.add(selection);
                    sellerSub.add(edit);
                    sellerSub.add(submit);
                    JTextField calendarDescription = new JTextField("New Description");
                    sellerSub.add(calendarDescription);
                    submit.addActionListener(e -> {
                        String[] temp2 = ((String)selection.getSelectedItem()).split(", ");
                        String storeName = temp2[0];
                        String calendarName = temp2[1];
                        String command = String.format("%s,%s,%s",storeName,
                                calendarName,calendarDescription.getText());
                        pw.write(command);
                        pw.println();

                        pw.flush();
                        int response;
                        try {
                            response = Integer.parseInt(br.readLine());
                        } catch (IOException exc) {
                            throw new RuntimeException(exc);
                        }
                        if (response == 0) {
                            JOptionPane.showMessageDialog(null, "Edit failed.",
                                    "Seller Client", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Edit successful!",
                                    "Seller Client", JOptionPane.INFORMATION_MESSAGE);
                        }
                        frame.remove(sellerSub);
                        frame.add(sellerMain, BorderLayout.CENTER);
                        frame.pack();
                    });

                }
                case 2 -> {
                    pw.write("editCalendarAddWindow");
                    pw.println();

                    pw.flush();
                    frame.add(sellerSub);
                    JButton submit = new JButton("Add");
                    String[] calendars = temp.substring(1, temp.length() - 1).split("],\\[");
                    JComboBox<String> selection = new JComboBox<String>(calendars);
                    sellerSub.add(selection);
                    sellerSub.add(edit);
                    sellerSub.add(submit);

                    JTextField start = new JTextField("0900");
                    JTextField end = new JTextField("1700");
                    JTextField capacity = new JTextField("Enter capacity");
                    JTextField desc = new JTextField("Enter description");
                    sellerSub.add(start);
                    sellerSub.add(end);
                    sellerSub.add(capacity);
                    sellerSub.add(desc);
                    submit.addActionListener(e -> {
                        String[] temp2 = ((String)selection.getSelectedItem()).split(", ");
                        String storeName = temp2[0];
                        String calendarName = temp2[1];
                        String command = String.format("%s,%s,%s,%s,%s,%s",storeName,
                                calendarName,desc.getText(),start.getText(),end.getText(),
                                capacity.getText());
                        pw.write(command);

                        pw.println();

                        pw.flush();
                        int response;
                        try {
                            response = Integer.parseInt(br.readLine());
                        } catch (IOException exc) {
                            throw new RuntimeException(exc);
                        }
                        if (response == 0) {
                            JOptionPane.showMessageDialog(null, "Edit failed.",
                                    "Seller Client", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Edit successful!",
                                    "Seller Client", JOptionPane.INFORMATION_MESSAGE);
                        }
                        frame.remove(sellerSub);
                        frame.add(sellerMain, BorderLayout.CENTER);
                        frame.pack();
                    });

                    frame.pack();
                }
                case 3 -> {
                    pw.write("editCalendarRemoveWindow");
                    pw.println();
                    System.out.println("entered remove window");
                    pw.flush();
                    String[] calendars = temp.substring(1, temp.length() - 1).split("],\\[");
                    JComboBox<String> selection = new JComboBox<String>(calendars);
                    sellerSub.add(selection);
                    JButton calChoice = new JButton("Select");
                    sellerSub.add(calChoice);
                    calChoice.addActionListener(e -> {
                        String[] temp2 = ((String)selection.getSelectedItem()).split(", ");
                        String storeName = temp2[0];
                        String calendarName = temp2[1];
                        String command = String.format("%s,%s", storeName,
                                calendarName);
                        System.out.println("sent command for associated windows");
                        pw.write(command);
                        pw.println();
                        pw.flush();
                        String[] windows;
                        try {
                            String temp3 = br.readLine();
                            System.out.println("found windows");
                            windows = temp3.substring(1, temp3.length() - 1).split("],\\[");
                        } catch (IOException exc) {
                            throw new RuntimeException(exc);
                        }
                        JComboBox<String> winOp = new JComboBox<String>(windows);
                        sellerSub.add(winOp);
                        frame.add(sellerSub);
                        JButton submit = new JButton("Delete");
                        sellerSub.add(edit);
                        sellerSub.add(submit);
                        submit.addActionListener(a -> {

                            String[] temp4 = ((String)winOp.getSelectedItem()).split(", ");
                            String command2 = String.format("%s",temp4[1]);
                            pw.write(command2);
                            pw.println();
                            System.out.println(command2);
                            System.out.println("sent window");
                            pw.flush();
                            int response;
                            try {
                                response = Integer.parseInt(br.readLine());
                                System.out.println("result received");
                            } catch (IOException exc) {
                                throw new RuntimeException(exc);
                            }
                            if (response == 0) {
                                JOptionPane.showMessageDialog(null, "Edit failed.",
                                        "Seller Client", JOptionPane.ERROR_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "Edit successful!",
                                        "Seller Client", JOptionPane.INFORMATION_MESSAGE);
                            }
                            frame.remove(sellerSub);
                            frame.add(sellerMain, BorderLayout.CENTER);
                            frame.pack();
                        });
                        frame.pack();
                    });
                }
            }
        });
    }

    //5 "Delete Calendar"
    private void s5(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("deleteCalendar");
        pw.println();
        pw.flush();
        //ok have a dropdown here
        String calendarNames = br.readLine();
        String[] calSel = calendarNames.substring(1, calendarNames.length()-1).split("],\\[");
        JComboBox<String> calNames = new JComboBox<String>(calSel);
        frame.add(sellerSub);
        sellerSub.add(calNames);

        sellerBack(sellerSub);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                s5(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sellerSub.add(refresh);
        JTextField result = new JTextField();
        result.setVisible(false);
        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> {
            String select = (String)calNames.getSelectedItem();
            String[] temp = select.split(", ");
            String command = String.format("%s,%s",temp[0],temp[1]);
            pw.write(command);
            pw.println();
            pw.flush();
            int response;
            try {
                response = Integer.parseInt(br.readLine());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            if (response == 0) {
                JOptionPane.showMessageDialog(null, "Failed to delete.",
                        "Seller Client", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Deletion successful!",
                        "Seller Client", JOptionPane.INFORMATION_MESSAGE);
            }
            frame.remove(sellerSub);
            frame.add(sellerMain, BorderLayout.CENTER);
            frame.pack();
        });

        sellerSub.add(delete);
        sellerSub.add(result);
        frame.pack();

        result.setVisible(true);
    }

    //6 "Show Statistics"
    private void s6(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("statisticsSeller");
        pw.println();

        pw.flush();
        String[] stores = br.readLine().split(",");
        JComboBox<String> storeOptions = new JComboBox<String>(stores);
        sellerSub.add(storeOptions);
        frame.add(sellerSub);
        sellerBack(sellerSub);
        JTextArea result = new JTextArea(3,20);
        sellerSub.add(result);
        String[] sortOp = new String[] { "Most Popular Window",
                "Customer Appointments"};
        JComboBox<String> sortOptions = new JComboBox<String>(sortOp);
        sellerSub.add(sortOptions);
        JTextField tempWind = new JTextField();
        JTextField tempCust = new JTextField();
        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(e -> {
            pw.write((String)storeOptions.getSelectedItem());
            pw.println();
            pw.flush();
            try {
                tempWind.setText(br.readLine());
                tempCust.setText(br.readLine());
                confirm.setVisible(false);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                s6(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        JButton display = new JButton("Display");
        display.addActionListener(e -> {

            switch (sortOptions.getSelectedIndex()) {
                case 0 -> {
                     result.setText(tempWind.getText());
                }
                case 1 -> {
                    result.setText(tempCust.getText());
                }
            }
        });

        JButton sort = new JButton("Sort");
        sort.addActionListener(e -> {
            pw.write("statisticsSellerOrdered");
            pw.println();
            pw.flush();
            try {
                br.readLine();
                pw.write((String)storeOptions.getSelectedItem());
                pw.println();
                pw.flush();
                tempWind.setText(br.readLine());
                tempCust.setText(br.readLine());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            switch (sortOptions.getSelectedIndex()) {
                case 0 -> {
                    result.setText(tempWind.getText());
                }
                case 1 -> {
                    result.setText(tempCust.getText());
                }
            }
        });
        sellerSub.add(display);
        sellerSub.add(display);
        sellerSub.add(refresh);
        frame.pack();
    }

    //7 "Import Calendar"
    private void s7(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("importCalendar");
        pw.println();

        pw.flush();

        frame.add(sellerSub);
        sellerBack(sellerSub);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                s7(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sellerSub.add(refresh);
        JTextField storeName = new JTextField("Example Store", 10);
        JTextField fileName = new JTextField("File Name", 10);
        JTextField result = new JTextField();
        result.setVisible(false);
        JButton imp = new JButton("Import");
        imp.addActionListener(e -> {
            String command = String.format("s7,%s,%s",storeName.getText(),fileName.getText());
            pw.write(command);
            pw.println();

            pw.flush();
            frame.remove(sellerSub);
            frame.add(sellerMain, BorderLayout.CENTER);
            frame.pack();
            int response;
            try {
                response = Integer.parseInt(br.readLine());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            if (response == 0) {
                JOptionPane.showMessageDialog(null, "Failed to import.",
                        "Seller Client", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Import successful!",
                        "Seller Client", JOptionPane.INFORMATION_MESSAGE);
            }
            result.setVisible(true);
        });
        sellerSub.add(storeName);
        sellerSub.add(fileName);
        sellerSub.add(imp);
        sellerSub.add(result);
        frame.pack();
    }
    private void sellerBack(JPanel panel) {
        JButton back = new JButton("Back");
        back.addActionListener(e -> {
            frame.remove(panel);
            frame.add(sellerMain, BorderLayout.CENTER);
            frame.pack();
        });
        panel.add(back);
    }

    private void customerBack(JPanel panel) {
        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(panel);
                frame.add(customerMain, BorderLayout.CENTER);
                frame.pack();
            }
        });
    }



    public static int exit() {
        //send exit message here
        writer.write("quit");
        writer.flush();
        return JFrame.DISPOSE_ON_CLOSE;
    }
}
