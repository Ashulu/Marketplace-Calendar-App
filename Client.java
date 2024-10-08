import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.Customizer;
import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;

/**
 * Client.java
 *
 * This class is the main GUI for our program, reading and writing to the server to decide what to display
 *
 * @author Gunyoung Park, Ashish Chenna, Ian Lam, Sanjana Gadaginmath
 *
 * @version date
 */
public class Client extends JComponent implements Runnable {

    JPanel loginPanel;
    JPanel createPanel;
    JPanel sellerMain;
    JPanel sellerSub;
    JPanel customerMain;
    JPanel customerSub;
    JPanel requestSub;
    JButton login;
    JButton createAccount;
    JFrame frame;
    static BufferedReader reader;
    static PrintWriter writer;

    public void run(){
        frame = new JFrame("Calendar");

        frame.setSize(500,500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginPage();

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        String host = JOptionPane.showInputDialog(null, "Enter the IP you want to connect to:",
                "Calendar System", JOptionPane.QUESTION_MESSAGE);
        int port = Integer.parseInt(JOptionPane.showInputDialog(null,
                "Enter the port number you want to connect to", "Calendar System", JOptionPane.QUESTION_MESSAGE));
        try {
            Socket socket = new Socket(host,port);
            JOptionPane.showMessageDialog(null, "Client connected");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());

            SwingUtilities.invokeLater(new Client());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void creationPanel(BufferedReader br, PrintWriter pw) {
        pw.println("createAccount");
        pw.flush();

        JLabel createLabel = new JLabel("Account creation");
        createPanel.add(createLabel);

        JTextField userField = new JTextField("Email", 30);
        createPanel.add(userField);

        JTextField passField = new JTextField("Password", 30);
        createPanel.add(passField);

        String[] choices = {"Customer", "Seller"};

        JComboBox<String> types = new JComboBox<>(choices);
        createPanel.add(types);

        JButton create = new JButton("Create Account");
        createPanel.add(create);

        JButton back = new JButton("Back");
        createPanel.add(back);

        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userField.getText().equals("Email") || passField.getText().equals("Password")) {
                    JOptionPane.showMessageDialog(frame.getContentPane(), "Enter an email and/or password");
                } else {
                    try {

                        pw.println(String.format("%s,%s,%s", types.getSelectedItem(), userField.getText(),
                                passField.getText()));
                        pw.flush();

                        int check = Integer.parseInt(br.readLine());
                        if (check == 0) {
                            JOptionPane.showMessageDialog(frame.getContentPane(), "Account already Exists!");
                        } else if (check == 1) {
                            JOptionPane.showMessageDialog(frame.getContentPane(), "Account created!");
                            loginPanel = new JPanel();
                            loginPanel.setBounds(0, 0, 30, frame.getHeight());
                            loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
                            loginPage();
                            frame.remove(createPanel);
                            frame.add(loginPanel, BorderLayout.CENTER);
                            frame.pack();
                        } else {
                            JOptionPane.showMessageDialog(frame.getContentPane(), "An error occurred!");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
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
                creationPanel(reader, writer);
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
                            case 0 -> JOptionPane.showMessageDialog(frame.getContentPane(),
                                    "Password is wrong!");
                            case -1 -> JOptionPane.showMessageDialog(frame.getContentPane(),
                                    "Account not made yet!");
                        }
                    } catch (IOException a) {
                        System.out.println("This is the reason");
                        a.printStackTrace();
                    }
                }
            }
        });
    }

    // ------------------------
    // START OF CUSTOMER GUI
    // ------------------------

    private void customer(BufferedReader br, PrintWriter pw) throws IOException {
        JLabel welcome = new JLabel("Welcome Customer! \nPlease Select what you would like to do:");
        customerMain.add(welcome);

        String[] customerOp = new String[]{
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
                            frame.remove(customerMain);
                            frame.add(customerSub);
//                    frame.repaint();
                            frame.pack();
                            frame.setVisible(true);
                            c0(br, pw);
                        }
                        case 1 -> {
                            frame.remove(customerMain);
                            frame.add(customerSub);
//                    frame.repaint();
                            frame.pack();
                            frame.setVisible(true);
                            c1(br, pw);
                        }
                        case 2 -> {
                            frame.remove(customerMain);
                            frame.add(customerSub);
//                    frame.repaint();
                            frame.pack();
                            frame.setVisible(true);
                            c2(br, pw);
                        }
                        case 3 -> {
                            frame.remove(customerMain);
                            frame.add(customerSub);
//                    frame.repaint();
                            frame.pack();
                            frame.setVisible(true);
                            c3(br, pw);
                        }
                        case 4 -> {
                            frame.remove(customerMain);
                            frame.add(customerSub);
//                    frame.repaint();
                            frame.pack();
                            frame.setVisible(true);
                            c4(br, pw);
                        }
                        case 5 -> {
                            frame.remove(customerMain);
                            frame.add(loginPanel, BorderLayout.CENTER);
                            frame.repaint();
                            frame.pack();
                            return;
                        }
                        case 6 -> {
                            frame.remove(customerMain);
                            exit();
                        }
                    }
                } catch (Exception a) {
                    a.printStackTrace();
                }
            }
        });
    }


    private void c0(BufferedReader br, PrintWriter pw) throws IOException {
        pw.println("viewCalendar");
        pw.flush();

        //get all store names from server and print out in dropdown to user
        //once user selects, get all calendars from that specific store and print out
        // user should be able to select based on a dropdown

        String windows = br.readLine();

        String calendarNames = br.readLine();
        if (calendarNames.length() > 0) {
            JButton refresh = new JButton("Refresh");
            refresh.addActionListener(e -> {
                try {
                    frame.remove(customerSub);
                    customerSub = new JPanel();
                    customerSub.setLayout(new BoxLayout(customerSub, BoxLayout.Y_AXIS));
                    c0(br,pw);
                    frame.add(customerSub, BorderLayout.CENTER);
                    frame.pack();
                    frame.setVisible(true);

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            customerSub.add(refresh);

            String[] strCalChoices = calendarNames.substring(1, calendarNames.length() - 1).split("],\\[");
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
                    if (windows.length() > 0) {
                        String[] appointmentWindowsData = windows.substring(1,
                                windows.length() - 1).split("],\\[");
                        String result = "";
                        for (int i = 0; i < appointmentWindowsData.length; i++) {
                            String[] data = appointmentWindowsData[i].split(",");
                            if (data[0].equals(choice)) {
                                String windowData = String.format("Appointment Title: %s \nTime: %s - %s " +
                                                "\nMaximum Attendees: %s \nCurrent Bookings: %s \n",
                                        data[1], data[2], data[3], data[4], data[5]);
                                result += windowData;
                            }
                        }
                        if (result.isEmpty()) {
                            displayWindows.setText("Calendar Description: " +
                                    calInfo[calendarChoices.getSelectedIndex()] + "\n\nNo appointments were found!");
                        } else {
                            displayWindows.setText("Calendar Description: " +
                                    calInfo[calendarChoices.getSelectedIndex()] + "\n\n" + result);
                        }
                        customerSub.add(displayWindows);
                        frame.pack();
                    } else {
                        JOptionPane.showMessageDialog(frame.getContentPane(), "No windows made.");
                        pw.println("break");
                        pw.flush();
                        frame.remove(customerSub);
                        frame.add(customerMain, BorderLayout.CENTER);
                        frame.pack();
                        frame.setVisible(true);
                    }
                }
            });
            customerSub.add(enter);
        } else {
            JOptionPane.showMessageDialog(frame.getContentPane(), "No calendars created");
            pw.println("break");
            pw.flush();
            customerMain = new JPanel();
            customerMain.setLayout(new BoxLayout(customerMain, BoxLayout.Y_AXIS));
            customer(br, pw);
            frame.remove(customerSub);
            frame.add(customerMain, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
        }
        customerBack(customerSub);
    }

    private void c1(BufferedReader br, PrintWriter pw) throws IOException {
        pw.println("requestAppointment");
        pw.flush();

        String allInfo = br.readLine();
        if (allInfo.length() > 0) {
            JLabel welcome = new JLabel("Select which store and calendar you would like to request");
            customerSub.add(welcome);

            String[] storesAndCalendars = allInfo.substring(1, allInfo.length() - 1).split("],\\[");
            int len = storesAndCalendars.length;

            ArrayList<String> listChoice = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                String[] splitted = storesAndCalendars[i].split(", ");
                listChoice.add(splitted[0] + " : " + splitted[1]);
            }

            String[] arrChoices = listChoice.toArray(new String[listChoice.size()]);
            JComboBox<String> choices = new JComboBox<>(arrChoices);
            customerSub.add(choices);

            JButton selection = new JButton("Select");
            customerSub.add(selection);

            JButton refresh = new JButton("Refresh");
            customerSub.add(refresh);
            refresh.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        frame.remove(customerSub);
                        customerSub = new JPanel();
//                        customerSub.setBounds(0, 0, 30, frame.getHeight());
                        customerSub.setLayout(new BoxLayout(customerSub, BoxLayout.Y_AXIS));
                        pw.println("break");
                        pw.flush();
                        c1(br, pw);
                        frame.add(customerSub);
                        frame.pack();
                        frame.setVisible(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            customerBackBreak(customerSub, pw);
            selection.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String choice = (String) choices.getSelectedItem();
                        System.out.println(choice);

                        String[] splitted = choice.split(" : ");
                        System.out.println(String.format("%s,%s", splitted[0], splitted[1]));
                        pw.println(String.format("%s,%s", splitted[0], splitted[1]));
                        pw.flush();

                        System.out.println("before");
                        String windows = br.readLine();
                        System.out.println("after");
                        System.out.println("windows: " + windows);

                        requestSub = new JPanel();
//                        requestSub.setBounds(0, 0, 30, frame.getHeight());
                        requestSub.setLayout(new BoxLayout(requestSub, BoxLayout.Y_AXIS));
                        requestSub(br, pw, windows);
                        frame.remove(customerSub);
                        frame.add(requestSub);
                        frame.pack();
                        frame.setVisible(true);
                    } catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            });
        } else {
            try {
                JOptionPane.showMessageDialog(frame.getContentPane(), "No Windows made");
                pw.print("break");
                pw.flush();
                customerMain = new JPanel();
//                customerMain.setBounds(0, 0, 30, frame.getHeight());
                customerMain.setLayout(new BoxLayout(customerMain, BoxLayout.Y_AXIS));
                customer(br, pw);
                frame.remove(requestSub);
                frame.add(customerMain, BorderLayout.CENTER);
                frame.pack();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void requestSub(BufferedReader br, PrintWriter pw, String windows) {
        System.out.println("reaches");
        if (windows.length() > 0) {
            JLabel welcome = new JLabel("Select which windows you want");
            requestSub.add(welcome);

            String[] windowSplit = windows.split("],\\[");
            int len = windowSplit.length;
            windowSplit[0] = windowSplit[0].substring(1);
            windowSplit[len - 1] = windowSplit[len - 1].substring(0, windowSplit[len - 1].length() - 1);

            ArrayList<String> listChoice = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                String[] windowSplitted = windowSplit[i].split(",");
                listChoice.add(String.format("%s -%s, Current booking: %s",windowSplitted[0],
                        windowSplitted[1], windowSplitted[3]));
            }

            String[] arrChoices = listChoice.toArray(new String[listChoice.size()]);
            JComboBox<String> choices = new JComboBox<>(arrChoices);
            requestSub.add(choices);

            JTextField bookingInfo = new JTextField("How many people are attenting this event");
            requestSub.add(bookingInfo);

            JButton selection = new JButton("Select");
            requestSub.add(selection);

            selection.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String choice = (String) choices.getSelectedItem();
                        String[] split1 = choice.split(",");
                        String[] split2 = split1[0].split(" - ");
                        pw.println(String.format("%s,%s,%s", split2[0], split2[1], bookingInfo.getText()));
                        pw.flush();
                        System.out.println("before");
                        int result = Integer.parseInt(br.readLine());
                        //doesn't reach this print
                        System.out.println("result: " + result);
                        switch (result) {
                            case 1:
                                JOptionPane.showMessageDialog(frame.getContentPane(),
                                        "Request made!");
                                break;
                            case 0:
                                JOptionPane.showMessageDialog(frame.getContentPane(),
                                        "This window has reached capacity.");
                                break;
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            customerBackBreak(requestSub, pw);
            frame.pack();
        } else {
            try {
                JOptionPane.showMessageDialog(frame.getContentPane(), "No Windows made");
                pw.print("break");
                pw.flush();
                customerMain = new JPanel();
//                customerMain.setBounds(0, 0, 30, frame.getHeight());
                customerMain.setLayout(new BoxLayout(customerMain, BoxLayout.Y_AXIS));
                customer(br, pw);
                frame.remove(requestSub);
                frame.add(customerMain, BorderLayout.CENTER);
                frame.pack();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void c2(BufferedReader br, PrintWriter pw) throws IOException {
        pw.println("cancelRequest");
        pw.flush();

        String clientRequests = br.readLine();
        System.out.println("requests: " + clientRequests);
        if (clientRequests.length() > 0) {
            JButton refresh = new JButton("Refresh");
            refresh.addActionListener(e -> {
                try {
                    frame.remove(customerSub);
                    customerSub = new JPanel();
                    customerSub.setLayout(new BoxLayout(customerSub, BoxLayout.Y_AXIS));
                    pw.println("break");
                    c2(br,pw);
                    frame.add(customerSub, BorderLayout.CENTER);
                    frame.pack();
                    frame.setVisible(true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            customerSub.add(refresh);

            String[] requests = clientRequests.substring(1, clientRequests.length() - 1).split("],\\[");
            String result = "";
            for (int i = 0; i < requests.length; i++) {
                String[] data = requests[i].split(",");
                result += (i + 1) + ". \n" + "Store Name: " + data[0] + "\nCalendar Name: " + data[1] +
                        "\nTime Window: " + data[2] + " - " + data[3] + "\nBookings: " + data[4] + "\n\n";
            }

            JTextField allAppointments = new JTextField(result, 50);
            customerSub.add(allAppointments);
            String[] options = new String[requests.length];
            for (int i = 1; i <= requests.length; i++) {
                options[i - 1] = String.valueOf(i);
            }
            JComboBox<String> appointment = new JComboBox<String>(options);
            customerSub.add(appointment);
            JButton delete = new JButton("Delete");
            delete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String[] choice = requests[appointment.getSelectedIndex()].split(",");
                    String command = String.format("%s,%s,%s", choice[0], choice[1], choice[2]);
                    pw.println(command);
                    pw.flush();
                }
            });
            customerSub.add(delete);

            JTextField success = new JTextField();
            customerSub.add(success);

            int response = br.read();
            if (response == 0) {
                success.setText("Unable to delete appointment.");
            } else {
                success.setText("Deletion successful!");
            }

            success.setVisible(true);

            customerBackBreak(customerSub, pw);
        } else {
            JOptionPane.showMessageDialog(frame.getContentPane(), "No requests made");
            pw.println("break");
            pw.flush();
            customerMain = new JPanel();
//            customerMain.setBounds(0, 0, 30, frame.getHeight());
            customerMain.setLayout(new BoxLayout(customerMain, BoxLayout.Y_AXIS));
            customer(br, pw);
            frame.remove(customerSub);
            frame.add(customerMain, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
        }

    }

    private void c3(BufferedReader br, PrintWriter pw) throws IOException {
        pw.println("viewApproved");
        pw.flush();

        // code to view approved appointments
        String data = br.readLine();
        if (data.length() > 0) {
            JButton refresh = new JButton("Refresh");
            refresh.addActionListener(e -> {
                try {
                    frame.remove(customerSub);
                    customerSub = new JPanel();
                    customerSub.setLayout(new BoxLayout(customerSub, BoxLayout.Y_AXIS));
                    c3(br,pw);
                    frame.add(customerSub, BorderLayout.CENTER);
                    frame.pack();
                    frame.setVisible(true);

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            customerSub.add(refresh);

            String[] appointments = data.substring(1, data.length() - 1).split("],\\[");
            String result = "";
            for (int i = 0; i < appointments.length; i++) {
                String[] appt = appointments[i].split(",");
                result += (i + 1) + ". \n" + "Store Name: " + appt[0] + "\nCalendar Name: " + appt[1] +
                        "\nTime Window: " + appt[2] + " - " + appt[3] + "\nBookings: " + appt[4] + "\n\n";
            }

            JTextField displayAppointments = new JTextField(50);
            displayAppointments.setText(result);
            customerSub.add(displayAppointments);
            displayAppointments.setVisible(true);
            customerBack(customerSub);
        } else {
            JOptionPane.showMessageDialog(frame.getContentPane(), "No appointments made");
            pw.println("break");
            pw.flush();
            customerMain = new JPanel();
//            customerMain.setBounds(0, 0, 30, frame.getHeight());
            customerMain.setLayout(new BoxLayout(customerMain, BoxLayout.Y_AXIS));
            customer(br, pw);
            frame.remove(customerSub);
            frame.add(customerMain, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
        }

    }
  
  
    private void c4(BufferedReader br, PrintWriter pw) throws IOException{
        JLabel askSort = new JLabel("Would you like to sort your statistics?");
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

                    showStats(br, pw);

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

                    showStats(br, pw);

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
                    c4(br, pw);
                    frame.add(customerSub);
                    frame.pack();
                    frame.setVisible(true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        customerBack(customerSub);

    }  

    //shows the stats for customers, regardless of whether its sorted or not
    public void showStats(BufferedReader br, PrintWriter pw) throws IOException{
        String storeString = br.readLine();
        String stats = br.readLine();
        String[] split = stats.split("],\\[");
        if (split.length > 0) {
            split[0] = split[0].substring(2);
            split[split.length - 1] = split[split.length - 1].substring(0, split[split.length - 1].length() - 1);

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
            frame.pack();
        } else {
            JOptionPane.showMessageDialog(frame.getContentPane(), "No statistics to be shown");
            pw.println("break");
            pw.flush();
            frame.remove(customerSub);
            frame.add(customerMain, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);
        }
    }


    // -------------------
    // START OF SELLER GUI
    // -------------------

    //Seller landing page
    private void seller(BufferedReader br, PrintWriter pw) throws IOException {
        //frame.removeAll();
        sellerMain = new JPanel();
        frame.add(sellerMain);
        JLabel welcome = new JLabel("Hello Seller! \nPlease Select what you would like to do.");
        sellerMain.add(welcome);

        String[] selOp = new String[] {
                "View Approved Appointments", "Appointment Requests",
                "Create Store", "Create Calendar",
                "Edit Calendar", "Delete Calendar",
                "Show Statistics", "Import Calendar",
                "Logout", "Quit"
        };
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
        frame.pack();
    }
    //0 "View Approved Appointments"
    private void s0(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("showApproved");
        pw.println();
        pw.flush();

        sellerBack(sellerSub, pw);

        JTextField stop = new JTextField();

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                sellerSub = new JPanel();
                sellerSub.setLayout(new BoxLayout(sellerSub, BoxLayout.Y_AXIS));
                stop.setText("stop");
                pw.write("break");
                pw.println();
                pw.flush();
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
        JTextField stop = new JTextField();

        sellerBack(sellerSub, pw);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                sellerSub = new JPanel();
                sellerSub.setLayout(new BoxLayout(sellerSub, BoxLayout.Y_AXIS));
                stop.setText("stop");
                pw.write("break");
                pw.println();
                pw.flush();
                sellerSub = new JPanel();

                s1(br,pw);
                frame.add(sellerSub, BorderLayout.CENTER);
                frame.pack();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sellerSub.add(refresh);
        if (!stop.getText().isEmpty()) {
            return;
        }
        String temp = br.readLine();
        if (temp.isEmpty()) {
            JTextField result = new JTextField("No Appointment Requests.");
            sellerSub.add(result);
        } else {
            String[] requests = temp.substring(1, temp.length() - 1).split("],\\[");
            JComboBox<String> appointments = new JComboBox<String>(requests);
            JButton confirm = new JButton("Confirm");
            confirm.addActionListener(e -> {
                String[] temp2 = ((String) appointments.getSelectedItem()).split(", ");
                String command = String.format("confirm,%s,%s,%s,%s,%s",
                        temp2[0], temp2[1], temp2[2], temp2[3], temp2[5]);
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
                String[] temp2 = ((String) appointments.getSelectedItem()).split(", ");
                String command = String.format("delete,%s,%s,%s,%s,%s",
                        temp2[0], temp2[1], temp2[2], temp2[3], temp2[5]);
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
    }

    //2 "Create Store"
    private void s2(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("createStore");
        pw.println();
        pw.flush();
        JTextField stop = new JTextField();
        frame.add(sellerSub);
        sellerBack(sellerSub, pw);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                stop.setText("stop");
                pw.write("break");
                pw.println();
                pw.flush();
                sellerSub = new JPanel();

                s2(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sellerSub.add(refresh);
        JTextField storeName = new JTextField("Example Name", 10);
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
        frame.pack();
    }

    //3 "Create Calendar"
    private void s3(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("createCalendar");
        pw.println();

        pw.flush();
        JTextField stop = new JTextField();
        frame.add(sellerSub);
        sellerBack(sellerSub, pw);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                stop.setText("stop");
                pw.write("break");
                pw.println();
                pw.flush();
                sellerSub = new JPanel();

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
        JTextField stop = new JTextField();
        frame.add(sellerSub);
        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pw.write("break");
                pw.println();
                pw.flush();
                frame.remove(sellerSub);
                frame.add(sellerMain, BorderLayout.CENTER);
                frame.pack();
            }
        });
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                stop.setText("stop");
                pw.write("break");
                pw.println();
                pw.flush();
                sellerSub = new JPanel();
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
                if (!stop.getText().isEmpty()) {
                    return;
                }
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
                            if (temp3.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "No windows.",
                                        "Seller Client", JOptionPane.ERROR_MESSAGE);
                                back.doClick();
                                return;
                            }
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
        JTextField stop = new JTextField();
        //ok have a dropdown here
        String calendarNames = br.readLine();
        String[] calSel = calendarNames.substring(1, calendarNames.length()-1).split("],\\[");
        JComboBox<String> calNames = new JComboBox<String>(calSel);
        frame.add(sellerSub);
        sellerSub.add(calNames);

        sellerBack(sellerSub, pw);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                stop.setText("stop");
                pw.write("break");
                pw.println();
                pw.flush();
                //consumes the result spat out
                br.readLine();
                sellerSub = new JPanel();
                s5(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sellerSub.add(refresh);

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
        frame.pack();

    }

    //6 "Show Statistics"
    private void s6(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("showStores");
        pw.println();
        System.out.println("entered clientside");
        pw.flush();
        JTextField stop = new JTextField();
        String temp = br.readLine();
        String[] stores = temp.substring(1, temp.length() - 1).split(",");
        JComboBox<String> storeOptions = new JComboBox<String>(stores);
        sellerSub.add(storeOptions);
        frame.add(sellerSub);
        sellerBack(sellerSub, pw);
        JTextArea result = new JTextArea(3,20);
        sellerSub.add(result);
        String[] sortOp = new String[] { "Most Popular Window",
                "Customer Appointments"};
        JComboBox<String> sortOptions = new JComboBox<String>(sortOp);
        sortOptions.setVisible(false);
        sellerSub.add(sortOptions);
        JTextField tempWind = new JTextField();
        JTextField tempCust = new JTextField();
        JTextField tempBool = new JTextField();
        JButton confirm = new JButton("Confirm");
        confirm.addActionListener(e -> {
            sortOptions.setVisible(true);
            pw.write((String)storeOptions.getSelectedItem());
            System.out.println("selected store");
            pw.println();
            pw.flush();
            try {

                String tempWind2 = br.readLine();
                String tempCust2 = br.readLine();
                System.out.println(tempCust2);
                tempWind2 = tempWind2.substring(1, tempWind2.length() - 1);
                tempCust2 = tempCust2.substring(1, tempCust2.length() - 1);
                tempWind2 = tempWind2.replace("],[", " | ");
                tempCust2 = tempCust2.replace("],[", " | ");
                tempWind.setText(tempWind2);
                tempCust.setText(tempCust2);

                System.out.println("store stats unsorted received");
                confirm.setVisible(false);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            JButton display = new JButton("Display");
            display.addActionListener(ex -> {
                switch (sortOptions.getSelectedIndex()) {
                    case 0 -> {
                        result.setRows(5);
                        result.setText(tempWind.getText());
                    }
                    case 1 -> {
                        result.setRows(5);
                        result.setText(tempCust.getText());
                    }
                }

            });
            sellerSub.add(display);
            JButton sort = new JButton("Sort");
            sort.addActionListener(ex -> {
                pw.write("statisticsSellerOrdered");
                pw.println();
                System.out.println("ask for ordered");
                pw.flush();
                try {
                    if (tempBool.getText().isEmpty()) {
                        tempBool.setText("has text here");
                        pw.write((String) storeOptions.getSelectedItem());
                        pw.println();
                        pw.flush();

                        String tempWind2 = br.readLine();
                        String tempCust2 = br.readLine();
                        System.out.println(tempCust2);

                        tempWind2 = tempWind2.substring(1, tempWind2.length() - 1);
                        tempCust2 = tempCust2.substring(1, tempCust2.length() - 1);
                        tempWind2 = tempWind2.replace("],[", " | ");
                        tempCust2 = tempCust2.replace("],[", " | ");
                        tempWind.setText(tempWind2);
                        tempCust.setText(tempCust2);
                        System.out.println("received ordered");
                    }
                } catch (IOException exc) {
                    throw new RuntimeException(exc);
                }
            });
            sellerSub.add(sort);
        });

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                frame.remove(sellerSub);
                stop.setText("stop");
                pw.write("break");
                pw.println();
                pw.flush();
                sellerSub = new JPanel();
                s6(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        sellerSub.add(confirm);
        sellerSub.add(refresh);
        frame.pack();
    }

    //7 "Import Calendar"
    private void s7(BufferedReader br, PrintWriter pw) throws IOException {
        pw.write("importCalendar");
        pw.println();

        pw.flush();
        JTextField stop = new JTextField();
        frame.add(sellerSub);
        sellerBack(sellerSub, pw);
        String temp = br.readLine();
        String[] stores = temp.substring(1, temp.length() - 1).split("],\\[");
        JComboBox<String> storeOp = new JComboBox<String>(stores);
        JButton confirm = new JButton("Confirm");
        JTextField fileName = new JTextField("File Name");
        sellerSub.add(storeOp);
        sellerSub.add(fileName);

        sellerSub.add(confirm);
        confirm.addActionListener(e -> {
            String[] temp2 = ((String)storeOp.getSelectedItem()).split(", ");
            String command = String.format("%s,%s",temp2[1],fileName.getText());
            pw.write(command);
            pw.println();
            pw.flush();
            System.out.println(command);

            frame.remove(sellerSub);
            frame.add(sellerMain, BorderLayout.CENTER);

            frame.pack();
        });
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> {
            try {
                stop.setText("stop");
                frame.remove(sellerSub);
                pw.write("break");
                pw.println();
                pw.flush();
                sellerSub = new JPanel();

                s7(br,pw);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        sellerSub.add(refresh);

        frame.pack();
    }
    private void sellerBack(JPanel panel, PrintWriter pw) {
        JButton back = new JButton("Back");
        panel.add(back);
        back.addActionListener(e -> {
            pw.write("break");
            pw.println();
            pw.flush();
            frame.remove(panel);
            try {
                seller(reader,pw);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            frame.pack();
        });
        panel.add(back);
    }

    private void customerBack(JPanel panel) {
        JButton back = new JButton("Back");
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.remove(panel);
                frame.add(customerMain, BorderLayout.CENTER);
                frame.pack();
            }
        });
    }

    private void customerBackBreak(JPanel panel, PrintWriter pw) {
        JButton back = new JButton("Back");
        panel.add(back);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pw.println("break");
                pw.flush();
                frame.remove(panel);
                frame.add(customerMain, BorderLayout.CENTER);
                frame.pack();
            }
        });
    }



    public static void exit() {
        //send exit message here
        writer.write("quit");
        writer.flush();
        System.exit(1);
    }
}
